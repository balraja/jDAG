package org.jdag.data;

import java.util.List;

import org.jdag.graph.ExecutionContext;

/**
 * Defines the function that can be used for merging data from different
 * Shards back into a <code>DataCollection</code>.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Merger<T>
{
    /**
     * Merges the data from different shards into a single collection back.
     */
    public void merge(List<FunctionInput<T>> shards,
                              FunctionOutput<T> output,
                              ExecutionContext graphContext);
}
