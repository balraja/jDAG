package org.jdag.dsl;

import java.util.List;

import org.jdag.dsl.impl.ComputeFailedException;
import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOKey;

/**
 * Defines the contract for method that needs to be called in a vertex.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Executable
{
    public void execute(ExecutionContext context,
                                 List<IOKey> inputKeys,
                                 List<IOKey> outputKeys)
        throws ComputeFailedException;
}
