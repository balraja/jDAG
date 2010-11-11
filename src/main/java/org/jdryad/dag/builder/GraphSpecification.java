package org.jdryad.dag.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jdryad.dag.UserDefinedFunction;

/**
 * A simple type that describes the task graph to be built.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class GraphSpecification
{
    /** The sources of inputs */
    private final Map<String, InputSpecification> myKey2InputMap;

    /** The set of user defined functions */
    private final Map<String, UDFSpecification> myKey2UDFMap;

    /** CTOR */
    public GraphSpecification()
    {
        myKey2InputMap = new HashMap<String, InputSpecification>();
        myKey2UDFMap = new HashMap<String, UDFSpecification>();
    }

    /** Adds input to the graph specification */
    public InputSpecification addInput(String inputIdentifier)
    {
        InputSpecification input = new InputSpecification(inputIdentifier);
        myKey2InputMap.put(inputIdentifier, input);
        return input;
    }

    /** Adds a specification for a function and its input */
    public UDFSpecification addUDF(
        String identifier,
        Class<? extends UserDefinedFunction> udfClass)
    {
        UDFSpecification udf =
            new UDFSpecification(identifier, udfClass);
        myKey2UDFMap.put(identifier, udf);
        return udf;
    }

    /**
     * Returns the value of inputSpecs
     */
    public Collection<InputSpecification> getInputSpecs()
    {
        return myKey2InputMap.values();
    }

    /**
     * Returns the value of functions
     */
    public Collection<UDFSpecification> getFunctions()
    {
        return myKey2UDFMap.values();
    }
}
