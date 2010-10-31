package org.jdryad.dag.builder;

import java.util.HashSet;
import java.util.Set;

import org.jdryad.dag.SimpleInputSplitterFactory.SplitterType;

/**
 * A simple type that describes the task graph to be built.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class GraphSpecification
{
    /** The sources of inputs */
    private final Set<InputSpecification> myInputSpecs;

    /** The set of user defined functions */
    private final Set<UDFSpecification> myFunctions;

    /** CTOR */
    public GraphSpecification()
    {
        myInputSpecs = new HashSet<InputSpecification>();
        myFunctions = new HashSet<UDFSpecification>();
    }

    /** Adds input to the graph specification */
    public GraphSpecification addInput(String inputSrc,
                                       SplitterType splitter,
                                       int numSplits)
    {
        myInputSpecs.add(new InputSpecification(inputSrc, splitter, numSplits));
        return this;
    }

    /** Adds a specification for a function and its input */
    public GraphSpecification addUDFSpecification(
        String function, String alias, Set<String> inputs, boolean isPartial)
    {
        myFunctions.add(new UDFSpecification(function, alias, inputs, isPartial));
        return this;
    }

    /**
     * Returns the value of inputSpecs
     */
    public Set<InputSpecification> getInputSpecs()
    {
        return myInputSpecs;
    }

    /**
     * Returns the value of functions
     */
    public Set<UDFSpecification> getFunctions()
    {
        return myFunctions;
    }
}
