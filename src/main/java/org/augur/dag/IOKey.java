package org.augur.dag;

/**
 * Type to be used for representing the keys used for identifying the
 * i/p or o/p sources.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class IOKey
{
    private final IOSource mySourceType;

    private final String myIdentifier;

    /**
     * CTOR
     */
    public IOKey(IOSource sourceType, String identifier)
    {
        super();
        mySourceType = sourceType;
        myIdentifier = identifier;
    }

    /**
     * Returns the value of sourceType
     */
    public IOSource getSourceType()
    {
        return mySourceType;
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
                + ((myIdentifier == null) ? 0 : myIdentifier.hashCode());
        result = prime * result
                + ((mySourceType == null) ? 0 : mySourceType.hashCode());
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
        IOKey other = (IOKey) obj;
        if (myIdentifier == null) {
            if (other.myIdentifier != null)
                return false;
        }
        else if (!myIdentifier.equals(other.myIdentifier))
            return false;
        if (mySourceType == null) {
            if (other.mySourceType != null)
                return false;
        }
        else if (!mySourceType.equals(other.mySourceType))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "Key [myIdentifier=" + myIdentifier + ", mySourceType="
                + mySourceType + "]";
    }

}
