package org.jdag.dsl;

import java.io.Serializable;


/**
 * Type that defines a function that gets shipped to a remote execution engine.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Function<I,O>
    extends Serializable, Executable
{
    /**
     * The definition of the function that needs to be executed on a vertex.
     */
    public void process( Input<I> input,  Output<O> output);
}
