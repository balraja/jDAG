package org.jdag.data;

/**
 * Defines the state where data is split into multiple partitions primarily to
 * enable processing of data in parallel.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface ShardedDataCollection<T> extends DataCollection<T>
{
    /**
     * Merges the data from different shards back into a data collection.
     */
    public DataCollection<T> merge(Merger<T> merger);
}
