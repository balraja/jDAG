package org.jdag.dsl;

/**
 * Type that defines the o/p of a job that executed on a vertex.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Output<T>
{
    /** Writes the result to the o/p source */
    public void write(T record);

    /** Notifies that we are done writing the data */
    public void done();
}
