package org.jdag.function;

import java.util.ArrayList;
import java.util.List;

import org.jdag.dsl.Input;
import org.jdag.dsl.Merger;
import org.jdag.dsl.Output;
import org.jdag.dsl.impl.ComputeFailedException;
import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOKey;

/**
 * Defines an abstract base class for all the <code>Merger</code>
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public abstract class MergerBase<T> implements Merger<T>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ExecutionContext context,
                                      List<IOKey> inputKeys,
                                      List<IOKey> outputKeys) throws ComputeFailedException
    {
        List<Input<T>> inputs =  new ArrayList<Input<T>>();

        for (IOKey inputKey : inputKeys) {
            inputs.add(context.makeIOFactory(inputKey.getSourceType())
                                          .<T>makeInput(inputKey));
        }

        IOKey outputKey = outputKeys.get(0);
        Output<T> output =
            context.makeIOFactory(outputKey.getSourceType())
                       .makeOutput(outputKey);

        merge(inputs, output);
    }
}
