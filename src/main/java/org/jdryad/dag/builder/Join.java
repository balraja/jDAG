package org.jdryad.dag.builder;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a cross product of <code>DataFragements</code>. Ideal if the function
 * is to be applied on the cross product of a data set.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class Join implements FunctionInputs
{
    private final DataFragments myDataFragments1;

    private final DataFragments myDataFragments2;


    /**
     * CTOR
     */
    public Join(DataFragments fragment1, DataFragments fragment2)
    {
        myDataFragments1 = fragment1;
        myDataFragments2 = fragment2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getInputAt(int index)
    {
        ArrayList<String> parameterList = new ArrayList<String>();
        int frag1Index = index / myDataFragments2.getNumFragments();
        int frag2Index = index % myDataFragments2.getNumFragments();
        parameterList.add(myDataFragments1.getFragementIds().get(frag1Index));
        parameterList.add(myDataFragments2.getFragementIds().get(frag2Index));
        return parameterList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumCombinations()
    {
        return (myDataFragments1.getNumFragments()
                * myDataFragments2.getNumFragments());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getFragmentSourcesFor(int index)
    {
        List<String> sources1 = myDataFragments1.getFragmentSources();
        List<String> sources2 = myDataFragments2.getFragmentSources();

        Preconditions.checkState(sources1.size() == sources2.size());
        if (sources1.size() == 1) {
            return Lists.newArrayList(sources1.get(0), sources2.get(0));
        }
        else {
            int frag1Index = index / myDataFragments2.getNumFragments();
            int frag2Index = index % myDataFragments2.getNumFragments();
            return Lists.newArrayList(sources1.get(frag1Index),
                                      sources2.get(frag2Index));
        }
    }
}
