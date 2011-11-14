/*******************************************************************************
 * jDAG is a project to build acyclic dataflow graphs for processing massive datasets.
 *
 *     Copyright (C) 2011, Author: Balraja,Subbiah
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package org.jdag.example.wc;

import com.google.common.base.Functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdag.dsl.Input;
import org.jdag.dsl.Output;
import org.jdag.function.MergerBase;

/**
 * Merges the word count from multiple partitions into the total count for
 * each word.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class WordCountMerger extends MergerBase<Map<String,Integer>>
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
                              lookupFunction.apply(
                                  entry.getKey()) + entry.getValue());
            }
        }
        output.write(resultMap);
        output.done();
    }
}
