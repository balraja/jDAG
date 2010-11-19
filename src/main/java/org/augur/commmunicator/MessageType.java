package org.augur.commmunicator;

/**
 * A simple type that abstracts the type of messages sent over wire.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface MessageType
{
    /** Returns the integer representation of message type */
    public int asInteger();
}
