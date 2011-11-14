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

import org.jdag.dsl.Input;
import org.jdag.dsl.Output;

/**
 * Type for denoting a factory that can be used for making inputs and outputs
 * corresponding for a function.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface IOFactory
{
    /** A factory method for making input corresponding to a task */
    public <T> Input<T> makeInput(IOKey key);

    /** A factory method for making output corresponding to a task */
    public <T> Output<T> makeOutput(IOKey key);
}
