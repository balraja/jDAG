package org.jdryad.dag;

import java.util.Iterator;

/**
 * Defines the source of input that is to be used for processing in a vertex.
 * 
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface FunctionInput
{   
    /** 
     * Returns an <code>Iterator</code> over the <code>Record</code>s to be
     * processed.
     */
    public Iterator<Record> getIterator();
}
