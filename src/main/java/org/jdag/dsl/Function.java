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

import java.io.Serializable;

/**
 * Type that defines a function that gets shipped to a remote execution engine.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Function<I,O>
    extends Serializable, Executable
{
    /**
     * The definition of the function that needs to be executed on a vertex.
     */
    public void process(Input<I> input, Output<O> output);
}
