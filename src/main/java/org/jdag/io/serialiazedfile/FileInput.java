package org.jdag.io.serialiazedfile;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.logging.Logger;

import org.jdag.common.log.LogFactory;
import org.jdag.dsl.Input;
import org.jdag.io.IOKey;
import org.jdag.io.PersistenceException;

/**
 * A simple class that encapsulates the input to a task that can be read from a
 * file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FileInput<T> implements Input<T>
{
    /** The logger */
    private static final Logger LOG =  LogFactory.getLogger(FileInput.class);

    private final IOKey myInputKey;

    /** A simple iterator that iterates over a file */
    private static class FileIterator<T> implements Iterator<T>
    {
        private ObjectInputStream myObjectStream;

        private T myReadRecord;

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
        @SuppressWarnings("unchecked")
        private T readRecord()
        {
            T result = null;
            try {
                result = (T) myObjectStream.readObject();
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
        public T next()
        {
            T result = myReadRecord;
            myReadRecord = readRecord();
            LOG.info("Read " + result);
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
    public Iterator<T> getIterator()
    {
        LOG.info("Reading from " + myInputKey.getIdentifier());
        return new FileIterator<T>(new File(myInputKey.getIdentifier()));
    }
}
