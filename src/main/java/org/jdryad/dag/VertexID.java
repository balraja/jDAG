package org.jdryad.dag;

import java.io.Serializable;

/**
 * The class to be used for identifying a vertex.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class VertexID implements Serializable
{
	private static final long serialVersionUID = -9172393936458409145L;

	private String myName;

    /**
     * CTOR
     */
    public VertexID()
    {
        myName = null;
    }

    /**
     * CTOR
     */
    public VertexID(String name)
    {
        myName = name;
    }

    /**
     * Returns the value of name
     */
    public String getName()
    {
        return myName;
    }

    /**
     * Sets the value of name.
     */
    public void setName(String name)
    {
        myName = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((myName == null) ? 0 : myName.hashCode());
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
        if (myName == null) {
            if (other.myName != null)
                return false;
        }
        else if (!myName.equals(other.myName))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "VertexID [myName=" + myName + "]";
    }
}
