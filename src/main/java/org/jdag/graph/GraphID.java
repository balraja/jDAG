package org.jdag.graph;

import java.io.Serializable;

/**
 * Type that defines an unique id assigned for a task graph.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class GraphID implements Serializable
{
    /**
     * The serial version id.
     */
    private static final long serialVersionUID = -2390864513699127717L;

    /** The string id assigned for a task graph */
    private final String myID;

    /**
     * CTOR
     */
    public GraphID(String iD)
    {
        super();
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

}
