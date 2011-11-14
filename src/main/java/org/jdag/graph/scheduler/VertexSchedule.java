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

import java.io.Externalizable;

import org.jdag.graph.Vertex;
import org.jdag.graph.VertexID;

/**
 * Defines the contract for the scheduler.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface VertexSchedule extends Externalizable
{
    /**
     * When called returns the next vertex to be executed. If all the
     * vertices are waiting for some previously scheduled vertices
     * then it will return null.
     */
    public Vertex getVertexForExecution();

    /**
     * Notifies that the given vertex id has been successfully executed.
     * Based on these notifications that the scheduler will give appropriate
     * vertices when {@link VertexSchedule#getVertexForExecution()} is called
     * next.
     */
    public void notifyDone(VertexID vertexID);

    /** Returns true if the complete graph has been scheduled */
    public boolean isCompleted();
}