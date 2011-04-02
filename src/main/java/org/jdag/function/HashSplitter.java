package org.jdag.function;

import java.util.ArrayList;
import java.util.List;

import org.jdag.data.ComputeFailedException;
import org.jdag.data.Input;
import org.jdag.data.Output;
import org.jdag.data.Splitter;
import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOKey;

/**
 * The splitter function to be used for hashing the records
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class HashSplitter<T> implements Splitter<T>
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
    public void split(Input<T> input, List<Output<T>> outputs)
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ExecutionContext context,
                                      List<IOKey> inputKeys,
                                      List<IOKey> outputKeys)
        throws ComputeFailedException
    {
        IOKey inputKey = inputKeys.get(0);
        Input<T> input =
            context.makeIOFactory(inputKey.getSourceType())
                      .makeInput(inputKey);

        List<Output<T>> outputs = new ArrayList<Output<T>>();
       for (IOKey outputKey : outputKeys) {
           outputs.add(
                context.makeIOFactory(outputKey.getSourceType())
                           .<T>makeOutput(outputKey)
           );
       }

        split(input, outputs);
    }
}
