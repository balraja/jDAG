package org.jdag.io.flatfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.jdag.dsl.Input;

/**
 * Implements <code>FunctionInput</code> that reads data from a flat file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FlatFileInput<T> implements Input<T>
{
    private final Interpreter<T> myLineInterpreter;

    private final File myFile;

    /**
     * A simple iterator that reads the file as input and transforms each line
     * to a <code>Record</code> using a <code>RecordFactory</code>
     */
    private static class FlattenFileIterator<T> implements Iterator<T>
    {
        private final BufferedReader myFileReader;

        private final Interpreter<T> myLineInterpreter;

        private T record = null;

        /**
         * CTOR
         */
        public FlattenFileIterator(File file, Interpreter<T> interpreter)
        {
            try {
                myFileReader = new BufferedReader(new FileReader(file));
            }
            catch (FileNotFoundException e) {
                 throw new RuntimeException(e);
            }
            myLineInterpreter = interpreter;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext()
        {
            try {
                String line = myFileReader.readLine();
                record = line != null ? myLineInterpreter.makeRecord(line) : null;
                return record != null;
            }
            catch (IOException e) {
                 throw new RuntimeException(e);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public T next()
        {
            return record;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void remove()
        {
            throw new IllegalArgumentException();
        }
    }

    /**
     * CTOR
     */
    public FlatFileInput(Interpreter<T> recordFactory, String fileName)
    {
        myFile = new File(fileName);
        myLineInterpreter = recordFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> getIterator()
    {
        return new FlattenFileIterator<T>(myFile, myLineInterpreter);
    }
}
