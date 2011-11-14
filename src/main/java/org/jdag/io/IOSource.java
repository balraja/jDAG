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

/**
 * An enum to define the medium in which data is stored.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public enum IOSource
{
    /**
     * Defines the case where data is stored in a file system as serialized
     * java objects.
     */
    SERIALIZED_FILE,

    /**
     * Defines the case where data is stored in a file system as raw files.
     */
    FLAT_FILE
}
