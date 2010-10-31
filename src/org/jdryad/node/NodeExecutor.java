package org.jdryad.node;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.jdryad.com.Communicator;
import org.jdryad.com.HostID;
import org.jdryad.com.Message;
import org.jdryad.com.Reactor;
import org.jdryad.com.messages.JDAGMessageType;
import org.jdryad.com.messages.SimpleMessage;
import org.jdryad.com.messages.UpAndLiveProtos;
import org.jdryad.com.messages.ExecuteVertexProtos.ExecuteVertexMessage;
import org.jdryad.dag.ExecutionContext;
import org.jdryad.dag.ExecutionResult;
import org.jdryad.dag.VertexID;

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
    
    private final PaceMaker myPaceMaker;
    
    private final ExecutorService myExecutorService;
    
    private final ExecutionContext myExecutionContext;
    
    private final Map<VertexID, Future<ExecutionResult>> myFunToExecutionStatusMap;
    
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
        public PaceMaker()
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
        
        /**
         * Starts sending heartbeats to the master.
         */
        public void start()
        {
        	myStartTime = System.currentTimeMillis();
        	myScheduler.scheduleAtFixedRate(PaceMaker.this, 10, 10, TimeUnit.SECONDS);
        }
    }
    
    /** Reactor for <node>ExecuteVertex</code> messages */
    private class ExecuteVertexReactor implements Reactor
    {
    	/**
    	 * {@inheritDoc}
    	 */
		@Override
		public void process(Message m) 
		{
			ExecuteVertexMessage message = 
				(ExecuteVertexMessage)
				    ((SimpleMessage) m).getPayload();
			ExecutableVertex v = 
				new ExecutableVertex(message, myExecutionContext);
			Future<ExecutionResult> result = 
			    myExecutorService.submit(v);
			myFunToExecutionStatusMap.put(v.getID(), result);
		}
    }

    /**
     * CTOR
     */
    public NodeExecutor(Communicator communicator, NodeConfig config)
    {
        myCommunicator = communicator;
        myConfig = config;
        myPaceMaker = new PaceMaker();
        myExecutorService = Executors.newFixedThreadPool(2);
        myExecutionContext = new SimpleExecutionContext();
        myFunToExecutionStatusMap =
        	new HashMap<VertexID, Future<ExecutionResult>>();
    }
    
    /**
     * Starts the executor responsible for executing the functions sent in by 
     * the master.
     */
    public void start()
    {
    	myPaceMaker.start();
    	myCommunicator.attachReactor(JDAGMessageType.EXECUTE_VERTEX_MESSAGE, 
    			                     new ExecuteVertexReactor());
    			
    }
}
