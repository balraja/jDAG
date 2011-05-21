package org.jdag.io.flatfile;

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
