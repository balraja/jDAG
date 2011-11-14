/*******************************************************************************
 * jDAG is a project to build acyclic dataflow graphs for processing massive datasets.
 *
 *     Copyright (C) 2011, Author: Balraja,Subbiah
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package org.jdag.io.flatfile;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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
    private String myInterpreterClassName;
    
    public FlatFileIOKey()
    {
        super();
        myInterpreterClassName = null;
    }

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
    
    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException
    {
        super.readExternal(in);
        boolean hasValue = in.readBoolean();
        if (hasValue) {
            myInterpreterClassName = in.readUTF();
        }
        else {
            myInterpreterClassName = null;
        }
    }
    
    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        super.writeExternal(out);
        if (myInterpreterClassName != null) {
            out.writeBoolean(true);
            out.writeUTF(myInterpreterClassName);
        }
        else {
            out.writeBoolean(false);
        }
    }
}
