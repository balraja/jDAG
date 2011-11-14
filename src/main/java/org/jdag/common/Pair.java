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

package org.jdag.common;

/**
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public class Pair<T, V>
{
    private final T myFirst;

    private final V mySecond;

    /**
     * CTOR
     */
    public Pair(T first, V second)
    {
        super();
        myFirst = first;
        mySecond = second;
    }

    /**
     * Returns the value of first
     */
    public T getFirst()
    {
        return myFirst;
    }

    /**
     * Returns the value of second
     */
    public V getSecond()
    {
        return mySecond;
    }

}
