package org.jdag.commmunicator;

import java.util.concurrent.Executor;

/**
 * The definition of the communicator to be used for communicating with the
 * rest of the world. This abstracts paradigms that can be used for
 * communication.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Communicator
{
    /** Sends message to the given host */
    public void sendMessage(HostID host, Message m);

    /**
     * Attaches a reactor for the given type of message. When a message of this
     * type arrives, the reactor will be executed in the communicator's thread.
     */
    public void attachReactor(Class<? extends Message> type, Reactor r);

    /**
     * Attaches a reactor for the given type of message.  When a message of this
     * type arrives, the reactor will be executed in the given executor thread.
     */
    public void attachReactor(Class<? extends Message> type,
                                         Reactor r,
                                         Executor executor);

    /** Starts the communicator */
    public void start();

    /** Stops the communicator */
    public void stop();

    /** Returns the HostID corresponding to this node */
    public HostID getMyHostID();
}
