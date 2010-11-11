package org.jdryad.persistence.flatten;

import com.google.common.base.Preconditions;

import org.jdryad.dag.FunctionInput;
import org.jdryad.dag.FunctionOutput;
import org.jdryad.dag.IOFactory;
import org.jdryad.dag.IOKey;
import org.jdryad.dag.IOSource;

/**
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public class FlattenFileIOFactory implements IOFactory
{
    private static final String SEPERATOR = ":";

    /**
     * {@inheritDoc}
     */
    @Override
    public FunctionInput makeInput(IOKey key)
    {
        Preconditions.checkArgument(
                key.getSourceType() == IOSource.FLATTEN_FILE);
        String[] splits = key.getIdentifier().split(SEPERATOR);
        try {
            LineInterpreter lineInterpreter =
                (LineInterpreter) Class.forName(splits[0]).newInstance();
            return new FlattenFileInput(lineInterpreter, splits[1]);
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
                key.getSourceType() == IOSource.FLATTEN_FILE);
        String[] splits = key.getIdentifier().split(SEPERATOR);
        try {
            LineInterpreter lineInterpreter =
                (LineInterpreter) Class.forName(splits[0]).newInstance();
            return new FlattenFileOutput(lineInterpreter, splits[1]);
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
