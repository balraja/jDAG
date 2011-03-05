package org.jdag.data;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.jdag.graph.Edge;
import org.jdag.graph.Graph;
import org.jdag.graph.GraphID;
import org.jdag.graph.InputVertex;
import org.jdag.graph.SimpleVertex;
import org.jdag.graph.Vertex;
import org.jdag.graph.VertexID;
import org.jdag.io.IOKey;
import org.jdag.io.KeyGenerator;
import org.jdag.io.IOSource;
import org.jdag.io.flatfile.Interpreter;

/**
 * DataProcessor acts as a wrapper for building the data flow graph that needs to
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
    private DataProcessor(GraphID graphID, KeyGenerator keyGenerator)
    {
        myGraph = new Graph(graphID);
        myKeyGenerator = keyGenerator;
    }

    /**
     * Wraps data in a file as a <code>DataCollection</code>
     */
    public <T> DataCollection<T> fromInputSource(
        String fileName, Interpreter<T> interpreter)
    {
         IOKey fileKey = new IOKey(IOSource.FILE_SYSTEM, fileName);
         fileKey.addAttribute(Interpreter.ATTRIBUTE_NAME,
                                     interpreter.getClass().getName());
         VertexID id = new VertexID(myGraph.getID(), UUID.randomUUID());
         Vertex vertex = new InputVertex(id, fileKey);
         myGraph.addVertex(vertex);
         return new PseudoDataCollection<T>(myGraph, id, fileKey, myKeyGenerator);
    }

    /**
     * Creates a node in the execution graph that can be used for splitting
     * the data into multiple partitions.
     */
    public <T> ShardedDataCollection<T> partition(
        DataCollection<T> dataCollection, Splitter<T> splitter)
    {
         Preconditions.checkArgument(
             ! (dataCollection instanceof ShardedDataCollection<?>));
         Preconditions.checkArgument(
             dataCollection instanceof PseudoDataCollection<?>);

         PseudoDataCollection<T> pseudoDataCollection =
             (PseudoDataCollection<T>) dataCollection;

         IOKey input = pseudoDataCollection.getFileKey();
         List<IOKey> outputFiles = new ArrayList<IOKey>();
         for (int i = 0; i < splitter.numPartitions(); i++) {
             outputFiles.add(myKeyGenerator.generateIdentifier(
                 myGraph.getID(),
                 input.getIdentifier() + " _" + i + "_" + SHARD));
         }

         VertexID id = new VertexID(myGraph.getID(), UUID.randomUUID());

         SimpleVertex vertex =
             new SimpleVertex(id,
                                      splitter.getClass().getName(),
                                      Collections.singletonList(input),
                                      outputFiles);

         Edge edge = new Edge(pseudoDataCollection.getVertex(), id);
         myGraph.addVertex(vertex);
         myGraph.addEdge(edge);
         return new JustPartitionedCollection<T>(
                myGraph, id, outputFiles, myKeyGenerator);
    }
}
