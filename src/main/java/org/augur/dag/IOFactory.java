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
    /** A factory method for making the input corresponding to a task */
    public FunctionInput makeInput(IOKey key);

    /** A factory method for making output corresponding to a task */
    public FunctionOutput makeOutput(IOKey key);
}
