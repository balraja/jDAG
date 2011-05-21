package org.jdag.io;

import org.jdag.graph.GraphID;

/**
 * Mainly used for generating IOKey based on the persistence medium on which
 * intermediate data is stored.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface KeyGenerator
{
    /**
     * Returns an unique identifier that can be used for directly accessing the 
     * data corresponding to the local identifier in the file system.
     */
    public IOKey generateIdentifier(GraphID graphID, String localIdentifier);

    /**
     * Returns an unique identifier that can be used for directly accessing the 
     * data corresponding to the local identifier in the file system.
     */
    public IOKey generateFlatFileIdentifier(
        GraphID graphID, String localIdentifier, String interpreterClassName);
}
