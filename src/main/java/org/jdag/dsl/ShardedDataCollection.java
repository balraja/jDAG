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

/**
 * Defines the state where data is split into multiple partitions primarily to
 * enable processing of data in parallel.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface ShardedDataCollection<T>
{
    /**
     * The suffix to be appended to the file name in which data is stored.
     */
    public static final String FILE_SUFFIX = "_shard";

    /**
     * Merges the data from different shards back into a data collection.
     */
    public DataCollection<T> merge(Merger<T> merger);

    /**
     * Applies the function directly on the data collection.
     */
    public <V> ShardedDataCollection<V> apply(Function<T, V> function);
}
