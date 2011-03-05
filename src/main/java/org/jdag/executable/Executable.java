package org.jdag.executable;

import java.util.List;

import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOKey;

/**
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public interface Executable
{
    public void execute(ExecutionContext context,
                                 List<IOKey> inputKeys,
                                 List<IOKey> outputKeys);
}
