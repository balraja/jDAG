package org.jdag.communicator.impl;

import org.jdag.communicator.Message;
import org.jdag.communicator.Reactor;

/**
 * Wraps a <code>Reactor</code> so that it can be scheduled on a
 * Executor.
 */
public class ExecutableReactor implements Runnable
{
    private final Reactor myReactor;

    private final Message myMessage;

    /**
     * CTOR
     */
    public ExecutableReactor(Message message, Reactor reactor)
    {
        myMessage = message;
        myReactor = reactor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        myReactor.process(myMessage);
    }
}