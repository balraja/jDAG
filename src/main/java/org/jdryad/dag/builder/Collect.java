package org.jdryad.dag.builder;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Ideal if used for collecting the fragments of a input source.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class Collect implements FunctionInputs
{
    private final DataFragments myFragments;

    /**
     * CTOR
     */
    public Collect(DataFragments fragments)
    {
        myFragments = fragments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getInputAt(int index)
    {
        Preconditions.checkArgument(index == 0);
        return myFragments.getFragementIds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumCombinations()
    {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getFragmentSourcesFor(int index)
    {
        Preconditions.checkArgument(index == 0);
        return myFragments.getFragmentSources();
    }
}
