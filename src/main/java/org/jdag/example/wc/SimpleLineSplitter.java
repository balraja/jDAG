package org.jdag.example.wc;

import java.util.ArrayList;
import java.util.List;

import org.jdag.data.ComputeFailedException;
import org.jdag.data.Input;
import org.jdag.data.Output;
import org.jdag.function.HashSplitter;
import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOKey;

/**
 * A simple splitter that splits the given file into multiple files.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class SimpleLineSplitter extends HashSplitter<String>
{

    /**
     * CTOR
     */
    public SimpleLineSplitter(int numPartitions)
    {
        super(numPartitions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ExecutionContext context, List<IOKey> inputKeys,
            List<IOKey> outputKeys) throws ComputeFailedException
    {
         List<Output<String>> outputs =
             new ArrayList<Output<String>>();
        for (IOKey outputKey : outputKeys) {
            outputs.add(context.makeIOFactory(outputKey.getSourceType())
                                        .<String>makeOutput(outputKey));
        }

        IOKey inputKey = inputKeys.get(0);
        Input<String> input =
            context.makeIOFactory(inputKey.getSourceType()).makeInput(inputKey);

        split(input, outputs);
    }
}
