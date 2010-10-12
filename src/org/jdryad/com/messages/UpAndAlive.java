package org.jdryad.com.messages;

import org.jdryad.com.HostID;
import org.jdryad.com.Message;
import org.jdryad.com.MessageType;

/**
 * The heartbeat message which is sent periodically on a discovery namespace
 * for denoting the liveness of a message.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class UpAndAlive implements Message
{
    private final MessageType myType;

    private final HostID myHost;

    private final long myUpMillis;

    /**
     * CTOR
     */
    public UpAndAlive(MessageType type, HostID host, long upMillis)
    {
        myType = type;
        myHost = host;
        myUpMillis = upMillis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HostID getHostID()
    {
        return myHost;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageType getMessageType()
    {
        return myType;
    }

    /** The number of millis for which the system is up */
    public long getUpMillis()
    {
        return myUpMillis;
    }
}
