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

/**
 * Defines the function that can be used for merging data from different
 * Shards back into a <code>DataCollection</code>.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Merger<T> extends Executable
{
    /**
     * Merges the data from different shards into a single collection back.
     */
    public void merge(List<Input<T>> shards, Output<T> output);
}
