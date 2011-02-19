package org.augur.dag;

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
    public <T extends Record> FunctionInput<T> makeInput(IOKey key);

    /** A factory method for making output corresponding to a task */
    public <T extends Record> FunctionOutput<T> makeOutput(IOKey key);
}
