package org.jdag.communicator.messages;

import java.io.Serializable;

import org.jdag.communicator.Message;
import org.jdag.graph.Vertex;

/**
 * The message to be used for sending the vertex to the node for execution.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ExecuteVertexCommand implements Serializable,Message
{
    private final Vertex myVertex;

    /**
     * CTOR
     */
    public ExecuteVertexCommand(Vertex vertex)
    {
        super();
        myVertex = vertex;
    }

    /**
     * Returns the value of vertex
     */
    public Vertex getVertex()
    {
        return myVertex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((myVertex == null) ? 0 : myVertex.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExecuteVertexCommand other = (ExecuteVertexCommand) obj;
        if (myVertex == null) {
            if (other.myVertex != null)
                return false;
        }
        else if (!myVertex.equals(other.myVertex))
            return false;
        return true;
    }
}
