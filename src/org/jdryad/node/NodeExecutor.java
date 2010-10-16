package org.jdryad.node;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import org.jdryad.com.Communicator;
import org.jdryad.com.HostID;
import org.jdryad.com.messages.JDAGMessageType;
import org.jdryad.com.messages.SimpleMessage;
import org.jdryad.com.messages.UpAndLiveProtos;

/**
 * The executor which sits on a node and executes the functions sent by the
 * master.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class NodeExecutor
{
    private final Communicator myCommunicator;

    private final NodeConfig myConfig;

    /**
     * Responsible for sending heartbeats to the master.
     */
    private class PaceMaker implements Runnable
    {
         private final ScheduledExecutorService myScheduler;

         private long myStartTime;


         /**
         * CTOR
         */
        public PaceMaker(Communicator communicator)
        {
            ThreadFactoryBuilder tfBuilder = new ThreadFactoryBuilder();
            ThreadFactory namedFactory =
                tfBuilder.setNameFormat(NodeExecutor.class.getSimpleName())
                         .build();
            myScheduler =
                Executors.newSingleThreadScheduledExecutor(namedFactory);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run()
        {
            UpAndLiveProtos.UpAndAliveMessage payload =
                UpAndLiveProtos.UpAndAliveMessage
                               .newBuilder()
                               .setAliveMillis(System.currentTimeMillis() -
                                                  myStartTime)
                               .build();
            myCommunicator.sendMessage(new HostID(myConfig.getMasterHostID()),
                                       new SimpleMessage(
                                           JDAGMessageType.UP_AND_ALIVE_MESSAGE,
                                           payload));
        }
    }

    /**
     * CTOR
     */
    public NodeExecutor(Communicator communicator, NodeConfig config)
    {
        myCommunicator = communicator;
        myConfig = config;
    }

}
