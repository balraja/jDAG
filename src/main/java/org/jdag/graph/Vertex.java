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

package org.jdag.graph;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.List;

import org.jdag.io.IOKey;

/**
 * Defines the contract for a vertex that can be executed.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Vertex extends Externalizable
{
    /**
     * Returns <code>VertexID</code> of vertex.
     */
    public VertexID getID();

    /**
     * Returns the value of inputs
     */
    public List<IOKey> getInputs();

    /**
     * Returns the value of outputs
     */
    public List<IOKey> getOutputs();

    /**
     * Returns the unique identifier corresponding to a function that will be
     * executed as part of this vertex.
     */
    public String getUDFIdentifier();
}