package org.jdag.data;

import java.util.List;

import org.jdag.graph.ExecutionContext;

/**
 * A splitter is used for splitting a <code>DataCollection</code> into multiple
 * partitions so that they can be processed in parallel.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Splitter<T>
{
    public int numPartitions();

    /**
     * Splits the data into multiple partitions.
     */
    public void split(FunctionInput<T> input,
                           List<FunctionOutput<T>> outputs,
                           ExecutionContext context);
}
