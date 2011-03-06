package org.jdag.data;

/**
 * Defines the state where data is split into multiple partitions primarily to
 * enable processing of data in parallel.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface ShardedDataCollection<T>
{
    /**
     * The sufiix to be appended to the file name in which data is stored.
     */
    public static final String FILE_SUFFIX = "_shard";

    /**
     * Merges the data from different shards back into a data collection.
     */
    public DataCollection<T> merge(Merger<T> merger);

    /**
     * Applies the function directly on the data collection.
     */
    public <V> ShardedDataCollection<V> apply(Function<T, V> function);
}
