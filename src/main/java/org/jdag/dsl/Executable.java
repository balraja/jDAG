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

package org.jdag.dsl;

import java.util.List;

import org.jdag.dsl.impl.ComputeFailedException;
import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOKey;

/**
 * Defines the contract for method that needs to be called in a vertex.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Executable
{
    public void execute(ExecutionContext context,
                        List<IOKey> inputKeys,
                        List<IOKey> outputKeys)
        throws ComputeFailedException;
}
