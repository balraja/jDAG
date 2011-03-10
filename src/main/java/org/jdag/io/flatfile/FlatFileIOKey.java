package org.jdag.io.flatfile;

import org.jdag.io.IOKey;
import org.jdag.io.IOSource;

/**
 * Extends <code>IOKey</code> to add support for representing the
 * <code>Interpreter</code> used for serializing/deserializing the lines
 * in the file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FlatFileIOKey extends IOKey
{
    private final String myInterpreterClassName;

    public FlatFileIOKey(IOSource sourceType,
                               String identifier,
                               String interpreterClass)
    {
        super(sourceType, identifier);
        myInterpreterClassName = interpreterClass;
    }

    /**
     * Returns the value of interpreterClassName
     */
    public String getInterpreterClassName()
    {
        return myInterpreterClassName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime
                * result
                + ((myInterpreterClassName == null) ? 0
                        : myInterpreterClassName.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        FlatFileIOKey other = (FlatFileIOKey) obj;
        if (myInterpreterClassName == null) {
            if (other.myInterpreterClassName != null)
                return false;
        }
        else if (!myInterpreterClassName.equals(other.myInterpreterClassName))
            return false;
        return true;
    }

}
