package org.jdag.io;

import org.jdag.dsl.Input;
import org.jdag.dsl.Output;

/**
 * Type for denoting a factory that can be used for making inputs and outputs
 * corresponding for a function.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface IOFactory
{
    /** A factory method for making input corresponding to a task */
    public <T> Input<T> makeInput(IOKey key);

    /** A factory method for making output corresponding to a task */
    public <T> Output<T> makeOutput(IOKey key);
}
