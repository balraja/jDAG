package org.jdag.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jdag.graph.Edge;
import org.jdag.graph.Graph;
import org.jdag.graph.SimpleVertex;
import org.jdag.graph.VertexID;
import org.jdag.io.IOKey;
import org.jdag.io.IOSource;

/**
 * Represents a state where data in a node split into many partitions but still not
 * consumed by any other input function.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class JustPartitionedCollection<T> implements ShardedDataCollection<T>
{
    private final Graph myGraph;

    private final VertexID myVertexID;

    private final List<IOKey> myIOKeys;

    /**
     * CTOR
     */
    public JustPartitionedCollection(
            Graph graph, VertexID vertexID, List<IOKey> iOKeys)
    {
        super();
        myGraph = graph;
        myVertexID = vertexID;
        myIOKeys = iOKeys;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataCollection<T> merge(Merger<T> merger)
    {
        // we are just splitting and immediately merging, which in most cases is useless.
         throw new IllegalArgumentException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> DataCollection<V> apply(Function<T, V> function)
    {
        Map<VertexID,IOKey> vertexToFileMap = new HashMap<VertexID, IOKey>();
        for (IOKey input : myIOKeys) {
            VertexID id = new VertexID(myGraph.getID(), UUID.randomUUID());
            IOKey outputFileKey =
                new IOKey(IOSource.FILE_SYSTEM, id + FILE_SUFFIX);
            SimpleVertex vertex =
                new SimpleVertex(id,
                                         function.getClass().getName(),
                                         Collections.singletonList(input),
                                         Collections.singletonList(outputFileKey));
            Edge edge = new Edge(myVertexID, id);
            myGraph.addVertex(vertex);
            myGraph.addEdge(edge);
            vertexToFileMap.put(id, outputFileKey);
        }

        return new ParallelShardedCollection<V>(myGraph, vertexToFileMap);
    }
}
