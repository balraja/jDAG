package org.jdag.function;

import java.util.Iterator;

/**
 * Wraps the given <code>Iterator</code> as an <code>Iterable</code> so that
 * they can be directly used in the for loops.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class IteratorWrapper<T> implements Iterable<T>
{
    private final Iterator<T> myIterator;

    /**
     * CTOR
     */
    public IteratorWrapper(Iterator<T> iterator)
    {
        super();
        myIterator = iterator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator()
    {
        return myIterator;
    }
}
