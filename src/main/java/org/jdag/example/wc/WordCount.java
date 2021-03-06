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

import java.util.Map;

import org.jdag.dsl.DataCollection;
import org.jdag.dsl.ShardedDataCollection;
import org.jdag.dsl.impl.DataProcessor;
import org.jdag.function.HashSplitter;
import org.jdag.graph.Graph;
import org.jdag.graph.GraphID;
import org.jdag.io.FilePathGenerator;
import org.jdag.io.flatfile.SimpleInterpreter;

/**
 * Defines the contract of a class that can be used for performing the word count
 * in a file using jDAG.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class WordCount
{
    private final String myInputFile;

    /**
     * CTOR
     */
    public WordCount(String inputFile)
    {
        super();
        myInputFile = inputFile;
    }

    /**
     * Returns the execution graph to be used for computing the word count in a file
     */
    public Graph getGraph()
    {
         DataProcessor processor =
             new DataProcessor(new GraphID("WordCountTest"),
                               new FilePathGenerator("C:\\temp"));
         
         DataCollection<String> words =
             processor.fromInputSource(myInputFile, new SimpleInterpreter());
         
         ShardedDataCollection<String> partitionedInput =
             words.partition(new HashSplitter(4));
         
         ShardedDataCollection<Map<String,Integer>> partialResults =
             partitionedInput.apply(new CountWords());
         
         DataCollection<Map<String, Integer>> wordCount =
             partialResults.merge(new WordCountMerger());
         
         wordCount.writeOutput("wcout.txt", new ExecutableDumper());
         return processor.getGraph();
    }
}
