package org.jdag.graph;

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

    public Edge(VertexID source, VertexID destination)
    {
        super();
        mySource = source;
        myDestination = destination;
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
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + (myDestination == null ? 0 : myDestination.hashCode());
        result = prime * result
                + (mySource == null ? 0 : mySource.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Edge other = (Edge) obj;
        if (myDestination == null) {
            if (other.myDestination != null) {
                return false;
            }
        }
        else if (!myDestination.equals(other.myDestination)) {
            return false;
        }
        if (mySource == null) {
            if (other.mySource != null) {
                return false;
            }
        }
        else if (!mySource.equals(other.mySource)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "Edge [myDestination=" + myDestination + ", mySource="
                + mySource + "]";
    }
}
