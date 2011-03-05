package org.jdag.io;

import org.jdag.data.FunctionInput;
import org.jdag.data.FunctionOutput;

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
    public <T> FunctionInput<T> makeInput(IOKey key);

    /** A factory method for making output corresponding to a task */
    public <T> FunctionOutput<T> makeOutput(IOKey key);
}
