package org.jdryad.com.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.jdryad.com.Communicator;
import org.jdryad.com.HostID;
import org.jdryad.com.MessageMarshallerFasctory;
import org.jdryad.com.Reactor;
import org.jdryad.common.NamedThreadFactory;

/**
 * </p>
 * An implementation of <code>Communicator</code> that internally uses RabbitMQ
 * for implementing the communication protocol.
 *
 * A basic documentation for active mq can be found in
 * <a href="http://www.rabbitmq.com/api-guide.html">APIGuide</a> and
 * <a href="http://www.infoq.com/articles/AMQP-RabbitMQ">InfoQ</code>
 *
 * </p>
 * The basic protocol is as follows.
 * <ul>
 * <li>1. We create a default exchange by name <b>jDAG</b>
 * <li>2. Every host will create a quque  and attach to the jDAG exchange with
 *        the binding key set to the host name
 * <li>3. If we want to send data to that host we will just publish to the
 *       exchange jDAG with the routing key set to host name.
 * </ul>
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class RabbitMQCommunicator implements Communicator
{
    private static final String CONST_FIXED_EXCHANGE_TYPE = "fixed";

    private final RabbitMQConfiguration myCOnfiguration;

    private final LockableChannel myChannel;

    private final MessageMarshallerFasctory myMarshallerFasctory;

    private final ExecutorService myCommExecutor;

    private final Connection myActiveConnection;

    /**
     * An implementation of <code>Consumer</code> that invokes appropriate
     * <code>Reactor</code> based on the incoming message type
     */
    private static class ReactorConsumer extends DefaultConsumer
    {
        /**
         * CTOR
         */
        public ReactorConsumer(Channel channel)
        {
            super(channel);
        }
    }

    /**
     * A simple type that ensures that only a single thread access
     * the channel at a single time.
     */
    private static class LockableChannel
    {
        private final Semaphore myLock;
        private final Channel myChannel;

        /**
         * CTOR
         */
        public LockableChannel(Channel channel)
        {
            myChannel = channel;
            myLock = new Semaphore(1);
        }

        /**
         * Returns the value of channel
         */
        public Channel getChannel()
        {
            return myChannel;
        }

        /** Locks the channel for use */
        public void lock() throws InterruptedException
        {
            myLock.acquire();
        }

        /** UnLocks the channel after use */
        public void unlock() throws InterruptedException
        {
            myLock.release();
        }
    }

    /**
     * CTOR
     */
    public RabbitMQCommunicator(RabbitMQConfiguration config,
                                MessageMarshallerFasctory mmFactory)
    {
        myCOnfiguration = config;
        myMarshallerFasctory = mmFactory;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(config.getHost());
        factory.setPort(config.getPort());
        factory.setUsername(config.getUserName());
        factory.setPassword(config.getPassword());
        factory.setVirtualHost(config.getVirtualHost());
        myCommExecutor =
            Executors.newSingleThreadExecutor(
                new NamedThreadFactory(RabbitMQCommunicator.class));

        try {
            myActiveConnection = factory.newConnection();
            Channel c = myActiveConnection.createChannel();
            myChannel = new LockableChannel(c);

            myChannel.lock();
            if (config.shouldDeclareGlobalExchange()) {
                // The global exchange should be durable.
                myChannel.getChannel().exchangeDeclare(
                    config.getGlobalExchange(),
                    CONST_FIXED_EXCHANGE_TYPE,
                    true);
            }

            myChannel.getChannel().queueDeclare(
                config.getHostName(),
                true,
                true,
                false,
                null);

            // Now attach the new quuqe with the default exchange and the
            // routing key is same as the quque name.
            myChannel.getChannel().queueBind(
                config.getHostName(),
                config.getGlobalExchange(),
                config.getHostName());

            myChannel.getChannel().basicConsume(config.getHostName(),
                                                new ReactorConsumer(
                                                    myChannel.getChannel()));
            myChannel.unlock();

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
             throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
             throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachReactor(int type, Reactor r)
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachReactor(int type, Reactor r, Executor executor)
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(HostID host)
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(String groupName)
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attachTOGroup(String groupName)
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createGroup(String groupName)
    {
        // TODO Auto-generated method stub

    }

}
