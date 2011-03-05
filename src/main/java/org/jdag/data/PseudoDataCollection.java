package org.jdag.data;

import java.util.Collections;
import java.util.UUID;

import javax.security.auth.kerberos.KerberosKey;

import org.jdag.graph.Edge;
import org.jdag.graph.Graph;
import org.jdag.graph.SimpleVertex;
import org.jdag.graph.VertexID;
import org.jdag.io.IOKey;
import org.jdag.io.KeyGenerator;
import org.jdag.io.IOSource;

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
}
