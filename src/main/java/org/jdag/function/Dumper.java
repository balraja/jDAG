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

package org.jdag.function;

import org.jdag.dsl.Input;
import org.jdag.dsl.Output;

/**
 * A helper class to support dumping the contents of a <code>DataCollection</code>
 * to a file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class Dumper<T> extends FunctionBase<T, String>
{
    private static final long serialVersionUID = 1L;

    /**
      * Dumps the contents of collection to the file.
      */
     public void  process(Input<T> input, Output<String> output)
     {
           for (T record : new IteratorWrapper<T>(input.getIterator())) {
               output.write(record.toString());
           }
           output.done();
     }
}
