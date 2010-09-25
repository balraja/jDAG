package org.jdryad.dag;

import java.io.Serializable;
import java.util.Set;

/**
 * Type that defines a function that gets shipped to a remote execution engine.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface UserDefinedFunction extends Serializable
{
    /** Defines a job that needs to be executed on a vertex */
    public boolean process(Set<IOKey> inputs,
                           Set<IOKey> outputs,
                           ExecutionContext graphContext);
}
