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

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ThreadFactory;

/**
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public final class NamedThreadFactory
{
    /** Private CTOR */
    private NamedThreadFactory()
    {
    }

    /**
     * Returns a <code>ThreadFactory that creates threads with the simple
     * name of the class.
     */
    public static ThreadFactory newNamedFactory(
        Class<?> hostClass)
    {
        ThreadFactoryBuilder tfBuilder = new ThreadFactoryBuilder();
        ThreadFactory namedFactory =
            tfBuilder.setNameFormat(hostClass.getSimpleName())
                     .build();
        return namedFactory;
    }

    /**
     * Returns a <code>ThreadFactory that creates threads with the given simple
     * name.
     */
    public static ThreadFactory newNamedFactory(
        String simpleName)
    {
        ThreadFactoryBuilder tfBuilder = new ThreadFactoryBuilder();
        ThreadFactory namedFactory =
            tfBuilder.setNameFormat(simpleName)
                     .build();
        return namedFactory;
    }
}
