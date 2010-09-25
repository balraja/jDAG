package org.jdryad.messenger;

/**
 * This handler is invoked by {@link Messenger} for notifying about received
 * messages.
 *
 * @author subbiahb
 * @version $Id:$
 */
public interface ReceiveMessageHandler
{
    public void messageReceived(Message m);
}
