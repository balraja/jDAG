/*******************************************************************************
 * jDAG is a project to build acyclic dataflow graphs for processing massive datasets.
 *
 *     Copyright (C) 2011, Author: Balraja,Subbiah
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package org.jdag.io;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Type to be used for representing the keys used for identifying the
 * i/p or o/p sources.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class IOKey implements Externalizable
{
    private static final long serialVersionUID = 1L;

    private IOSource mySourceType;

    private String myIdentifier;
    
    /**
     * CTOR
     */
    public IOKey()
    {
        mySourceType = null;
        myIdentifier = null;
    }

    /**
     * CTOR
     */
    public IOKey(IOSource sourceType, String identifier)
    {
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

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException
    {
        mySourceType = IOSource.valueOf(in.readUTF());
        myIdentifier = in.readUTF();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeUTF(mySourceType.name());
        out.writeUTF(myIdentifier);
    }
}
