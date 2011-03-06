package org.jdag.function;

import java.util.List;

import org.jdag.data.FunctionInput;
import org.jdag.data.FunctionOutput;
import org.jdag.data.Splitter;
import org.jdag.graph.ExecutionContext;

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
    public void split(FunctionInput<T> input,
                           List<FunctionOutput<T>> outputs);
    {
        for (T record : new IteratorWrapper<T>(input.getIterator())) {
            int hashCode = Math.abs(record.hashCode());
            int outIndex = hashCode % myNumPartitions;
            FunctionOutput<T> output = outputs.get(outIndex);
            output.write(record);
        }
    }
}
