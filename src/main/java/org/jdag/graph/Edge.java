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

package org.jdag.graph;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * A simple class that represents the unidirectional flow of data in a system.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class Edge implements Externalizable
{
    private VertexID mySource;

    private VertexID myDestination;
    
    /**
     * CTOR
     */
    public Edge()
    {
        mySource = null;
        myDestination = null;
    }

    /**
     * CTOR
     */
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

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException
    {
        mySource = (VertexID) in.readObject();
        myDestination = (VertexID) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(mySource);
        out.writeObject(myDestination);
    }
}
