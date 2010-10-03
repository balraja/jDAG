package org.jdryad.dag;

/**
 * Type that defines the o/p of a job that executed on a vertex.
 * 
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface FunctionOutput
{
    /** Writes the result to the o/p source */
    public void write(Record r);
    
    /** Notifies that we are done writing the data */
    public void done();
}
