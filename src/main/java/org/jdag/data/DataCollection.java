package org.jdag.data;

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
}
