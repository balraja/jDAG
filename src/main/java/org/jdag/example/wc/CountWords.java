package org.jdag.example.wc;

import com.google.common.base.Functions;

import java.util.HashMap;
import java.util.Map;
import org.jdag.data.Input;
import org.jdag.data.Output;
import org.jdag.function.FunctionBase;
import org.jdag.function.IteratorWrapper;

/**
 * The function to be used for computing the number of words in a file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class CountWords extends FunctionBase<String, Map<String,Integer>>
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
}
