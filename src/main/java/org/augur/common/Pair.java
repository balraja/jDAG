package org.augur.common;

/**
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public class Pair<T, V>
{
    private final T myFirst;

    private final V mySecond;

    /**
     * CTOR
     */
    public Pair(T first, V second)
    {
        super();
        myFirst = first;
        mySecond = second;
    }

    /**
     * Returns the value of first
     */
    public T getFirst()
    {
        return myFirst;
    }

    /**
     * Returns the value of second
     */
    public V getSecond()
    {
        return mySecond;
    }

}
