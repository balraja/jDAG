package org.jdryad.dag.builder;

import java.util.List;

/**
 * A simple type that abstracts out the combinations of input to be processed
 * by the system. If there are more than one combination then each combination
 * will be processed in parallel.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface FunctionInputs
{
    /**
     * Returns the number of combinations of input to be processed by the
     * system.
     */
    public int getNumCombinations();

    /**
     * Returns the parameter combination at the given index.
     */
    public List<String> getInputAt(int index);

    /**
     * Returns the fragment sources for the input combination at given index.
     */
    public List<String> getFragmentSourcesFor(int index);
}
