package org.jdag.graph;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;

/**
 * The unique identifier for a vertex in a graph.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class VertexID implements Externalizable
{
	private static final long serialVersionUID = -9172393936458409145L;

	private GraphID myGraphID;

	private UUID myVertexID;
	
	/**
	 * CTOR
	 */
	public VertexID()
	{
	    myGraphID = null;
	    myVertexID = null;
	}

	/**
	 * CTOR
	 */
    public VertexID(GraphID graphID, UUID vertexID)
    {
        super();
        myGraphID = graphID;
        myVertexID = vertexID;
    }

    /**
     * Returns the value of graphID
     */
    public GraphID getGraphID()
    {
        return myGraphID;
    }

    /**
     * Returns the value of vertexID
     */
    public UUID getVertexID()
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
        VertexID other = (VertexID) obj;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return myGraphID.getID() + "_" + myVertexID.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readExternal(ObjectInput in) 
        throws IOException,ClassNotFoundException
    {
        myGraphID = (GraphID) in.readObject();
        myVertexID = (UUID) in.readObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(myGraphID);
        out.writeObject(myVertexID);
    }
}
