package org.jdag.example.wc;

import org.jdag.io.flatfile.Interpreter;

/**
 * A simple interpreter which returns the line as is.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class SimpleInterpreter implements Interpreter<String>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String flattenRecord(String r)
    {
        return r;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String makeRecord(String line)
    {
        return line;
    }
}
