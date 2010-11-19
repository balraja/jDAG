package org.augur.persistence.file;

import org.augur.dag.FunctionInput;
import org.augur.dag.FunctionOutput;
import org.augur.dag.IOFactory;
import org.augur.dag.IOKey;

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
