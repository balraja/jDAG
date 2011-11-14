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
 * A splitter is used for splitting a <code>DataCollection</code> into multiple
 * partitions so that they can be processed in parallel.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Splitter<T> extends Executable
{
    /**
     * Returns the number of partitions.
     */
    public int numPartitions();

    /**
     * Splits the data into multiple partitions.
     */
    public void split(Input<T>        input,
                      List<Output<T>> outputs);
}
