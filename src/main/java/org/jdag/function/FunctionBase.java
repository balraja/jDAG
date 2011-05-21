package org.jdag.function;

import java.util.List;
import org.jdag.dsl.Function;
import org.jdag.dsl.Input;
import org.jdag.dsl.Output;
import org.jdag.dsl.impl.ComputeFailedException;
import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOKey;

/**
 * Defines an abstract base for the <code>Function</code>. The user defined
 * function can extend from this class.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public abstract class FunctionBase<I,O> implements Function<I, O>
{
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
        Input<I> input =
            context.makeIOFactory(inputKey.getSourceType())
                   .makeInput(inputKey);

        IOKey outputKey = outputKeys.get(0);
        Output<O> output =
            context.makeIOFactory(outputKey.getSourceType())
                   .makeOutput(outputKey);

        process(input, output);
    }
}
