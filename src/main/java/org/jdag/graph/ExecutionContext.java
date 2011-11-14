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

import org.jdag.io.IOFactory;
import org.jdag.io.IOSource;

/**
 * A simple class that encapsulates the <code>TaskGraphContext</code> for a
 * graph.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface ExecutionContext
{
    /**
     * Provides access to IOFactory that in turn be used for taskInputs and
     * task outputs.
     */
    public IOFactory makeIOFactory(IOSource source);
}
