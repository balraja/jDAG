package org.jdag.io.serialiazition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.jdag.data.Output;
import org.jdag.io.IOKey;
import org.jdag.io.IOSource;
import org.jdag.io.PersistenceException;

/**
 * Implements <code>FunctionOutput</code> to read data from files where
 * <code>Record</code>s are stored ina serialization format.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FileOutput<T> implements Output<T>
{
    /** The random access file where data is stored */
    private final File myFile;

    /** The o/p stream to which the objects are written */
    private final ObjectOutputStream myObjectOut;

    /** CTOR */
    public FileOutput(IOKey key)
    {
        assert key.getSourceType() == IOSource.SERIALIZED_FILE;
        // For file sources the identifier corresponds to the file path.
        myFile = new File(key.getIdentifier());
        try {
            myObjectOut = new ObjectOutputStream(new FileOutputStream(myFile));
        }
        catch (FileNotFoundException e) {
             throw new PersistenceException(e);
        }
        catch (IOException e) {
             throw new PersistenceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(T r)
    {
        try {
            myObjectOut.writeObject(r);
            myObjectOut.writeInt(FileConstants.RECORD_DELIM);
        }
        catch (IOException e) {
             throw new PersistenceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void done()
    {
        try {
            myObjectOut.close();
        }
        catch (IOException e) {
             throw new PersistenceException(e);
        }
    }
}
