package org.jdag.io.flatfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jdag.data.Output;

/**
 * Implements <code>FunctionOutput</code> that writes data to a flat file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FlatFileOutput<T> implements Output<T>
{
    private final PrintWriter myWriter;

    private final Interpreter<T> myLineInterpreter;

    /**
     * CTOR
     */
    public FlatFileOutput(Interpreter<T> lineInterpreter, String fileName)
    {
        myLineInterpreter = lineInterpreter;
        File f = new File(fileName);

        try {
            myWriter =
                new PrintWriter(new BufferedWriter(new FileWriter(f)));
        }
        catch (IOException e) {
             throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void done()
    {
        myWriter.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(T r)
    {
        String line = myLineInterpreter != null ?
                          myLineInterpreter.flattenRecord(r)
                          : r.toString();
        myWriter.println(line);
    }
}
