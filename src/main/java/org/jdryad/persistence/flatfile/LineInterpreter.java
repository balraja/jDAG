package org.jdryad.persistence.flatfile;

import org.jdryad.dag.Record;

/**
 * A typical interface to be used for reading records from a flat file
 * where the record data is stored in a line.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface LineInterpreter
{
    public Record makeRecord(String line);

    public String flattenRecord(Record r);

}
