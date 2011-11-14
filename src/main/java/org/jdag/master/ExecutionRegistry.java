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

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.jdag.common.log.LogFactory;
import org.jdag.common.persistentds.Persist;
import org.jdag.common.persistentds.PersistentDS;
import org.jdag.common.persistentds.Snapshot;
import org.jdag.communicator.HostID;
import org.jdag.graph.GraphID;
import org.jdag.graph.Vertex;
import org.jdag.graph.VertexID;
import org.jdag.graph.scheduler.VertexSchedule;

/**
 * The registry to be used for maintaining the state of the executions.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ExecutionRegistry
{
    /** The logger */
    private static final Logger LOG =  LogFactory.getLogger(ExecutionRegistry.class);

    private final Set<HostID> myWorkerNodes;

    private final Map<VertexID, HostID> myVertex2HostMap;

    private final Map<GraphID, VertexSchedule> myGraph2ScheduleMap;

    /**
     * CTOR
     */
    public ExecutionRegistry()
    {
        myWorkerNodes = new HashSet<HostID>();
        myVertex2HostMap = new HashMap<VertexID, HostID>();
        myGraph2ScheduleMap = new HashMap<GraphID, VertexSchedule>();
    }

    /** Adds a worker */
    public synchronized void addWorker(HostID workerID)
    {
        LOG.info("Adding worker " + workerID);
        myWorkerNodes.add(workerID);
    }

    /** Returns true if the worker has already been added to the registry */
    public synchronized boolean hasWorker(HostID workerID)
    {
        return myWorkerNodes.contains(workerID);
    }

    /** Returns the list of worker nodes available in the registry */
    public synchronized Set<HostID> getWorkers()
    {
        return Sets.newHashSet(myWorkerNodes);
    }

    /** Removes the worker */
    public synchronized void removeWorker(HostID worker)
    {
        myWorkerNodes.remove(worker);
    }

    /** Replaces the existing worker with the new worker */
    public synchronized void replaceWoker(HostID existing, HostID newWorker)
    {
        myWorkerNodes.remove(existing);
        myWorkerNodes.add(newWorker);
    }

    /**
     * Updates the mapping between vertex to the host that executes the
     * vertex.
     */
    public synchronized void updateVertex2HostMapping(
        VertexID graphVertexID, HostID hostID)
    {
        myVertex2HostMap.put(graphVertexID, hostID);
    }

    /** Returns the <code>HostID</code> where the graph is executed now */
    public synchronized Set<HostID> getWorkersForGraph(GraphID graphID)
    {
        HashSet<HostID> hosts = new HashSet<HostID>();
        for (Map.Entry<VertexID, HostID> entry :
                myVertex2HostMap.entrySet())
        {
            if (entry.getKey().getGraphID().equals(graphID)) {
                hosts.add(entry.getValue());
            }
        }
        return hosts;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void removeVertexToHostMapping(VertexID graphVertexID)
    {
         myVertex2HostMap.remove(graphVertexID);
    }

    /** Updates the mapping between graph and its schedule */
    public synchronized void addSchedule(GraphID graphID, VertexSchedule schedule)
    {
        myGraph2ScheduleMap.put(graphID, schedule);
    }

    /**
     * Removes the vertex to the worker mapping and updates the schedule for the
     * given graph that the vertex is done.
     *
     * @return True if the execution of the graph is done, false otherwise.
     */
    public synchronized boolean markDone(VertexID graphVertexID)
    {
        LOG.info("The execution of vertex " + graphVertexID + "has completed succesfully");
        myVertex2HostMap.remove(graphVertexID);
        VertexSchedule schedule = myGraph2ScheduleMap.get(graphVertexID.getGraphID());
        schedule.notifyDone(graphVertexID);
        if (schedule.isCompleted()) {
            myGraph2ScheduleMap.remove(graphVertexID.getGraphID());
            return true;
        }
        else {
            return false;
        }
    }

    /** Returns a vertex for execution */
    public synchronized Vertex getVertexForExecution(GraphID graphID)
    {
        VertexSchedule schedule = myGraph2ScheduleMap.get(graphID);
        Preconditions.checkNotNull(schedule);
        return schedule.getVertexForExecution();
    }
}
