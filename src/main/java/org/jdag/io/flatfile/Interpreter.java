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

package org.jdag.io.flatfile;

/**
 * A typical interface to be used for reading records from a flat file
 * where the record data is stored in a line.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Interpreter<T>
{
    public static String ATTRIBUTE_NAME = "interpreter";

    /** Reads data from the given line and generates a record out of it */
    public T makeRecord(String line);

    /** Flattens the given record to a line in the file */
    public String flattenRecord(T r);

}
