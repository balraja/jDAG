package org.jdag.example.wc;

import java.util.List;
import java.util.Map;

import org.jdag.data.ComputeFailedException;
import org.jdag.data.Dumper;
import org.jdag.data.Input;
import org.jdag.data.Output;
import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOKey;

/**
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public class ExecutableDumper extends Dumper<Map<String,Integer>>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ExecutionContext context, List<IOKey> inputKeys,
            List<IOKey> outputKeys) throws ComputeFailedException
    {
        IOKey inputKey = inputKeys.get(0);
        Input<Map<String,Integer>> input =
            context.makeIOFactory(inputKey.getSourceType())
                      .<Map<String,Integer>>makeInput(inputKey);

        IOKey outputKey = outputKeys.get(0);
        Output<String> output = context.makeIOFactory(outputKey.getSourceType())
                                                     .<String>makeOutput(outputKey);

        process(input,output);
    }
}
