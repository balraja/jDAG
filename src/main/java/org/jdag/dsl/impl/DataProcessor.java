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

import java.util.UUID;

import org.jdag.dsl.DataCollection;
import org.jdag.graph.Graph;
import org.jdag.graph.GraphID;
import org.jdag.graph.InputVertex;
import org.jdag.graph.Vertex;
import org.jdag.graph.VertexID;
import org.jdag.io.IOKey;
import org.jdag.io.KeyGenerator;
import org.jdag.io.IOSource;
import org.jdag.io.flatfile.FlatFileIOKey;
import org.jdag.io.flatfile.Interpreter;

/**
 * DataProcessor acts as a helper while building the data flow graph that needs to
 * be executed in a parallel fashion.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public final class DataProcessor
{
    public static final String SHARD = "shard";

    /**
     * The execution graph that needs to be built.
     */
    private final Graph myGraph;

    /**
     * The key generator to be used for uniquely identifying the key.
     */
    private final KeyGenerator myKeyGenerator;

    /**
     * CTOR
     */
    public DataProcessor(GraphID graphID, KeyGenerator keyGenerator)
    {
        myGraph = new Graph(graphID);
        myKeyGenerator = keyGenerator;
    }


    /**
     * Returns the value of graph
     */
    public Graph getGraph()
    {
        return myGraph;
    }

    /**
     * Wraps data in a file as a <code>DataCollection</code>
     */
    public <T> DataCollection<T> fromInputSource(
        String fileName, Interpreter<T> interpreter)
    {
         IOKey fileKey =
             new FlatFileIOKey(IOSource.FLAT_FILE,
                               fileName,
                               interpreter.getClass().getName());

         VertexID id = new VertexID(myGraph.getID(), UUID.randomUUID());
         Vertex vertex = new InputVertex(id, fileKey);
         myGraph.addVertex(vertex);
         return new PseudoDataCollection<T>(myGraph, id, fileKey, myKeyGenerator);
    }
}
