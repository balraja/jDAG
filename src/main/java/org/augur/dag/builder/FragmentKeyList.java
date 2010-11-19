package org.augur.dag.builder;

import java.util.ArrayList;

/**
 * A simple iterator that returns keys corresponding to the fragements
 * of data.
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
