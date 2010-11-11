package org.jdryad.persistence.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.jdryad.dag.FunctionOutput;
import org.jdryad.dag.IOKey;
import org.jdryad.dag.Record;
import org.jdryad.dag.IOSource;
import org.jdryad.persistence.PersistenceException;

/**
 * 
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FileOutput implements FunctionOutput
{
    /** The random access file where data is stored */
    private final File myFile;
    
    /** The o/p stream to which the objects are written */
    private final ObjectOutputStream myObjectOut;
    
    /** CTOR */
    public FileOutput(IOKey key)
    {
        assert key.getSourceType() == PersistenceSource.IOSource;
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
    public void write(Record r)
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
