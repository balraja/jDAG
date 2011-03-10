package org.jdag.function;

import java.util.List;

import org.jdag.data.Input;
import org.jdag.data.Output;
import org.jdag.data.Splitter;

/**
 * The splitter function to be used for hashing the records
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public abstract class HashSplitter<T> implements Splitter<T>
{
    private final int myNumPartitions;

    /**
     * CTOR
     */
    public HashSplitter(int numPartitions)
    {
        myNumPartitions = numPartitions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int numPartitions()
    {
        return myNumPartitions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void split(Input<T> input,
                           List<Output<T>> outputs)
    {
        for (T record : new IteratorWrapper<T>(input.getIterator())) {
            int hashCode = Math.abs(record.hashCode());
            int outIndex = hashCode % myNumPartitions;
            Output<T> output = outputs.get(outIndex);
            output.write(record);
        }
    }
}
