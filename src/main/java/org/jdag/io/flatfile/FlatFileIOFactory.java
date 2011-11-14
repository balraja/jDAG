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

import org.jdag.dsl.Input;
import org.jdag.dsl.Output;
import org.jdag.io.IOFactory;
import org.jdag.io.IOKey;

import com.google.common.base.Preconditions;

/**
 * Implements <code>IOFactory</code> that reads data from flat files.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FlatFileIOFactory implements IOFactory
{
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> Input<T> makeInput(IOKey key)
    {
        Preconditions.checkArgument(
            key instanceof FlatFileIOKey);

        try {
            FlatFileIOKey flatFileKey = (FlatFileIOKey) key;
            Interpreter<T> lineInterpreter =
                (Interpreter<T>) Class.forName(flatFileKey.getInterpreterClassName())
                                                          .newInstance();
            return new FlatFileInput<T>(lineInterpreter, key.getIdentifier());
        }
        catch (InstantiationException e) {
             throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
             throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e) {
             throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> Output<T> makeOutput(IOKey key)
    {
        Preconditions.checkArgument(
                key instanceof FlatFileIOKey);

        try {
            FlatFileIOKey flatFileKey = (FlatFileIOKey) key;
            if (flatFileKey.getInterpreterClassName() != null) {
                Interpreter<T> lineInterpreter =
                    (Interpreter<T>) Class.forName(flatFileKey.getInterpreterClassName())
                                                   .newInstance();
                return new FlatFileOutput(lineInterpreter, key.getIdentifier());
            }
            else {
                return new FlatFileOutput(null, flatFileKey.getIdentifier());
            }
        }
        catch (InstantiationException e) {
             throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
             throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e) {
             throw new RuntimeException(e);
        }
    }
}
