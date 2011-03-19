package org.jdag.example.wc;

import com.google.common.base.Functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdag.data.ComputeFailedException;
import org.jdag.data.Function;
import org.jdag.data.Input;
import org.jdag.data.Output;
import org.jdag.function.IteratorWrapper;
import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOKey;

/**
 * The function to be used for computing the number of records in a file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class CountWords implements Function<String, Map<String,Integer>>
{

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(Input<String> input,
                                 Output<Map<String, Integer>> output)
    {
         Map<String, Integer> wordcountMap = new HashMap<String, Integer>();
         com.google.common.base.Function<String,Integer> lookupFunction =
             Functions.forMap(wordcountMap, 0);
         for (String line : new IteratorWrapper<String>(input.getIterator())) {
             String[] words = line.split(" ");
             for (String word : words) {
                  int count = lookupFunction.apply(word);
                  wordcountMap.put(word, ++count);
             }
         }
         output.write(wordcountMap);
         output.done();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ExecutionContext context,
                                List<IOKey> inputKeys,
                                List<IOKey> outputKeys) throws ComputeFailedException
    {
        IOKey inputKey = inputKeys.get(0);
         Input<String> input =
             context.makeIOFactory(inputKey.getSourceType()).makeInput(inputKey);

         IOKey outputKey = outputKeys.get(0);
         Output<Map<String,Integer>> output =
             context.makeIOFactory(outputKey.getSourceType()).makeOutput(outputKey);

         process(input, output);
    }
}
