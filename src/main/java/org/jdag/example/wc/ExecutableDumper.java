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

package org.jdag.example.wc;

import java.util.List;
import java.util.Map;

import org.jdag.dsl.Input;
import org.jdag.dsl.Output;
import org.jdag.dsl.impl.ComputeFailedException;
import org.jdag.function.Dumper;
import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOKey;

/**
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public class ExecutableDumper extends Dumper<Map<String,Integer>>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ExecutionContext context, List<IOKey> inputKeys,
            List<IOKey> outputKeys) throws ComputeFailedException
    {
        IOKey inputKey = inputKeys.get(0);
        Input<Map<String,Integer>> input =
            context.makeIOFactory(inputKey.getSourceType())
                      .<Map<String,Integer>>makeInput(inputKey);

        IOKey outputKey = outputKeys.get(0);
        Output<String> output = context.makeIOFactory(outputKey.getSourceType())
                                                     .<String>makeOutput(outputKey);

        process(input,output);
    }
}
