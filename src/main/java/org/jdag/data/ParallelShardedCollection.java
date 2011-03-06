package org.jdag.data;

import java.util.ArrayList;
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
import org.jdag.io.KeyGenerator;
import org.jdag.io.IOSource;

/**
 * Wraps the state where partitioned data is split across multiple vertices in the
 * graph.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ParallelShardedCollection<T> implements ShardedDataCollection<T>
{
    private final Graph myGraph;

    private final Map<VertexID, IOKey> myVertexToFileMap;

    /**
     * The key generator to be used for assigning ids to the temporary files
     * used in the computation
     */
    private final KeyGenerator myKeyGenerator;

    /**
     * CTOR
     */
    public ParallelShardedCollection(Graph graph,
                                                Map<VertexID, IOKey> vertexToFileMap,
                                                KeyGenerator keyGenerator)
    {
        myGraph = graph;
        myVertexToFileMap = vertexToFileMap;
        myKeyGenerator = keyGenerator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> ShardedDataCollection<V> apply(Function<T, V> function)
    {
        Map<VertexID,IOKey> vertexToFileMap = new HashMap<VertexID, IOKey>();

        for (Map.Entry<VertexID, IOKey> entry : myVertexToFileMap.entrySet() ) {
            VertexID inVertex = entry.getKey();
            VertexID id = new VertexID(myGraph.getID(), UUID.randomUUID());
            IOKey outputFileKey =
               myKeyGenerator.generateIdentifier(myGraph.getID(),  id + FILE_SUFFIX);
            SimpleVertex vertex =
                new SimpleVertex(id,
                                         function.getClass().getName(),
                                         Collections.singletonList(entry.getValue()),
                                         Collections.singletonList(outputFileKey));
            Edge edge = new Edge(inVertex, id);
            myGraph.addVertex(vertex);
            myGraph.addEdge(edge);
            vertexToFileMap.put(id, outputFileKey);
        }
        return new ParallelShardedCollection<V>(
                myGraph, vertexToFileMap, myKeyGenerator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataCollection<T> merge(Merger<T> merger)
    {
        VertexID id = new VertexID(myGraph.getID(), UUID.randomUUID());
        IOKey outputFileKey =
            new IOKey(IOSource.SERIALIZED_FILE, id + FILE_SUFFIX);
        List<IOKey> inputKeys = new ArrayList<IOKey>(myVertexToFileMap.values());
        SimpleVertex vertex =
            new SimpleVertex(id,
                                     merger.getClass().getName(),
                                     inputKeys,
                                     Collections.singletonList(outputFileKey));
        myGraph.addVertex(vertex);

        for (VertexID src : myVertexToFileMap.keySet()) {
            Edge edge = new Edge(src, id);
            myGraph.addEdge(edge);
        }
        return new PseudoDataCollection<T>(
                myGraph, id, outputFileKey, myKeyGenerator);
    }
}
