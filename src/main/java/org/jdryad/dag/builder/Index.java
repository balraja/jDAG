package org.jdryad.dag.builder;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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
    private final List<String> myDataFragments1;

    private final List<String> myDataFragments2;

    /**
     * CTOR
     */
    public Index(DataFragments fragments1, DataFragments fragments2)
    {
        Preconditions.checkArgument(
            fragments1.getNumFragments()
                == fragments2.getNumFragments());
        myDataFragments1 = Lists.newArrayList(fragments1.getFragementIds());
        myDataFragments2 = Lists.newArrayList(fragments2.getFragementIds());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getInputAt(int index)
    {
        ArrayList<String> parameterList = new ArrayList<String>();
        parameterList.add(myDataFragments1.get(index));
        parameterList.add(myDataFragments2.get(index));
        return parameterList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumCombinations()
    {
        return myDataFragments1.size();
    }
}
