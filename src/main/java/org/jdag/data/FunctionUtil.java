package org.jdag.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOFactory;
import org.jdag.io.IOKey;

/**
 * A simple class that provides utility functions for function execution
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public final class FunctionUtil
{
    private FunctionUtil()
    {
    }

    /**
     * An utility method for creating a map from the given set of keys to
     * their <code>TaskOutput</code>
     */
    public static Map<IOKey, FunctionOutput> makeMap(
        List<IOKey> keys, ExecutionContext context)
    {
        HashMap<IOKey, FunctionOutput> outputMap =
            new HashMap<IOKey, FunctionOutput>();
        IOFactory ioFactory = context.makeIOFactory();
        for (IOKey key : keys) {
            outputMap.put(key, ioFactory.makeOutput(key));
        }
        return outputMap;
    }
}
