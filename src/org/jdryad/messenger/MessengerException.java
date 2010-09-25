package org.jdryad.messenger;

/**
 * Exception used to denote erroneous conditions happening wrt
 * Messenger.
 *
 * @author subbiahb
 * @version $Id:$
 */
public class MessengerException extends Exception
{
    public MessengerException() {
        super();
    }

    public MessengerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessengerException(String message) {
        super(message);
    }

    public MessengerException(Throwable cause) {
        super(cause);
    }
}
