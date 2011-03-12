package org.jdag.communicator;

import java.io.Serializable;

/**
 * Type for representing an unique identifier corresponding to a host.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class HostID implements Serializable
{
    /**
     * The serial version id.
     */
    private static final long serialVersionUID = -6057921445338124953L;

    private final String myIdentifier;

    /**
     * CTOR
     */
    public HostID(String identifier)
    {
        myIdentifier = identifier;
    }

    /**
     * Returns the value of identifier
     */
    public String getIdentifier()
    {
        return myIdentifier;
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
                + (myIdentifier == null ? 0 : myIdentifier.hashCode());
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
        HostID other = (HostID) obj;
        if (myIdentifier == null) {
            if (other.myIdentifier != null) {
                return false;
            }
        }
        else if (!myIdentifier.equals(other.myIdentifier)) {
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
        return "HostID [myIdentifier=" + myIdentifier + "]";
    }
}
