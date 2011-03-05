package org.jdag.communicator.messages;

import java.io.Serializable;

import org.jdag.communicator.Message;
import org.jdag.graph.ExecutionResult;
import org.jdag.graph.VertexID;

/**
 * The message to be used for sending back the status related to the
 * execution of a vertex in a node.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ExecuteVertexCommandStatus implements Serializable,Message
{
     private final VertexID myExecutedVertex;

     private final ExecutionResult myResult;

     /**
      * CTOR
      */
    public ExecuteVertexCommandStatus(VertexID executedVertex,
                                                        ExecutionResult result)
    {
        myExecutedVertex = executedVertex;
        myResult = result;
    }

    /**
     * Returns the value of executedVertex
     */
    public VertexID getExecutedVertex()
    {
        return myExecutedVertex;
    }

    /**
     * Returns the value of result
     */
    public ExecutionResult getResult()
    {
        return myResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((myExecutedVertex == null) ? 0 : myExecutedVertex.hashCode());
        result = prime * result
                + ((myResult == null) ? 0 : myResult.hashCode());
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
        ExecuteVertexCommandStatus other = (ExecuteVertexCommandStatus) obj;
        if (myExecutedVertex == null) {
            if (other.myExecutedVertex != null)
                return false;
        }
        else if (!myExecutedVertex.equals(other.myExecutedVertex))
            return false;
        if (myResult == null) {
            if (other.myResult != null)
                return false;
        }
        else if (!myResult.equals(other.myResult))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "ExecuteVertexCommandStatus [myExecutedVertex="
                  + myExecutedVertex + ", myResult=" + myResult + "]";
    }
}
