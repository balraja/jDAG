package org.jdag.io.flatfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.jdag.data.FunctionInput;
import org.jdag.graph.Record;

/**
 * Implements <code>FunctionInput</code> that reads data from a flat file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FlatFileInput implements FunctionInput
{
    private final Interpreter myLineInterpreter;

    private final File myFile;

    /**
     * A simple iterator that reads the file as input and transforms each line
     * to a <code>Record</code> using a <code>RecordFactory</code>
     */
    private static class FlattenFileIterator implements Iterator<Record>
    {
        private final BufferedReader myFileReader;

        private final Interpreter myLineInterpreter;

        private Record record = null;

        /**
         * CTOR
         */
        public FlattenFileIterator(File file, Interpreter factory)
        {
            try {
                myFileReader = new BufferedReader(new FileReader(file));
            }
            catch (FileNotFoundException e) {
                 throw new RuntimeException(e);
            }
            myLineInterpreter = factory;
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
        public Record next()
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
    public FlatFileInput(Interpreter recordFactory, String fileName)
    {
        myFile = new File(fileName);
        myLineInterpreter = recordFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Record> getIterator()
    {
        return new FlattenFileIterator(myFile, myLineInterpreter);
    }
}
