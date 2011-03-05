package org.jdag.communicator;

/**
 * Type that defines the basic definition for a piece of code that
 * reacts to given message.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Reactor
{
    /** This method should be overridden for processing the message */
    public void process(Message m);
}
