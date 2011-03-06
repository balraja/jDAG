package org.jdag.io.flatfile;

import com.google.common.base.Preconditions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jdag.data.FunctionOutput;

/**
 * Implements <code>FunctionOutput</code> that writes data to a flat file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FlatFileOutput<T> implements FunctionOutput<T>
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
        Preconditions.checkArgument(f.exists() && f.canWrite());
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
        myWriter.println(myLineInterpreter.flattenRecord(r));
    }
}