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

package org.jdag.dsl.impl;

/**
 * A checked exception thrown in case of computational failures.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ComputeFailedException extends Exception
{

    public ComputeFailedException()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public ComputeFailedException(String message, Throwable cause)
    {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public ComputeFailedException(String message)
    {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public ComputeFailedException(Throwable cause)
    {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
