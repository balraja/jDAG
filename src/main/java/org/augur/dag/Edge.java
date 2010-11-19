package org.augur.dag;

/**
 * A simple class that represents the unidirectional flow of data in a system.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class Edge
{
    private final VertexID mySource;

    private final VertexID myDestination;

    private final IOKey myDataKey;

    public Edge(VertexID source, VertexID destination, IOKey dataKey)
    {
        super();
        mySource = source;
        myDestination = destination;
        myDataKey = dataKey;
    }

    /**
     * Returns the value of source
     */
    public VertexID getSource()
    {
        return mySource;
    }

    /**
     * Returns the value of destination
     */
    public VertexID getDestination()
    {
        return myDestination;
    }

    /**
     * Returns the value of key
     */
    public IOKey getDataKey()
    {
        return myDataKey;
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
                + ((myDataKey == null) ? 0 : myDataKey.hashCode());
        result = prime * result
                + ((myDestination == null) ? 0 : myDestination.hashCode());
        result = prime * result
                + ((mySource == null) ? 0 : mySource.hashCode());
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
        Edge other = (Edge) obj;
        if (myDataKey == null) {
            if (other.myDataKey != null)
                return false;
        }
        else if (!myDataKey.equals(other.myDataKey))
            return false;
        if (myDestination == null) {
            if (other.myDestination != null)
                return false;
        }
        else if (!myDestination.equals(other.myDestination))
            return false;
        if (mySource == null) {
            if (other.mySource != null)
                return false;
        }
        else if (!mySource.equals(other.mySource))
            return false;
        return true;
    }
}
