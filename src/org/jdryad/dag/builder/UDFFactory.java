package org.jdryad.dag.builder;

import org.jdryad.dag.UserDefinedFunction;

/**
 * Factory for creating a user defined function.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface UDFFactory
{
    /** Generates a UserDefinedFunction from the function name */
    public UserDefinedFunction makeFunction(String fnSpecification);
}
