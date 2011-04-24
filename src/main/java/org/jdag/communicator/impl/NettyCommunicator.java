/*
 *  This product is licenced under GNU public licence.
 *  A copy of the licence can be obtained from,
 *
 *   http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.jdag.communicator.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.CommunicationException;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.ServerChannelFactory;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.jboss.netty.logging.AbstractInternalLogger;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jdag.common.NamedThreadFactory;
import org.jdag.common.Pair;
import org.jdag.common.log.LogFactory;
import org.jdag.communicator.Communicator;
import org.jdag.communicator.HostID;
import org.jdag.communicator.Message;
import org.jdag.communicator.MessageMarshaller;
import org.jdag.communicator.Reactor;

/**
 * An implementation of communicator using JBoss Netty NIO framework.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class NettyCommunicator implements Communicator
{
    /** The logger */
    private static final Logger LOG =
        LogFactory.getLogger(NettyCommunicator.class);

    /**
     * Delimiter used for identifying message boundaries.
     */
    private static final byte DELIMITER = 'm';

    /**
     * The thread factory to be used for boss threads.
     */
    private static final String BOSS_TF_NAME = "NettyBoss";

    /**
     * The thread factory to be used for worker threads.
     */
    private static final String WORKER_TF_NAME = "NettyWorker";

    /**
     * The thread pool to be used for processing the received messages
     * from the quque.
     */
    private final ExecutorService myCommExecutor;

    /**
     * The thread pool to be used for running non blocking server threads.
     */
    private final ExecutorService myBossExecutor;

    /** Used for converting the messages to binary format */
    private final MessageMarshaller myMarshaller;

    /**
     * The identifier of the host to which this address is connected to.
     */
    private final HostID myHostID;

    /** Maps a Reactor to its MessageType */
    private final Multimap<Class<? extends Message>, Pair<Reactor, Executor>>
        myMessageToReactorMap;

    /**
     * The channel factory to be used for creating <code>ServerChannel</code>s
     */
    private final ServerChannelFactory myServerChannelFactory;

    /**
     * The <code>ServerChannel</code> to be used for communication.
     */
    private Channel myServerChannel;

   /* static {
        InternalLoggerFactory.setDefaultFactory(new NettyLoggerFactory());
    }*/

    /**
     * Custom logger to be injected into the netty.
     */
    private static class NettyLogger extends AbstractInternalLogger
    {
        private final Logger myLogger;

        /**
         * CTOR
         */
        public NettyLogger(Logger logger)
        {
            myLogger = logger;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void debug(String msg)
        {
            myLogger.info(msg);

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void debug(String msg, Throwable cause)
        {
            myLogger.log(Level.INFO, msg, cause);

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void error(String msg)
        {
            myLogger.info(msg);

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void error(String msg, Throwable cause)
        {
            myLogger.log(Level.INFO, msg, cause);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void info(String msg)
        {
            myLogger.info(msg);

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void info(String msg, Throwable cause)
        {
            myLogger.log(Level.INFO, msg, cause);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isDebugEnabled()
        {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isErrorEnabled()
        {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isInfoEnabled()
        {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isWarnEnabled()
        {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void warn(String msg)
        {
            myLogger.info(msg);

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void warn(String msg, Throwable cause)
        {
            myLogger.log(Level.INFO, msg, cause);
        }

    }

    /**
     * Factory for creating our cutom logger for netty.
     */
    private static class NettyLoggerFactory extends InternalLoggerFactory
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public InternalLogger newInstance(String name)
        {
            return new NettyLogger(LogFactory.getLogger(name));
        }
    }

    /**
     * The handler to be used for receiving messages.
     */
    private class MessageReceiver extends SimpleChannelUpstreamHandler
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
        {
            Message m = (Message) e.getMessage();
            synchronized(myMessageToReactorMap) {
                for (Pair<Reactor, Executor> reactorExecPair :
                    myMessageToReactorMap.get(m.getClass()))
                {
                    ExecutableReactor reactor =
                        new ExecutableReactor(m, reactorExecPair.getFirst());
                    if (reactorExecPair.getSecond() != null) {
                        reactorExecPair.getSecond().execute(reactor);
                    }
                    else {
                        reactor.run();
                    }
                }
            }
        }
    }

    /**
     * Extends <code>OneToOneEncoder</code> to support
     * transferring the <code>Message</code> to byte
     * stream.
     */
    private static class MessageEncoder extends OneToOneEncoder
    {
        private final MessageMarshaller myMessageMarshaller;

        /**
         * CTOR
         */
        public MessageEncoder(MessageMarshaller marshaller)
        {
            myMessageMarshaller = marshaller;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Object encode(ChannelHandlerContext ctx, Channel channel,
                Object obj) throws Exception
        {
            if (obj instanceof Message) {
                ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
                Message msg = (Message) obj;
                byte[] serializedData = myMessageMarshaller.marshal(msg);
                buffer.writeByte(DELIMITER);
                buffer.writeInt(serializedData.length);
                buffer.writeBytes(serializedData);
                LOG.info("Sending " + serializedData.length + "bytes");
                return buffer;
            }
            else {
                return null;
            }
        }
    }

    /**
     * The decoder to be used for handling the byte streams.
     */
    private static class MessageDecoder extends FrameDecoder
    {
        private final MessageMarshaller myMarshaller;

        /**
         * CTOR
         */
        public MessageDecoder(MessageMarshaller marshaller)
        {
            myMarshaller = marshaller;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Object decode(
            ChannelHandlerContext ctx,
            Channel channel,
            ChannelBuffer buffer)
            throws Exception
        {
            LOG.info("In decoder");
           if (buffer.readableBytes() < 5) {
               LOG.info("No bytes < 5");
               return null;
           }
           buffer.markReaderIndex();
           byte delimiter = buffer.readByte();
           if (delimiter != DELIMITER) {
               buffer.resetReaderIndex();
               throw new CommunicationException("Frame is corrupted " + delimiter);
           }
           int size = buffer.readInt();
           if (buffer.readableBytes() < size) {
               buffer.resetReaderIndex();
               LOG.info("No bytes is < " + size);
               return null;
           }
           byte[] serializedData = new byte[size];
           Message m = myMarshaller.unmarshal(serializedData);
           LOG.info("Deserialized message " + m);
           return m;
        }
    }

    private class MessagePipelineFactory
        implements ChannelPipelineFactory
    {
        private final MessageMarshaller myMarshaller;

        private final boolean myClient;

        /**
         * CTOR
         */
        public MessagePipelineFactory(MessageMarshaller marshaller)
        {
            this(marshaller, false);
        }

        /**
         * CTOR
         */
        public MessagePipelineFactory(MessageMarshaller marshaller,
                                      boolean client)
        {
            myMarshaller = marshaller;
            myClient = client;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ChannelPipeline getPipeline() throws Exception
        {
             ChannelPipeline pipeline = Channels.pipeline();
             pipeline.addLast(MessageDecoder.class.getName(),
                              new MessageDecoder(myMarshaller));
             pipeline.addLast(MessageEncoder.class.getName(),
                              new MessageEncoder(myMarshaller));

             if (!myClient) {
                pipeline.addLast(MessageReceiver.class.getName(),
                                 new MessageReceiver());
            }
            return pipeline;
        }
    }

    /**
     * CTOR
     */
    @Inject
    public NettyCommunicator(MessageMarshaller messageMarshaller,
                             CommunicatorConfig config)
    {
        myMarshaller = messageMarshaller;
        myHostID = new HostID(config.getSocketAddress());
        myMessageToReactorMap =
            HashMultimap.<Class<? extends Message>,
                          Pair<Reactor, Executor>>create();

        myCommExecutor =
            Executors.newSingleThreadExecutor(
                NamedThreadFactory.newNamedFactory(WORKER_TF_NAME));
        myBossExecutor =
            Executors.newSingleThreadExecutor(
                NamedThreadFactory.newNamedFactory(BOSS_TF_NAME));

        myServerChannelFactory =
            new NioServerSocketChannelFactory(myBossExecutor, myCommExecutor);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachReactor(Class<? extends Message> type, Reactor r)
    {
        synchronized (myMessageToReactorMap) {
            myMessageToReactorMap.put(type,
                                       new Pair<Reactor, Executor>(r, null));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachReactor(Class<? extends Message> type, Reactor r, Executor e)
    {
        synchronized (myMessageToReactorMap) {
            myMessageToReactorMap.put(type,
                                       new Pair<Reactor, Executor>(r, e));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HostID getMyHostID()
    {
        return myHostID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(HostID host, Message m)
    {
         System.out.println("Sending message to " + host);
         ClientBootstrap clientBootStrap =
             new ClientBootstrap(
                 new NioClientSocketChannelFactory(myBossExecutor,
                                                   myCommExecutor));
         clientBootStrap.setPipelineFactory(
             new MessagePipelineFactory(myMarshaller, true));

         ChannelFuture future = clientBootStrap.connect(host.getHostAddress());
         LOG.info("waiting to connect to " + host.getHostAddress());
         future.awaitUninterruptibly();
         Channel c = future.getChannel();
         ChannelFuture f = c.write(m);
         f.awaitUninterruptibly();
         LOG.info("Message sent " + f.isSuccess());
         c.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start()
    {
        ServerBootstrap bootstrap = new ServerBootstrap(myServerChannelFactory);
        bootstrap.setPipelineFactory(new MessagePipelineFactory(myMarshaller));
        try {
            bootstrap.bind(myHostID.getHostAddress());
        }
        catch (Throwable e) {
             throw new RuntimeException(e);
        }
        LOG.info("Done binding with the channel");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop()
    {
        ChannelFuture future = myServerChannel.close();
        try {
            future.await();
        }
        catch (InterruptedException e) {
            LOG.log(Level.INFO, e.getMessage(), e);
        }
        myServerChannelFactory.releaseExternalResources();
    }
}
