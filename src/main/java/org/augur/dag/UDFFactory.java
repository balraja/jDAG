package org.augur.dag;

/**
 * A factory for building user defined functions from their identifiers.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface UDFFactory
{
    /** Creates a user defined function from the given identifier */
    public UserDefinedFunction makeUDF(String identifier);

    /** Creates the <code>InputSplitter</code> for the given identifier */
    public InputSplitter makeInputSplitter(String identifier);
}
