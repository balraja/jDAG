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
 * Type that defines an unique id assigned for a task graph.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class GraphID implements Externalizable
{
    /**
     * The serial version id.
     */
    private static final long serialVersionUID = -2390864513699127717L;

    /** The string id assigned for a task graph */
    private String myID;
    
    /**
     * CTOR
     */
    public GraphID()
    {
        myID= null;
    }

    /**
     * CTOR
     */
    public GraphID(String iD)
    {
        myID = iD;
    }

    public String getID()
    {
        return myID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (myID == null ? 0 : myID.hashCode());
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
        GraphID other = (GraphID) obj;
        if (myID == null) {
            if (other.myID != null) {
                return false;
            }
        }
        else if (!myID.equals(other.myID)) {
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
        return "TaskGraphID [myID=" + myID + "]";
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException
    {
        myID = in.readUTF();
        
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeUTF(myID);
    }
}
