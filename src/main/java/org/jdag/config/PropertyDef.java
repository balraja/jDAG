package org.jdag.config;

/**
 * All methods in a configuration interface will be annotated by this.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public @interface PropertyDef
{
    /** The type to which the data has to be parsed to */
    public ExpectedResult resultType();

    /** The name of the property */
    public String name();
}
