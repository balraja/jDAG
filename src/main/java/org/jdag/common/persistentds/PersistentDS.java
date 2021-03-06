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

package org.jdag.common.persistentds;

/**
 * All the data structures, where every intermediate change to be persisted
 * has to implement this interface.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface PersistentDS
{
    /** Returns the identifier of the persistent data structure */
    public String id();

    /** Returns the snapshot that has to be persisted */
    public Snapshot makeSnapshot();

    /** Should implement this method for initializing from the snapshot */
    public void initFromSnapshot(Snapshot snapshot);
}
