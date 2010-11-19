package org.augur.persistence.flatfile;

import com.google.common.base.Preconditions;

import java.util.List;

import org.augur.common.Pair;
import org.augur.dag.FunctionInput;
import org.augur.dag.FunctionOutput;
import org.augur.dag.IOFactory;
import org.augur.dag.IOKey;
import org.augur.dag.IOSource;

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
            LineInterpreter lineInterpreter =
                (LineInterpreter)
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
            LineInterpreter lineInterpreter =
                (LineInterpreter)
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
