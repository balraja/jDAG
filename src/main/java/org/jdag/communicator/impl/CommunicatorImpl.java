package org.jdag.communicator.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.SimpleString;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.MessageHandler;

import org.jdag.common.NamedThreadFactory;
import org.jdag.common.Pair;
import org.jdag.common.log.LogFactory;
import org.jdag.communicator.Communicator;
import org.jdag.communicator.HostID;
import org.jdag.communicator.Message;
import org.jdag.communicator.MessageMarshaller;
import org.jdag.communicator.Reactor;

/**
 * A simple implementation of communicator using jboss HornetQ messaging
 * server. The idea is each node has a message queue. The queue is made
 * persistent. This way the messages sent to an application is persisted in the
 * message quque's journal and when started again it can process the messages
 * from the quque. This is our first level of defense against failover.
 *
 * I am contemplating about replacing the hornetq with our own custom
 * protocol implementation using jboss netty. But that can wait for sometime.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public final class CommunicatorImpl implements Communicator
{
    private static final String PRODUCER_TF_NAME = "HQMessageProducer";

    private static final String CONSUMER_TF_NAME = "HQMessageConsumer";

    /** The logger */
    private static final Logger LOG =
        LogFactory.getLogger(CommunicatorImpl.class);

    /** The connection factory to be used for the creating a jms connection */
    private final ClientSessionFactory myClientSessionFactory;

    /**
     * The session to be used by message producer for sending the messages
     */
    private final ClientSession mySession;

    /** The producer is configured with null destination */
    private final ClientProducer myMessageProducer;

    /** The consumer to be used for receiving the messages */
    private final ClientConsumer myMessageConsumer;

    /**
     * The thread pool to be used for sending messages through producer.
     * Since a session is not thread safe, we always use a SingleThreaded
     * Executor.
     */
    private final ExecutorService myProducerExecutor;

    /**
     * The thread pool to be used for processing the received messages
     * from the quque.
     */
    private final ExecutorService myConsumerExecutor;

    /** Used for converting the messages to binary format */
    private final MessageMarshaller myMarshaller;

    /** The configuration information */
    private final CommunicatorConfig myConfig;

    /** Maps a Reactor to its MessageType */
    private final Multimap<Class<? extends Message>, Pair<Reactor, Executor>>
        myMessageToReactorMap;

    /**
     * Wraps a <code>Reactor</code> so that it can be scheduled on a
     * Executor.
     */
    private static class ExecutableReactor implements Runnable
    {
        private final Reactor myReactor;

        private final Message myMessage;

        /**
         * CTOR
         */
        public ExecutableReactor(Message message, Reactor reactor)
        {
            myMessage = message;
            myReactor = reactor;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run()
        {
            myReactor.process(myMessage);
        }
    }

    /**
     * Takes care of performing send message task on a single threaded
     * executor.
     */
    private class SendMessageTask implements Runnable
    {
        private final HostID myHostID;

        private final byte[] myMessageBody;

        /**
         * CTOR
         */
        public SendMessageTask(HostID hostID, Message message)
        {
            myHostID = hostID;
            try {
                myMessageBody = myMarshaller.marshal(message);
            }
            catch (IOException e) {
                 LOG.log(Level.SEVERE, " Exception while serializing a msg", e);
                 throw new RuntimeException(e);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run()
        {
            ClientMessage message = null;
            synchronized(mySession) {
                message =
                    mySession.createMessage(ClientMessage.BYTES_TYPE, true);
            }
            message.setAddress(new SimpleString(myHostID.getIdentifier()));
            message.getBodyBuffer().writeBytes(myMessageBody,
                                               0,
                                               myMessageBody.length);
            try {
                LOG.info("Sending message to " + myHostID);
                myMessageProducer.send(message.getAddress(), message);
            }
            catch (HornetQException e) {
                LOG.log(Level.SEVERE, "Unable to send the message", e);
            }
        }
    }

    /**
     * Implements MessageListener for processing the messages received
     * through jms quque.
     */
    private class CommMessageListener implements MessageHandler
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onMessage(ClientMessage message)
        {
            if (message.getType() != ClientMessage.BYTES_TYPE) {
                LOG.info("Received a message from " + message.getAddress()
                         + " of type " + message.getType());
                return;
            }
            byte[] body = new byte[message.getBodyBuffer().readableBytes()];
            message.getBodyBuffer().readBytes(body);
            Message m;
            try {
                m = myMarshaller.unmarshal(body);
            }
            catch (IOException e) {
                LOG.log(Level.SEVERE, " Exception while deserializing a msg", e);
                 throw new RuntimeException(e);
            }
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
                        myConsumerExecutor.execute(reactor);
                    }
                }
            }
        }
    }

    /**
     * CTOR
     */
    @Inject
    public CommunicatorImpl(MessageMarshaller messageMarshaller,
                            CommunicatorConfig config,
                            TransportConfiguration transportConfiguration)
    {
        myConfig = config;
        myMarshaller = messageMarshaller;
        myMessageToReactorMap =
            HashMultimap.<Class<? extends Message>,
                          Pair<Reactor, Executor>>create();
        myClientSessionFactory =
            HornetQClient.createClientSessionFactory(transportConfiguration);
        myClientSessionFactory.setConsumerWindowSize(0);
        try {
            mySession = myClientSessionFactory.createSession();
            myMessageProducer = mySession.createProducer();
            myProducerExecutor =
                Executors.newSingleThreadExecutor(
                    NamedThreadFactory.newNamedFactory(PRODUCER_TF_NAME));

            // Creates a quque for receiving messages sent to the address
            // corresponding to the given client name. The quque is also
            // named as equivalent to client id.
            ClientSession.QueueQuery ququeQuery =
                mySession.queueQuery(new SimpleString(myConfig.getClientName()));
            if (!ququeQuery.isExists()) {
                mySession.createQueue(myConfig.getClientName(),
                                      myConfig.getClientName(),
                                      true);
            }
            else {
                LOG.info("Quque with id " + myConfig.getClientName()
                         + " already exists");
            }
            myMessageConsumer =
                mySession.createConsumer(myConfig.getClientName());
            myMessageConsumer.setMessageHandler(new CommMessageListener());
            myConsumerExecutor =
                Executors.newSingleThreadExecutor(
                    NamedThreadFactory.newNamedFactory(CONSUMER_TF_NAME));
        }
        catch (HornetQException e) {
            LOG.log(Level.INFO,
                    "Exception while initializing the communicator",
                    e);
            throw new RuntimeException(e);
        }
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
        return new HostID(myConfig.getClientName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(HostID host, Message m)
    {
        LOG.info("Sending " + m + " -> " + host);
        myProducerExecutor.execute(new SendMessageTask(host, m));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start()
    {
        try {
            mySession.start();
        }
        catch (HornetQException e) {
            LOG.log(Level.SEVERE, "Unable to start a session", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop()
    {
        LOG.info("Stopping the communicator");
        try {
            myMessageConsumer.close();
            myMessageProducer.close();
            mySession.deleteQueue(myConfig.getClientName());
            mySession.stop();
            myProducerExecutor.shutdownNow();
            myConsumerExecutor.shutdownNow();
        }
        catch (HornetQException e) {
            LOG.log(Level.SEVERE, "Unable to shutdown hornetq session", e);
            throw new RuntimeException(e);
        }
    }
}
