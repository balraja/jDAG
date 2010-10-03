package org.jdryad.dag.test;

import java.util.List;

import org.jdryad.dag.ExecutionContext;
import org.jdryad.dag.IOKey;
import org.jdryad.dag.UserDefinedFunction;

/**
 * A simple sum function that takes a list and sums the list
 *
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public class SumFunction implements UserDefinedFunction
{

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean process(List<IOKey> inputs, List<IOKey> outputs,
            ExecutionContext graphContext)
    {
        // TODO Auto-generated method stub
        return false;
    }

}
