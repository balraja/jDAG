package org.jdag.data;

import java.io.Serializable;

import org.jdag.graph.ExecutionContext;

/**
 * Type that defines a function that gets shipped to a remote execution engine.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Function<I,O>
    extends Serializable
{
    /**
     * The definition of the function that needs to be executed on a vertex.
     */
    public boolean process(
            FunctionInput<I> input,
            FunctionOutput<O> output,
            ExecutionContext graphContext);
}
