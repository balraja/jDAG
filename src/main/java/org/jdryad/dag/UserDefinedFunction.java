package org.jdryad.dag;

import java.io.Serializable;
import java.util.List;

/**
 * Type that defines a function that gets shipped to a remote execution engine.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface UserDefinedFunction extends Serializable
{
    /** Defines a job that needs to be executed on a vertex */
    public boolean process(List<IOKey> inputs,
                           List<IOKey> outputs,
                           ExecutionContext graphContext);
}
