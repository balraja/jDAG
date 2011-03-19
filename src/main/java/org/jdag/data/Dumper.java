package org.jdag.data;

import org.jdag.function.IteratorWrapper;

/**
 * A helper class to support dumping the contents of a <code>DataCollection</code>
 * to a file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public abstract class Dumper<T> implements Function<T, String>
{
    private static final long serialVersionUID = 1L;

    /**
      * Dumps the contents of collection to the file.
      */
     public void  process(Input<T> input, Output<String> output)
     {
           for (T record : new IteratorWrapper<T>(input.getIterator())) {
               output.write(record.toString());
           }
           output.done();
     }
}
