package org.jdryad.dag.builder;

import java.util.Collections;
import java.util.List;

/**
 * A simple type that defines the fragments are to be executed in parallel.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class InParallel implements FunctionInputs
{
    private final DataFragments myFragments;

    /**
     * CTOR
     */
    public InParallel(DataFragments fragments)
    {
        myFragments = fragments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getFragmentSourcesFor(int index)
    {
        return Collections.singletonList(
            myFragments.getFragmentSources().get(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getInputAt(int index)
    {
        return Collections.singletonList(
            myFragments.getFragementIds().get(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumCombinations()
    {
        return myFragments.getNumFragments();
    }
}
