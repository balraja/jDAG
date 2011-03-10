package org.jdag.data;

import java.util.Iterator;

/**
 * Defines the source of input that is to be used for processing in a vertex.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Input<T>
{
    /**
     * Returns an <code>Iterator</code> over the <code>Record</code>s to be
     * processed.
     */
    public Iterator<T> getIterator();
}
