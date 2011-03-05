package org.jdag.io.flatfile;

/**
 * A typical interface to be used for reading records from a flat file
 * where the record data is stored in a line.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Interpreter<T>
{
    public static String ATTRIBUTE_NAME = "interpreter";

    /** Reads data from the given line and generates a record out of it */
    public T makeRecord(String line);

    /** Flattens the given record to a line in the file */
    public String flattenRecord(T r);

}
