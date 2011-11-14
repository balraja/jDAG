/*******************************************************************************
 * jDAG is a project to build acyclic dataflow graphs for processing massive datasets.
 *
 *     Copyright (C) 2011, Author: Balraja,Subbiah
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package org.jdag.master;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.jdag.common.Application;
import org.jdag.common.ApplicationExecutor;
import org.jdag.common.log.LogFactory;
import org.jdag.common.persistentds.PersistentDSManagerAccessor;
import org.jdag.communicator.Communicator;
import org.jdag.communicator.HostID;
import org.jdag.communicator.Message;
import org.jdag.communicator.Reactor;
import org.jdag.communicator.messages.ExecuteVertexCommand;
import org.jdag.communicator.messages.ExecuteVertexCommandStatus;
import org.jdag.communicator.messages.Heartbeat;
import org.jdag.example.wc.WordCount;

import org.jdag.graph.Graph;
import org.jdag.graph.GraphID;
import org.jdag.graph.ExecutionResult;
import org.jdag.graph.Vertex;
import org.jdag.graph.scheduler.VertexSchedule;
import org.jdag.graph.scheduler.TopologicalSortSchedule;
import org.jdag.graph.scheduler.WorkerSchedulingPolicy;
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

    private final ExecutionRegistry myStateRegistry;

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
            Heartbeat hearbeat = (Heartbeat) m;
            HostID fromHost = hearbeat.getNodeID();
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
            ExecuteVertexCommandStatus status =
                (ExecuteVertexCommandStatus) m;
            ExecutionResult result = status.getResult();

            if (result == ExecutionResult.SUCCESS) {
                boolean isGraphCompleted =
                    myStateRegistry.markDone(status.getExecutedVertex());
                if (!isGraphCompleted) {
                    myScheduler.schedule(
                        new VertexSchedulingTask(
                                status.getExecutedVertex().getGraphID()),
                        10,
                        TimeUnit.MICROSECONDS);
                }
                else {
                    LOG.info("The execution of " +
                            status.getExecutedVertex().getGraphID()
                            + " is completed ");
                }
            }
            else {
                LOG.info("The execution of " + status.getExecutedVertex() + " failed");
            }
        }
    }

    /**
     * A simple function that schedules the next vertex from the graph on
     * a worker node.
     */
    private class VertexSchedulingTask implements Runnable
    {
        /**
         * THe graph whose scheduling is managed by this task.
         */
        private final GraphID myGraphID;

        /**
         * THe vertex to be scheduled by this task.
         */
        private Vertex myToBeScheduledVertex;

        /**
         * CTOR
         */
        public VertexSchedulingTask(GraphID graphID)
        {
            this(graphID, null);
        }

        /**
         * CTOR
         */
        public VertexSchedulingTask(GraphID graphID, Vertex vertex)
        {
            myGraphID = graphID;
            myToBeScheduledVertex = vertex;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run()
        {
            LOG.info("Executing vertex scheduling task");
            if (myToBeScheduledVertex == null) {
                myToBeScheduledVertex =
                    myStateRegistry.getVertexForExecution(myGraphID);
            }
            LOG.info("Trying to schedule " + myToBeScheduledVertex);
            if (myToBeScheduledVertex != null) {
                HostID hostID =
                    myWorkerSchedulingPolicy.getWorkerNode(
                        myToBeScheduledVertex.getID(), myStateRegistry);
                if (hostID != null) {
                    myStateRegistry.updateVertex2HostMapping(
                            myToBeScheduledVertex.getID(), hostID);
                    ExecuteVertexCommand command = new ExecuteVertexCommand(
                            myToBeScheduledVertex);
                    myCommunicator.sendMessage(hostID, command);
                }
                else {
                    LOG.info("No hosts are available rescheduling");
                    myScheduler.schedule(
                        new VertexSchedulingTask(myGraphID,
                                                 myToBeScheduledVertex),
                        10,
                        TimeUnit.SECONDS);
                }
            }
        }
    }

    /**
     * CTOR
     */
    @Inject
    public MasterExecutor(WorkerSchedulingPolicy schedulingPolicy,
                          Communicator communicator,
                          ExecutionRegistry registry)
    {
        myWorkerSchedulingPolicy = schedulingPolicy;
        myStateRegistry = registry;
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
        WordCount count = new WordCount("C:\\temp\\wcjournal.txt");
        Graph graph = count.getGraph();
        execute(graph);
        LOG.info("STarting communicator");
        myCommunicator.attachReactor(Heartbeat.class,
                                     new UpAndALiveReactor());
        myCommunicator.attachReactor(ExecuteVertexCommandStatus.class,
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

    /**
     * Executes the graph over the worker vertices.
     */
    public void execute(Graph graph)
    {
        LOG.info("Received graph with id " + graph.getID() + " for execution");
        VertexSchedule schedule = new TopologicalSortSchedule(graph);
        myStateRegistry.addSchedule(graph.getID(), schedule);
        myScheduler.schedule(
            new VertexSchedulingTask(graph.getID()),
            5,
            TimeUnit.SECONDS);
         LOG.info("Added schedule for  " + graph.getID()
                  + " for execution");
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
