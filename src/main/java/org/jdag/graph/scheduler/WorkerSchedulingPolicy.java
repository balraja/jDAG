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

import org.jdag.communicator.HostID;
import org.jdag.graph.VertexID;
import org.jdag.master.ExecutionRegistry;

/**
 * The type that defines the contract for scheduling vertices over the
 * worker nodes. This is the type that's responsible for selecting a node
 * to which a graph vertex has to be shipped for execution.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface WorkerSchedulingPolicy
{
    /**
     * Returns the identifier for a worker node to which graph vertices
     * can be scheduled or null if a worker is not free.
     */
    public HostID getWorkerNode(VertexID          graphVertexID,
                                ExecutionRegistry executionStateRegistry);
}
