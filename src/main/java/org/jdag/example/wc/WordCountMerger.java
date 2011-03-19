package org.jdag.example.wc;

import com.google.common.base.Functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdag.data.ComputeFailedException;
import org.jdag.data.Input;
import org.jdag.data.Output;
import org.jdag.data.Merger;
import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOKey;

/**
 * Merges the word count from multiple partitions into the total count for
 * each word.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class WordCountMerger implements Merger<Map<String,Integer>>
{

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(List<Input<Map<String, Integer>>> shards,
                              Output<Map<String, Integer>> output)
    {
        Map<String,Integer> resultMap = new HashMap<String, Integer>();
        com.google.common.base.Function<String,Integer> lookupFunction =
            Functions.forMap(resultMap, 0);

        for (Input<Map<String, Integer>> shard : shards) {
            Map<String,Integer> splitWcMap = shard.getIterator().next();
            for (Map.Entry<String, Integer> entry : splitWcMap.entrySet()) {
                resultMap.put(entry.getKey(),
                                   lookupFunction.apply(entry.getKey())
                                   + entry.getValue());
            }
        }
        output.write(resultMap);
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
         List<Input<Map<String,Integer>>> inputs =
             new ArrayList<Input<Map<String,Integer>>>();

         for (IOKey inputKey : inputKeys) {
             inputs.add(context.makeIOFactory(inputKey.getSourceType())
                                       .<Map<String,Integer>>makeInput(inputKey));
         }

         IOKey outputKey = outputKeys.get(0);
         Output<Map<String,Integer>> output =
             context.makeIOFactory(outputKey.getSourceType())
                        .makeOutput(outputKey);

         merge(inputs, output);
    }
}
