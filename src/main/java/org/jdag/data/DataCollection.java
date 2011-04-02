package org.jdag.data;

import org.jdag.function.Dumper;

/**
 * Type to define the collection of data records to be processed.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface DataCollection<T>
{
    /**
     * The sufiix to be appended to the file name in which data is stored.
     */
    public static final String FILE_SUFFIX = "_data";

    /**
     * Applies the function directly on the data collection.
     */
    public <V> DataCollection<V> apply(Function<T, V> function);

    /**
     * Creates a node in the execution graph that can be used for splitting
     * the data into multiple partitions.
     */
    public ShardedDataCollection<T> partition(Splitter<T> splitter);

    /**
     * Dump the contents of collection to the file.
     */
    public void writeOutput(String fileName, Dumper<T> dumper);
}
