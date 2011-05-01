package org.jdag.communicator;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Type for representing an unique identifier corresponding to a host.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class HostID implements Externalizable
{
    /**
     * The serial version id.
     */
    private static final long serialVersionUID = -6057921445338124953L;

    private String myIdentifier;
    
    /**
     * CTOR
     */
    public HostID()
    {
        myIdentifier = null;
    }

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

    @Override
    public void readExternal(ObjectInput in) 
        throws IOException, ClassNotFoundException
    {
        myIdentifier = in.readUTF();
        
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeUTF(myIdentifier);
    }
}
