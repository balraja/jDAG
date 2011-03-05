package org.jdag.io.flatfile;

import com.google.common.base.Preconditions;

import java.util.List;

import org.jdag.common.Pair;
import org.jdag.dag.IOSource;
import org.jdag.data.FunctionInput;
import org.jdag.data.FunctionOutput;
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
    @Override
    public FunctionInput makeInput(IOKey key)
    {
        Preconditions.checkArgument(
            key.getSourceType() == IOSource.FLAT_FILE);
        Pair<String, List<Object>> decodedKey =
            key.getSourceType().getIOKeyCodec().getDecodedKey(
                key.getIdentifier());
        try {
            Interpreter lineInterpreter =
                (Interpreter)
                    Class.forName(decodedKey.getSecond().get(0).toString())
                         .newInstance();
            return new FlatFileInput(lineInterpreter, decodedKey.getFirst());
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
    @Override
    public FunctionOutput makeOutput(IOKey key)
    {
        Preconditions.checkArgument(
                key.getSourceType() == IOSource.FLAT_FILE);
        Pair<String, List<Object>> decodedKey =
            key.getSourceType().getIOKeyCodec().getDecodedKey(
                key.getIdentifier());
        try {
            Interpreter lineInterpreter =
                (Interpreter)
                    Class.forName(decodedKey.getSecond().get(0).toString())
                         .newInstance();
            return new FlatFileOutput(lineInterpreter, decodedKey.getFirst());
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
