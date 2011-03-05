package org.jdag.io.serialiazition;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;

import org.jdag.data.FunctionInput;
import org.jdag.graph.Record;
import org.jdag.io.IOKey;
import org.jdag.io.PersistenceException;

/**
 * A simple class that encapsulates the input to a task that can be read from a
 * file.
 * 
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FileInput implements FunctionInput
{
    private final IOKey myInputKey;
    
    /** A simple iterator that iterates over a file */
    private static class FileIterator implements Iterator<Record>
    {
        private ObjectInputStream myObjectStream;
        
        private Record myReadRecord;
        
        /**
         * CTOR
         */
        public FileIterator(File file)
        {
            try {
                assert file.exists() && file.canRead();
                myObjectStream = 
                    new ObjectInputStream(new FileInputStream(file));
            }
            catch (FileNotFoundException e) {
                 throw new PersistenceException(e);
            }
            catch (IOException e) {
                 throw new PersistenceException(e);
            }
            myReadRecord = readRecord();
        }
        
        /** A helper method for reading record from the file */
        private Record readRecord()
        {
            Record result = null;
            try {
                result = (Record) myObjectStream.readObject();
                int delim = myObjectStream.readInt();
                assert delim == FileConstants.RECORD_DELIM;
            }
            catch (EOFException e) {
                try {
                    myObjectStream.close();
                }
                catch (IOException e1) {
                     throw new PersistenceException(e1);
                }
            }
            catch (IOException e) {
                throw new PersistenceException(e);
            }
            catch (ClassNotFoundException e) {
                throw new PersistenceException(e);
            }
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext()
        {
            return myReadRecord != null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Record next()
        {
            Record result = myReadRecord;
            myReadRecord = readRecord();
            return result;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void remove()
        {
             throw new IllegalArgumentException("Remove is not supported");            
        }
    }
    
    /**
     * CTOR
     */
    public FileInput(IOKey inputKey)
    {
        myInputKey = inputKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Record> getIterator()
    {
        return new FileIterator(new File(myInputKey.getIdentifier()));
    }

}
