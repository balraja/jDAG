package org.jdryad.messenger;

import java.io.Serializable;

/**
 * A tagged implementation of network messages.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public abstract class NetworkMessage implements Message, Serializable
{
    /**
     * Request id of the message. The role it plays depends on the context.
     * In case of request its the request no. In case of response it correponds
     * to the request that generated it.
     */
    private final int myRequestID;

    /** CTOR */
    public NetworkMessage(int requestID)
    {
        myRequestID = requestID;
    }

    /** Returns the request no of the message */
    public int getRequestNumber()
    {
        return myRequestID;
    }
}
