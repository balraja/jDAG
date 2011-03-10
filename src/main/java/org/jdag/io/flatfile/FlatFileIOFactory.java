package org.jdag.io.flatfile;

import com.google.common.base.Preconditions;

import org.jdag.data.Input;
import org.jdag.data.Output;
import org.jdag.io.IOFactory;
import org.jdag.io.IOKey;

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
    public <T> Input<T> makeInput(IOKey key)
    {
        Preconditions.checkArgument(
            key instanceof FlatFileIOKey);

        try {
            FlatFileIOKey flatFileKey = (FlatFileIOKey) key;
            Interpreter<T> lineInterpreter =
                (Interpreter<T>) Class.forName(flatFileKey.getInterpreterClassName())
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
    public <T> Output<T> makeOutput(IOKey key)
    {
        Preconditions.checkArgument(
                key instanceof FlatFileIOKey);

        try {
            FlatFileIOKey flatFileKey = (FlatFileIOKey) key;
            if (flatFileKey.getInterpreterClassName() != null) {
                Interpreter<T> lineInterpreter =
                    (Interpreter<T>) Class.forName(flatFileKey.getInterpreterClassName())
                                                   .newInstance();
                return new FlatFileOutput(lineInterpreter, key.getIdentifier());
            }
            else {
                return new FlatFileOutput(null, flatFileKey.getIdentifier());
            }
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
