package org.jdryad.dag;

import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple task that splits a given big input into multiple small outputs.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class MapperFunction implements UserDefinedFunction
{
    private final InputSplitter mySplitter;

    /**
     * CTOR
     */
    public MapperFunction(InputSplitter splitter)
    {
        mySplitter = splitter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean process(List<IOKey> inputs, List<IOKey> outputs,
            ExecutionContext graphContext)
    {
        Preconditions.checkArgument(!inputs.isEmpty());
        Preconditions.checkArgument(!outputs.isEmpty());
        Preconditions.checkArgument(inputs.size() == 1);

        IOFactory ioFactory = graphContext.getIOFactory();
        IOKey inKey = inputs.iterator().next();
        FunctionInput in = ioFactory.makeInput(inputs.iterator().next());
        Map<IOKey, FunctionOutput> keyToOPMap = FunctionUtil.makeMap(outputs,
                graphContext);
        Iterator<Record> inItr = in.getIterator();
        int count = 1;
        while (inItr.hasNext()) {
            Record r = inItr.next();
            IOKey k = mySplitter.getOutput(r, count, inKey, outputs);
            if (k != null) {
                FunctionOutput output = keyToOPMap.get(k);
                if (output != null) {
                    output.write(r);
                }
            }
        }

        for (FunctionOutput output : keyToOPMap.values()) {
            output.done();
        }
        return true;
    }
}
