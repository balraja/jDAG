package org.augur.dag.builder;

import java.util.List;

/**
 * A simple interface to represent the partitioned data.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface DataFragments
{
    /** Returns true if the data is fragmented */
    public boolean isFragemented();

    /** Returns the number of fragments */
    public int getNumFragments();

    /** Returns the list of fragment identifiers */
    public List<String> getFragementIds();

    /** Returns the list of fragment sources */
    public List<String> getFragmentSources();
}
