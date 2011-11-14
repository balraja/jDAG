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

package org.jdag.graph.scheduler;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import java.util.Set;

import org.jdag.communicator.HostID;
import org.jdag.graph.VertexID;
import org.jdag.master.ExecutionRegistry;

/**
 * A simple scheduling policy that says at a given point of time only one
 * vertex from a graph can be alive in a node. Also it also restricts the
 * number of active vertices that can be executed in a node.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class BoundedWorkerSchedulingPolicy implements WorkerSchedulingPolicy
{
    /**
     * {@inheritDoc}
     */
    @Override
    public HostID getWorkerNode(VertexID graphVertexID,
                                ExecutionRegistry stateRegistry)
    {
        Set<HostID> workers = stateRegistry.getWorkers();
        if (workers.isEmpty()) {
            return null;
        }
        Set<HostID> currentWorkersForGraph =
            stateRegistry.getWorkersForGraph(graphVertexID.getGraphID());

        if (currentWorkersForGraph.isEmpty()) {
            return workers.iterator().next();
        }
        else {
            SetView<HostID> availableWorkers =
                Sets.difference(workers, currentWorkersForGraph);
            if (availableWorkers.isEmpty()) {
                return null;
            }
            else {
                return availableWorkers.iterator().next();
            }
        }
    }
}
