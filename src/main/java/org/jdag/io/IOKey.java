package org.jdag.io;

import java.util.HashMap;
import java.util.Map;

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

    private final Map<String, String> myAttributes;

    /**
     * CTOR
     */
    public IOKey(IOSource sourceType, String identifier)
    {
        mySourceType = sourceType;
        myIdentifier = identifier;
        myAttributes = new HashMap<String, String>();
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
     * Returns true if the key has the attribute with the given id.
     */
    public boolean hasAttribute(String id)
    {
        return myAttributes.containsKey(id);
    }

    /** Returns value associated with the given key */
    public String getAttribute(String id)
    {
        return myAttributes.get(id);
    }

    /** Adds an attribute with the given value */
    public void addAttribute(String id, String value)
    {
        myAttributes.put(id, value);
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
