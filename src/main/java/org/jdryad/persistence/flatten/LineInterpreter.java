package org.jdryad.persistence.flatten;

import org.jdryad.dag.Record;

/**
 * A typical interface to be used for reading records from a file
 * where the record data is stored line by line.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface LineInterpreter
{
    public Record makeRecord(String line);

    public String flattenRecord(Record r);

}
