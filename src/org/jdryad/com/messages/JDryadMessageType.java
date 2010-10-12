package org.jdryad.com.messages;

import org.jdryad.com.MessageType;

/**
 * A simple class that represents the <code>MessageType</code> as an enum.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public enum JDryadMessageType implements MessageType
{
    UP_AND_ALIVE_MESSAGE(1),
    EXECUTE_VERTEX_MESSAGE(2);

    private final int myIntegerRepresentation;

    /**
     * CTOR
     */
    private JDryadMessageType(int integerRepresentation)
    {
        myIntegerRepresentation = integerRepresentation;
    }

    /**
     * Returns the value as an integer
     */
    public int asInteger()
    {
        return myIntegerRepresentation;
    }
}
