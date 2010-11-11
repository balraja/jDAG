package org.jdryad.dag.builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdryad.dag.IOSource;
import org.jdryad.dag.SimpleInputSplitterFactory.SplitterType;

/**
 * Type that abstracts out the characteristics of input to be processed.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class InputSpecification implements DataFragments
{
    private final String myIdentifier;

    private final Map<String, Object> myAttributes;

    private SplitterType mySplitter;

    private int myNumSplits;

    private String mySource;

    private IOSource myPersistenceSourceType;

    /**
     * CTOR
     */
    public InputSpecification(String identifier)
    {
        super();
        myIdentifier = identifier;
        myAttributes = new HashMap<String, Object>();
        mySplitter = null;
        myNumSplits = 0;
        myPersistenceSourceType = null;
    }

    /**
     * Sets the source from which attributes are loaded.
     */
    public InputSpecification fromSource(
       String sourceIdentifier,
       IOSource persistenceSourceType)
    {
        mySource = sourceIdentifier;
        myPersistenceSourceType = persistenceSourceType;
        return this;
    }

    /** Sets the attributes to the system */
    public InputSpecification setAttribute(String key, Object value)
    {
        myAttributes.put(key, value);
        return this;
    }

    /** Sets the number of splits */
    public InputSpecification splitInto(int numSplits)
    {
        myNumSplits = numSplits;
        return this;
    }

    /** Sets the splitter */
    public InputSpecification by(SplitterType splitterType)
    {
        mySplitter = splitterType;
        return this;
    }

    /**
     * Returns the value of identifier
     */
    public String getIdentifier()
    {
        return myIdentifier;
    }

    /** Returns the type of persistence medium from which data is to be read */
    public IOSource getPersistenceSourceType()
    {
        return myPersistenceSourceType;
    }

    /**
     * Returns the value of splitter
     */
    public SplitterType getSplitter()
    {
        return mySplitter;
    }

    /** Returns the file from which data is read */
    public String getSource()
    {
        return mySource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getFragementIds()
    {
        return new FragmentKeyList(myIdentifier, myNumSplits);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumFragments()
    {
        return myNumSplits;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFragemented()
    {
        return myNumSplits > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getFragmentSources()
    {
        return Collections.singletonList(myIdentifier);
    }
}
