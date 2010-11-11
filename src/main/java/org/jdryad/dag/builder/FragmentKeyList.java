package org.jdryad.dag.builder;

import java.util.ArrayList;

/**
 * A simple iterator that returns the keys corresponding to the fragements
 * of the data.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FragmentKeyList extends ArrayList<String>
{
    /**
     * CTOR
     */
    public FragmentKeyList(String dataName, int numFragements)
    {
        for (int i = 0; i < numFragements; i++) {
            add(dataName + "_" + i);
        }
    }
}
