package org.jdag.graph;

import org.jdag.io.IOFactory;

/**
 * A simple class that encapsulates the <code>TaskGraphContext</code> for a
 * graph.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface ExecutionContext
{
    /**
     * Provides access to IOFactory that in turn be used for taskInputs and
     * task outputs.
     */
    public IOFactory makeIOFactory();
}
