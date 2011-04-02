package org.jdag.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.jdag.function.Dumper;
import org.jdag.graph.Edge;
import org.jdag.graph.Graph;
import org.jdag.graph.SimpleVertex;
import org.jdag.graph.VertexID;
import org.jdag.io.IOKey;
import org.jdag.io.KeyGenerator;

/**
 * Wraps a <code>DataCollection</code> so that function calls on a
 * <code>DataCollection</code> can be translated to appropriate
 * data flow paths in the execution graph.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class PseudoDataCollection<T>
    implements DataCollection<T>
{

    /** The execution graph wrapped by this data collection */
    private final Graph myGraph;

    /**
     * The id of the vertex from which the data represented by this
     * collection flows out.
     */
    private final VertexID myVertex;

    /**
     * The identifier of the file where actual data for this collection is
     * stored.
     */
    private final IOKey myFileKey;

    /**
     * The key generator to be used for assigning ids to the temporary files
     * used in the computation
     */
    private final KeyGenerator myKeyGenerator;

    /**
     * CTOR
     */
    public PseudoDataCollection(
            Graph graph, VertexID compVertex, IOKey fileKey, KeyGenerator gen)
    {
        myGraph = graph;
        myVertex = compVertex;
        myFileKey = fileKey;
        myKeyGenerator = gen;
    }

    /**
     * Returns the value of vertex
     */
    public VertexID getVertex()
    {
        return myVertex;
    }

    /**
     * Returns the value of fileKey
     */
    public IOKey getFileKey()
    {
        return myFileKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> DataCollection<V> apply(Function<T, V> function)
    {
         VertexID id = new VertexID(myGraph.getID(), UUID.randomUUID());
         IOKey outputFileKey =
            myKeyGenerator.generateIdentifier(myGraph.getID(),
                                              id.toString() + FILE_SUFFIX);

         SimpleVertex vertex =
             new SimpleVertex(id,
                                      function.getClass().getName(),
                                      Collections.singletonList(myFileKey),
                                      Collections.singletonList(outputFileKey));

         Edge edge = new Edge(myVertex, id);
         myGraph.addVertex(vertex);
         myGraph.addEdge(edge);
         return new PseudoDataCollection<V>(
                 myGraph, id, outputFileKey, myKeyGenerator);
    }

    /**
     * Creates a node in the execution graph that can be used for splitting
     * the data into multiple partitions.
     */
    public ShardedDataCollection<T> partition(Splitter<T> splitter)
    {

         VertexID id = new VertexID(myGraph.getID(), UUID.randomUUID());
         IOKey input = getFileKey();
         List<IOKey> outputFiles = new ArrayList<IOKey>();
         for (int i = 0; i < splitter.numPartitions(); i++) {
             IOKey outputKey =
                myKeyGenerator.generateIdentifier(
                               myGraph.getID(),
                                id + "_"
                                + i
                                + ShardedDataCollection.FILE_SUFFIX);
            outputFiles.add(outputKey);
         }

         SimpleVertex vertex =
             new SimpleVertex(id,
                              splitter.getClass().getName(),
                              Collections.singletonList(input),
                              outputFiles);

         Edge edge = new Edge(myVertex, id);
         myGraph.addVertex(vertex);
         myGraph.addEdge(edge);
         return new JustPartitionedCollection<T>(
                myGraph, id, outputFiles, myKeyGenerator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeOutput(String fileName, Dumper<T> dumper)
    {
        VertexID id = new VertexID(myGraph.getID(), UUID.randomUUID());
        IOKey outputFileKey =
           myKeyGenerator.generateFlatFileIdentifier(
                   myGraph.getID(), fileName, null);

        SimpleVertex vertex =
            new SimpleVertex(id,
                             dumper.getClass().getName(),
                             Collections.singletonList(myFileKey),
                             Collections.singletonList(outputFileKey));

        Edge edge = new Edge(myVertex, id);
        myGraph.addVertex(vertex);
        myGraph.addEdge(edge);
    }
}
