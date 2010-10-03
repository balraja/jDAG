package org.jdryad.dag;

/**
 * Type that defines an unique id assigned for a task graph.
 * 
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ExecutionGraphID
{
    /** The string id assigned for a task graph */
    private final String myID;

    /**
     * CTOR
     */
    public ExecutionGraphID(String iD)
    {
        super();
        myID = iD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((myID == null) ? 0 : myID.hashCode());
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
        ExecutionGraphID other = (ExecutionGraphID) obj;
        if (myID == null) {
            if (other.myID != null)
                return false;
        }
        else if (!myID.equals(other.myID))
            return false;
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
