package org.augur.config;

/**
 * All methods in a configuration interface will be annotated by this.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public @interface PropertyDef
{
    public ExpectedResult resultType();

    public String name();
}
