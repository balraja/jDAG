package org.jdryad.dag.builder;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a combination of inputs to be processed by aligning them based on
 * the index.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class Index implements FunctionInputs
{
    private final DataFragments myDataFragments1;

    private final DataFragments myDataFragments2;

    /**
     * CTOR
     */
    public Index(DataFragments fragments1, DataFragments fragments2)
    {
        Preconditions.checkArgument(
            fragments1.getNumFragments()
                == fragments2.getNumFragments());
        myDataFragments1 = fragments1;
        myDataFragments2 = fragments2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getInputAt(int index)
    {
        ArrayList<String> parameterList = new ArrayList<String>();
        parameterList.add(myDataFragments1.getFragementIds().get(index));
        parameterList.add(myDataFragments2.getFragementIds().get(index));
        return parameterList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumCombinations()
    {
        return myDataFragments1.getNumFragments();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getFragmentSourcesFor(int index)
    {
        ArrayList<String> sourceList = new ArrayList<String>();
        sourceList.add(myDataFragments1.getFragmentSources().get(index));
        sourceList.add(myDataFragments2.getFragmentSources().get(index));
        return sourceList;
    }
}
