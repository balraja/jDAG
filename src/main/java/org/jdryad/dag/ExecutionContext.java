package org.jdryad.dag;

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

    /**
     * Returns the <code>UDFFactory<code> to be used for creating the
     * function that needs to be executed.
     */
    public UDFFactory makeUDFFactory();
}
