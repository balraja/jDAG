package org.augur.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ThreadFactory;

/**
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public final class NamedThreadFactory
{
    /** Private CTOR */
    private NamedThreadFactory()
    {
    }

    /**
     * Returns a <code>ThreadFactory that creates threads with the simple
     * name of the class.
     */
    public static ThreadFactory newNamedFactory(
        Class<?> hostClass)
    {
        ThreadFactoryBuilder tfBuilder = new ThreadFactoryBuilder();
        ThreadFactory namedFactory =
            tfBuilder.setNameFormat(hostClass.getSimpleName())
                     .build();
        return namedFactory;
    }

    /**
     * Returns a <code>ThreadFactory that creates threads with the given simple
     * name.
     */
    public static ThreadFactory newNamedFactory(
        String simpleName)
    {
        ThreadFactoryBuilder tfBuilder = new ThreadFactoryBuilder();
        ThreadFactory namedFactory =
            tfBuilder.setNameFormat(simpleName)
                     .build();
        return namedFactory;
    }
}
