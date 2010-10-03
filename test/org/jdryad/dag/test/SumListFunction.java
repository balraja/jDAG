package org.jdryad.dag.test;

import java.util.List;

import org.jdryad.dag.ExecutionContext;
import org.jdryad.dag.IOKey;
import org.jdryad.dag.UserDefinedFunction;

/**
 * A simple sum function that takes two lista and sums the list and writes the
 * output as a list.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public class SumListFunction implements UserDefinedFunction
{
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean process(List<IOKey> inputs,
                           List<IOKey> outputs,
                           ExecutionContext graphContext)
    {
        return false;
    }
}
