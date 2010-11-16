package org.jdryad.commmunicator;

/**
 * A tagged type to denote a message being sent on wire.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Message
{
    /** The type with which the message is tagged */
    public MessageType getMessageType();

    /** The actual data that is sent as aprt of this message */
    public Object getPayload();
}
