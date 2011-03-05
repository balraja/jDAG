package org.jdag.common.persistentds;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A marker annotation used for marking the methods that has to be intercepted
 * for persisting the changes after their invocation.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Persist
{
}
