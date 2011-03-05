package org.jdag.master;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.jdag.commmunicator.Communicator;
import org.jdag.commmunicator.HostID;
import org.jdag.commmunicator.Message;
import org.jdag.commmunicator.Reactor;
import org.jdag.common.Application;
import org.jdag.common.ApplicationExecutor;
import org.jdag.common.log.LogFactory;
import org.jdag.common.persistentds.PersistentDSManagerAccessor;
import org.jdag.communicator.messages.ExecuteVertexProtos;
import org.jdag.communicator.messages.JDAGMessageType;
import org.jdag.communicator.messages.SimpleMessage;
import org.jdag.communicator.messages.UpAndLiveProtos;
import org.jdag.communicator.messages.ExecuteVertexProtos.ExecuteVertexMessage;
import org.jdag.communicator.messages.ExecuteVertexStatusProtos.ExecuteVertexStatusMessage;
import org.jdag.communicator.messages.UpAndLiveProtos.UpAndAliveMessage;
import org.jdag.graph.Graph;
import org.jdag.graph.GraphID;
import org.jdag.graph.ExecutionResult;
import org.jdag.graph.GraphVertexID;
import org.jdag.graph.Vertex;
import org.jdag.graph.VertexID;
import org.jdag.graph.scheduler.Schedule;
import org.jdag.graph.scheduler.TopologicalSortSchedule;
import org.jdag.io.IOKey;
import org.jdag.node.NodeExecutor;

/**
 * The master server. Master takes a graph as input and gets its executed
 * through many of its workers nodes.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class MasterExecutor implements Application
{
    /** The logger */
    private final Logger LOG =  LogFactory.getLogger(MasterExecutor.class);

    private final WorkerSchedulingPolicy myWorkerSchedulingPolicy;

    private final Communicator myCommunicator;

    private final ScheduledExecutorService myScheduler;

    private final ExecutionStateRegistry myStateRegistry;

    /**
     * Updates scheduling policy with the latest information from the scheduling
     * nodes.
     */
    private class UpAndALiveReactor implements Reactor
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void process(Message m)
        {
            SimpleMessage message = (SimpleMessage) m;
            UpAndAliveMessage aliveMessage =
                ((UpAndLiveProtos.UpAndAliveMessage) message.getPayload());
            HostID fromHost = new HostID(aliveMessage.getIdentifier());
            if (!myStateRegistry.hasWorker(fromHost)) {
                myStateRegistry.addWorker(fromHost);
            }
        }
    }

    /**
     * Updates the status of execution of a vertex corresponding to a graph.
     */
    private class ExecuteVertexStatusReactor implements Reactor
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void process(Message m)
        {
            SimpleMessage message = (SimpleMessage) m;
            ExecuteVertexStatusMessage status =
                (ExecuteVertexStatusMessage) message.getPayload();
            ExecutionResult result =
                ExecutionResult.getExecutionResult(
                    status.getExecutionStatus());
            if (result == ExecutionResult.SUCCESS) {
                GraphID graphID =
                    new GraphID(status.getGraphId());
                VertexID vertexID = new VertexID(status.getVertexId());
                GraphVertexID graphVertexID =
                    new GraphVertexID(graphID, vertexID);
                boolean isCompleted =
                    myStateRegistry.markDone(graphVertexID);
                if (!isCompleted) {
                    myScheduler.schedule(
                        new VertexSchedulingTask(graphID),
                        10,
                        TimeUnit.MICROSECONDS);
                }
            }
        }
    }

    /**
     * A simple function that schedules the next vertex from the graph on
     * a worker node.
     */
    private class VertexSchedulingTask implements Runnable
    {
        private final GraphID myGraphID;

        /**
         * CTOR
         */
        public VertexSchedulingTask(GraphID graphID)
        {
            myGraphID = graphID;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run()
        {
            Vertex vertex =
                myStateRegistry.getVertexForExecution(myGraphID);
            HostID hostID =
                myWorkerSchedulingPolicy.getWorkerNode(
                    new GraphVertexID(myGraphID, vertex.getID()),
                    myStateRegistry);
            if (hostID != null) {
                GraphVertexID gvID =
                    new GraphVertexID(myGraphID, vertex.getID());
                myStateRegistry.updateVertex2HostMapping(gvID, hostID);
                ExecuteVertexMessage executeVertexMessage =
                    ExecuteVertexProtos.ExecuteVertexMessage
                                       .newBuilder()
                                       .setUdfIdentifier(vertex.getUDFIdentifier())
                                       .addAllInputs(
                                           (Iterable<? extends org.jdag.communicator.messages.ExecuteVertexProtos.ExecuteVertexMessage.IOKey>)
                                           makeIOKeys(vertex.getInputs()).iterator())
                                       .addAllOutputs(
                                           (Iterable<? extends org.jdag.communicator.messages.ExecuteVertexProtos.ExecuteVertexMessage.IOKey>)
                                           makeIOKeys(vertex.getOutputs()).iterator())
                                       .build();
                SimpleMessage simpleMessage =
                    new SimpleMessage(JDAGMessageType.EXECUTE_VERTEX_MESSAGE,
                                      executeVertexMessage);
                myCommunicator.sendMessage(hostID, simpleMessage);
            }
            else {
                myScheduler.schedule(
                    new VertexSchedulingTask(myGraphID),
                    10,
                    TimeUnit.SECONDS);
            }
        }
    }

    /**
     * CTOR
     */
    @Inject
    public MasterExecutor(WorkerSchedulingPolicy schedulingPolicy,
                         Communicator communicator)
    {
        myWorkerSchedulingPolicy = schedulingPolicy;
        myStateRegistry = new ExecutionStateRegistry();
        myCommunicator = communicator;
        ThreadFactoryBuilder tfBuilder = new ThreadFactoryBuilder();
        ThreadFactory namedFactory =
            tfBuilder.setNameFormat(NodeExecutor.class.getSimpleName())
                     .build();
        myScheduler = Executors.newScheduledThreadPool(1, namedFactory);
    }

    /** Starts the communicator */
    public void start()
    {
        LOG.info("Starting the master");
        myStateRegistry.initFromSnapshot(
           PersistentDSManagerAccessor.getPersistentDSManager()
                                      .getSnapshot(myStateRegistry.ID()));
        myCommunicator.attachReactor(JDAGMessageType.UP_AND_ALIVE_MESSAGE,
                                     new UpAndALiveReactor());
        myCommunicator.attachReactor(JDAGMessageType.EXECUTE_VERTEX_STATUS_MESSAGE,
                                     new ExecuteVertexStatusReactor());
        myCommunicator.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop()
    {
        LOG.info("Stopping the master");
        myCommunicator.stop();
    }

    private List<ExecuteVertexProtos.ExecuteVertexMessage.IOKey> makeIOKeys(
        List<IOKey> ioKeys)
    {
        ArrayList<ExecuteVertexProtos.ExecuteVertexMessage.IOKey> protoIOKeys =
            new ArrayList<ExecuteVertexProtos.ExecuteVertexMessage.IOKey>();
        for (IOKey ioKey : ioKeys) {
            protoIOKeys.add(ExecuteVertexProtos.ExecuteVertexMessage
                                               .IOKey
                                               .newBuilder()
                                               .setTypeId(ioKey.getSourceType()
                                                               .getTypeID())
                                               .setIoIdentifier(
                                                   ioKey.getIdentifier())
                                               .build());
        }
        return protoIOKeys;

    }

    /**
     * Executes the graph over the worker vertices.
     */
    public void execute(Graph graph)
    {
        Schedule schedule = new TopologicalSortSchedule(graph);
        myStateRegistry.addSchedule(graph.getID(), schedule);
        myScheduler.schedule(
            new VertexSchedulingTask(graph.getID()),
            10,
            TimeUnit.SECONDS);
    }

    /** The starting point of the application */
    public static void main(String[] args)
    {
        Injector injector = Guice.createInjector(new MasterExecutorModule());
        MasterExecutor executor =
            injector.getInstance(MasterExecutor.class);
        ApplicationExecutor applicationExecutor =
            new ApplicationExecutor(executor);
        applicationExecutor.run();
    }
}
