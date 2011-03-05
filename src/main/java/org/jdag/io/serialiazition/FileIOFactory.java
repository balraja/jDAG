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
    public <T> FunctionInput<T> makeInput(IOKey key)
    {
        return new FileInput<T>(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> FunctionOutput<T> makeOutput(IOKey key)
    {
        return new FileOutput<T>(key);
    }
}
