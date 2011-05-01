package org.jdag.communicator;

/**
 * Extends <code>RuntimeException</code> to capture the error scenarios that
 * might happen while using a communicator.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class CommunicatorException extends RuntimeException
{

    public CommunicatorException()
    {
        super();
    }

    public CommunicatorException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CommunicatorException(String message)
    {
        super(message);
    }

    public CommunicatorException(Throwable cause)
    {
        super(cause);
    }
}
