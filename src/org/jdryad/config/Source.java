package org.jdryad.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to define the source of the configuration loaded.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Source
{
    public String prefix();
}
