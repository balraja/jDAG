package org.jdag.communicator.messages;

import java.io.Serializable;

import org.jdag.communicator.Message;

/**
 * This periodic message is sent from the clients to the server to notify whether a
 * client is alive or not.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class Heartbeat implements Serializable,Message
{
    private final String myNodeID;

    private final long myAliveMillis;

    /**
     * CTOR
     */
    public Heartbeat(String nodeID, long aliveMillis)
    {
        super();
        myNodeID = nodeID;
        myAliveMillis = aliveMillis;
    }

    /**
     * Returns the value of nodeID
     */
    public String getNodeID()
    {
        return myNodeID;
    }

    /**
     * Returns the value of aliveMillis
     */
    public long getAliveMillis()
    {
        return myAliveMillis;
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
                + (int) (myAliveMillis ^ (myAliveMillis >>> 32));
        result = prime * result
                + ((myNodeID == null) ? 0 : myNodeID.hashCode());
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
        Heartbeat other = (Heartbeat) obj;
        if (myAliveMillis != other.myAliveMillis)
            return false;
        if (myNodeID == null) {
            if (other.myNodeID != null)
                return false;
        }
        else if (!myNodeID.equals(other.myNodeID))
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "Heartbeat [myAliveMillis=" + myAliveMillis + ", myNodeID="
                + myNodeID + "]";
    }
}
