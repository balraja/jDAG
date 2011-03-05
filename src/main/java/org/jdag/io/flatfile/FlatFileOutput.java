package org.jdag.io.flatfile;

import com.google.common.base.Preconditions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jdag.data.FunctionOutput;
import org.jdag.graph.Record;

/**
 * Implements <code>FunctionOutput</code> that writes data to a flat file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FlatFileOutput implements FunctionOutput
{
    private final PrintWriter myWriter;

    private final Interpreter myLineInterpreter;

    /**
     * CTOR
     */
    public FlatFileOutput(Interpreter lineInterpreter, String fileName)
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
    public void write(Record r)
    {
        myWriter.println(myLineInterpreter.flattenRecord(r));
    }
}
