package org.jdag.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to denote that the value of the configuration should be read from a property.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyDef
{
    public String name();

    public ExpectedResult resultType();
}
