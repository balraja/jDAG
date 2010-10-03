package org.jdryad.dag.builder;

import org.jdryad.dag.InputSplitterFactory.SplitterType;

/**
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public class InputSpecification
{
    private final String myIdentifier;

    private final SplitterType mySplitter;

    private final int numSplits;

    /**
     * CTOR
     */
    public InputSpecification(String identifier, SplitterType splitter,
            int numSplits)
    {
        super();
        myIdentifier = identifier;
        mySplitter = splitter;
        this.numSplits = numSplits;
    }

    /**
     * Returns the value of identifier
     */
    public String getIdentifier()
    {
        return myIdentifier;
    }

    /**
     * Returns the value of splitter
     */
    public SplitterType getSplitter()
    {
        return mySplitter;
    }

    /**
     * Returns the value of numSplits
     */
    public int getNumSplits()
    {
        return numSplits;
    }
}
