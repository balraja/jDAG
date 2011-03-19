package org.jdag.node;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.jdag.common.Application;
import org.jdag.common.ApplicationExecutor;
import org.jdag.common.log.LogFactory;
import org.jdag.communicator.Communicator;
import org.jdag.communicator.HostID;
import org.jdag.communicator.Message;
import org.jdag.communicator.Reactor;
import org.jdag.communicator.messages.ExecuteVertexCommand;
import org.jdag.communicator.messages.ExecuteVertexCommandStatus;
import org.jdag.communicator.messages.Heartbeat;

import org.jdag.graph.ExecutionContext;
import org.jdag.graph.ExecutionResult;
import org.jdag.graph.VertexID;

/**
 * The executor which sits on a node and executes the functions sent by the
 * master.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class NodeExecutor implements Application
{
    /** The logger */
    private static final Logger LOG =  LogFactory.getLogger(NodeExecutor.class);

    private final Communicator myCommunicator;

    private final NodeConfig myConfig;

    private final PaceMaker myPaceMaker;

    private final ExecutorService myExecutorService;

    private final ExecutionContext myExecutionContext;

    private final ScheduledExecutorService myScheduler;

    private final Map<VertexID, Future<ExecutionResult>>
        myFuncToExecutionStatusMap;

    /**
     * Responsible for sending heartbeats to the master.
     */
    private class PaceMaker implements Runnable
    {
        private long myStartTime;

         /**
         * CTOR
         */
        public PaceMaker()
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run()
        {

            Heartbeat heartbeat =
                new Heartbeat(myCommunicator.getMyHostID(),
                              System.currentTimeMillis() - myStartTime);
            myCommunicator.sendMessage(new HostID(myConfig.getMasterHostID()),
                                       heartbeat);
        }

        /**
         * Starts sending heartbeats to the master.
         */
        public void start()
        {
        	myStartTime = System.currentTimeMillis();
        	myScheduler.scheduleAtFixedRate(
        	    PaceMaker.this, 1, 1, TimeUnit.SECONDS);
        }
    }

    /**
     * Responsible for checking the results of execution.
     */
    private class ResultChecker implements Runnable
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void run()
        {
            Map<VertexID, ExecutionResult> doneVertices =
                new HashMap<VertexID, ExecutionResult>();
            for (Map.Entry<VertexID, Future<ExecutionResult>> entry :
                    myFuncToExecutionStatusMap.entrySet())
            {
                Future<ExecutionResult> future = entry.getValue();
                ExecutionResult result = null;
                if (future.isDone()) {
                    try {
                        result = future.get();
                    }
                    catch (InterruptedException e) {
                         result = ExecutionResult.ERROR;
                    }
                    catch (ExecutionException e) {
                        result = ExecutionResult.ERROR;
                    }
                    doneVertices.put(entry.getKey(), result);
                }
            }

            for (Map.Entry<VertexID, ExecutionResult> entry
                    : doneVertices.entrySet())
            {
                myFuncToExecutionStatusMap.remove(entry.getKey());
                ExecuteVertexCommandStatus status =
                    new ExecuteVertexCommandStatus(
                            entry.getKey(), entry.getValue());
                myCommunicator.sendMessage(
                    new HostID(myConfig.getMasterHostID()), status);
            }
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
			ExecuteVertexCommand message = (ExecuteVertexCommand) m;
			LOG.info("Received " + message.getVertex().getID()
			         + " for execution");
			ExecutableVertex v =
				new ExecutableVertex(message.getVertex(), myExecutionContext);
			Future<ExecutionResult> result =
			    myExecutorService.submit(v);
			myFuncToExecutionStatusMap.put(v.getID(), result);
		}
    }

    /**
     * CTOR
     */
    @Inject
    public NodeExecutor(Communicator communicator, NodeConfig config)
    {
        myCommunicator = communicator;
        myConfig = config;
        myPaceMaker = new PaceMaker();
        ThreadFactoryBuilder tfBuilder = new ThreadFactoryBuilder();
        ThreadFactory namedFactory =
            tfBuilder.setNameFormat(NodeExecutor.class.getSimpleName())
                     .build();
        myExecutorService = Executors.newFixedThreadPool(2, namedFactory);
        ThreadFactory namedSchedulerFactory =
            tfBuilder.setNameFormat(NodeExecutor.class.getSimpleName() + " sch")
                     .build();
        myScheduler = Executors.newScheduledThreadPool(2, namedSchedulerFactory);
        myExecutionContext = new SimpleExecutionContext();
        myFuncToExecutionStatusMap =
        	new HashMap<VertexID, Future<ExecutionResult>>();
    }

    /**
     * Starts the executor responsible for executing the functions sent in by
     * the master.
     */
    public void start()
    {
        LOG.info("Starting Worker " + myCommunicator.getMyHostID());
        myCommunicator.start();
    	myPaceMaker.start();
    	myScheduler.scheduleWithFixedDelay(
           new ResultChecker(), 1, 1, TimeUnit.SECONDS);
    	myCommunicator.attachReactor(
    	    ExecuteVertexCommand.class, new ExecuteVertexReactor());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop()
    {
        LOG.info("Stopping Worker " + myCommunicator.getMyHostID());
        myScheduler.shutdownNow();
        myCommunicator.stop();
    }

    /**
     * The main method.
     */
    public static void main(String[] args)
    {
        Injector injector = Guice.createInjector(new NodeModule());
        NodeExecutor exec = injector.getInstance(NodeExecutor.class);
        ApplicationExecutor applicationExecutor =
            new ApplicationExecutor(exec);
        applicationExecutor.run();
    }
}
