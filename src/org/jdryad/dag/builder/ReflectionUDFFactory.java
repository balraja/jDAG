package org.jdryad.dag.builder;

import org.jdryad.dag.UserDefinedFunction;

/**
 * A factory which builds <code>UserDefinedFunction<code> from the
 * function identifier provided based on reflection.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public class ReflectionUDFFactory implements UDFFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public UserDefinedFunction makeFunction(String fnSpecification)
    {
        try {
            UserDefinedFunction fn =
                (UserDefinedFunction)
                    Class.forName(fnSpecification).newInstance();
            return fn;
        }
        catch (InstantiationException e) {
            // TODO Auto-generated catch block
             throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
             throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
             throw new RuntimeException(e);
        }
    }
}
