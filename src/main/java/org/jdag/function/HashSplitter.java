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
    private int myNumPartitions;

    /**
     * CTOR
     */
    public HashSplitter()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int numPartitions()
    {
        return myNumPartitions;
    }

    public void setPartitions(int numPartitions)
    {
        myNumPartitions = numPartitions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void split(Input<T> input,
                           List<Output<T>> outputs)
    {
        int rc = 0;
        for (T record : new IteratorWrapper<T>(input.getIterator())) {
            int outIndex = rc % myNumPartitions;
            Output<T> output = outputs.get(outIndex);
            output.write(record);
            rc++;
        }

        for (Output<T> output : outputs) {
            output.done();
        }
    }
}
