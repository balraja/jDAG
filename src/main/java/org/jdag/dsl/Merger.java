package org.jdag.dsl;

import java.util.List;


/**
 * Defines the function that can be used for merging data from different
 * Shards back into a <code>DataCollection</code>.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Merger<T> extends Executable
{
    /**
     * Merges the data from different shards into a single collection back.
     */
    public void merge(List<Input<T>> shards,
                              Output<T> output);
}
