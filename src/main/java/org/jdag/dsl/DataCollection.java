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

import org.jdag.function.Dumper;

/**
 * Type to define the collection of data records to be processed.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface DataCollection<T>
{
    /**
     * The sufiix to be appended to the file name in which data is stored.
     */
    public static final String FILE_SUFFIX = "_data";

    /**
     * Applies the function directly on the data collection.
     */
    public <V> DataCollection<V> apply(Function<T, V> function);

    /**
     * Creates a node in the execution graph that can be used for splitting
     * the data into multiple partitions.
     */
    public ShardedDataCollection<T> partition(Splitter<T> splitter);

    /**
     * Dump the contents of collection to the file.
     */
    public void writeOutput(String fileName, Dumper<T> dumper);
}
