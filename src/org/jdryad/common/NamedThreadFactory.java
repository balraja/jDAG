package org.jdryad.common;

import java.util.concurrent.ThreadFactory;

/**
 * Implementation of ThreadFactory that creates named threads.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class NamedThreadFactory implements ThreadFactory
{
    private int myCounter;

    private final String myThreadPoolName;

    /**
     * CTOR
     */
    public NamedThreadFactory(Class<?> hostClass)
    {
        myThreadPoolName = hostClass.getSimpleName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Thread newThread(Runnable r)
    {
        myCounter++;
        String threadName = myThreadPoolName + myCounter;
        return new Thread(r, threadName);
    }
}
