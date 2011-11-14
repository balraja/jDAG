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

package org.jdag.io.serializedfile;

import org.jdag.dsl.Input;
import org.jdag.dsl.Output;
import org.jdag.io.IOFactory;
import org.jdag.io.IOKey;

/**
 * A simple class that takes key to the inputs or outputs and generates
 * appropriate file based input or outputs.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FileIOFactory implements IOFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Input<T> makeInput(IOKey key)
    {
        return new FileInput<T>(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Output<T> makeOutput(IOKey key)
    {
        return new FileOutput<T>(key);
    }
}
