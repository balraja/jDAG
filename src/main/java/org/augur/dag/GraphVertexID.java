package org.augur.dag;

/**
 * A key to be used for identifying a vertex and its graph.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class GraphVertexID
{
    private final ExecutionGraphID myGraphID;

    private final VertexID myVertexID;

    /**
     * CTOR
     */
    public GraphVertexID(ExecutionGraphID graphID, VertexID vertexID)
    {
        myGraphID = graphID;
        myVertexID = vertexID;
    }

    /**
     * Returns the value of graphID
     */
    public ExecutionGraphID getGraphID()
    {
        return myGraphID;
    }

    /**
     * Returns the value of vertexID
     */
    public VertexID getVertexID()
    {
        return myVertexID;
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
                + ((myGraphID == null) ? 0 : myGraphID.hashCode());
        result = prime * result
                + ((myVertexID == null) ? 0 : myVertexID.hashCode());
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
        GraphVertexID other = (GraphVertexID) obj;
        if (myGraphID == null) {
            if (other.myGraphID != null)
                return false;
        }
        else if (!myGraphID.equals(other.myGraphID))
            return false;
        if (myVertexID == null) {
            if (other.myVertexID != null)
                return false;
        }
        else if (!myVertexID.equals(other.myVertexID))
            return false;
        return true;
    }
}
