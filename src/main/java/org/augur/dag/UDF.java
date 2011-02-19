package org.augur.dag;

import java.io.Serializable;

/**
 * Type that defines a function that gets shipped to a remote execution engine.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface UDF<I extends Record,O extends Record> extends Serializable
{
    /** Defines a job that needs to be executed on a vertex */
    public boolean process(
            FunctionInput<I> input,
            FunctionOutput<O> output,
            ExecutionContext graphContext);
}
