package org.jdag.data;

import java.util.List;

/**
 * A splitter is used for splitting a <code>DataCollection</code> into multiple
 * partitions so that they can be processed in parallel.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Splitter<T> extends Executable
{
    public int numPartitions();

    /**
     * Splits the data into multiple partitions.
     */
    public void split(Input<T> input,
                           List<Output<T>> outputs);
}
