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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jdag.dsl.Output;

/**
 * Implements <code>FunctionOutput</code> that writes data to a flat file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FlatFileOutput<T> implements Output<T>
{
    private final PrintWriter myWriter;

    private final Interpreter<T> myLineInterpreter;

    /**
     * CTOR
     */
    public FlatFileOutput(Interpreter<T> lineInterpreter, String fileName)
    {
        myLineInterpreter = lineInterpreter;
        File f = new File(fileName);

        try {
            myWriter =
                new PrintWriter(new BufferedWriter(new FileWriter(f)));
        }
        catch (IOException e) {
             throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void done()
    {
        myWriter.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(T r)
    {
        String line = myLineInterpreter != null ?
                          myLineInterpreter.flattenRecord(r)
                          : r.toString();
        myWriter.println(line);
    }
}
