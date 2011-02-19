package org.augur.dag;

import java.util.Iterator;

/**
 * Defines the source of input that is to be used for processing in a vertex.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface FunctionInput<T extends Record>
{
    /**
     * Returns an <code>Iterator</code> over the <code>Record</code>s to be
     * processed.
     */
    public Iterator<T> getIterator();
}
