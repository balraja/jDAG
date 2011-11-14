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

package org.jdag.io;

import org.jdag.graph.GraphID;

/**
 * Mainly used for generating IOKey based on the persistence medium on which
 * intermediate data is stored.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface KeyGenerator
{
    /**
     * Returns an unique identifier that can be used for directly accessing the 
     * data corresponding to the local identifier in the file system.
     */
    public IOKey generateIdentifier(GraphID graphID, String localIdentifier);

    /**
     * Returns an unique identifier that can be used for directly accessing the 
     * data corresponding to the local identifier in the file system.
     */
    public IOKey generateFlatFileIdentifier(
        GraphID graphID, String localIdentifier, String interpreterClassName);
}
