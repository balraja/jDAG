package org.augur.dag.builder;

import java.util.Collections;
import java.util.List;

/**
 * Represents a FunctionOutput from the system.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FunctionOutput implements DataFragments
{
    private final String myIdentifier;

    private final UDFSpecification myUdfSpecification;

    /**
     * CTOR
     */
    public FunctionOutput(String identifier,
                          UDFSpecification udfSpecification)
    {
        myIdentifier = identifier;
        myUdfSpecification = udfSpecification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getFragementIds()
    {
        return new FragmentKeyList(
            myIdentifier,
            myUdfSpecification.getFunctionInputs().getNumCombinations());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumFragments()
    {
        return myUdfSpecification.getFunctionInputs().getNumCombinations();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFragemented()
    {
        return myUdfSpecification.getFunctionInputs().getNumCombinations() > 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getFragmentSources()
    {
        return myUdfSpecification.getFunctionInputs().getNumCombinations() > 1 ?
               myUdfSpecification.getParallelFunctionIds()
               : Collections.singletonList(myUdfSpecification.getIdentifier());
    }
}
