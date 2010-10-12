package org.jdryad.com;

/**
 * A tagged type to denote a message being sent on wire.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Message
{
    /** The host from which this message has been sent */
    public HostID getHostID();

    /** The type with which the message is tagged */
    public MessageType getMessageType();
}
