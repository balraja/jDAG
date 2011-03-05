package org.jdag.io.serialiazition;

import org.jdag.data.FunctionInput;
import org.jdag.data.FunctionOutput;
import org.jdag.io.IOFactory;
import org.jdag.io.IOKey;

/**
 * A simple class that takes key to the inputs or outputs and generates
 * appropriate file based input or outputs.
 * 
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FileIOFactory implements IOFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public FunctionInput makeInput(IOKey key)
    {
        return new FileInput(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FunctionOutput makeOutput(IOKey key)
    {
        return new FileOutput(key);
    }
}
