package org.jdag.io.flatfile;

import com.google.common.base.Preconditions;

import org.jdag.data.FunctionInput;
import org.jdag.data.FunctionOutput;
import org.jdag.io.IOFactory;
import org.jdag.io.IOKey;
import org.jdag.io.IOSource;

/**
 * Implements <code>IOFactory</code> that reads data from flat files.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FlatFileIOFactory implements IOFactory
{
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> FunctionInput<T> makeInput(IOKey key)
    {
        Preconditions.checkArgument(
            key.getSourceType() == IOSource.FILE_SYSTEM);

        try {
            Interpreter<T> lineInterpreter =
                (Interpreter<T>)
                    Class.forName(key.getAttribute(Interpreter.ATTRIBUTE_NAME))
                         .newInstance();
            return new FlatFileInput<T>(lineInterpreter, key.getIdentifier());
        }
        catch (InstantiationException e) {
             throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
             throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e) {
             throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> FunctionOutput<T> makeOutput(IOKey key)
    {
        Preconditions.checkArgument(
                key.getSourceType() == IOSource.FILE_SYSTEM);

                try {
            Interpreter<T> lineInterpreter =
                (Interpreter<T>)
                    Class.forName(key.getAttribute(Interpreter.ATTRIBUTE_NAME))
                         .newInstance();
            return new FlatFileOutput(lineInterpreter, key.getIdentifier());
        }
        catch (InstantiationException e) {
             throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
             throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e) {
             throw new RuntimeException(e);
        }
    }
}
