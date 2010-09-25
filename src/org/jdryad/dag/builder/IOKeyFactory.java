package org.jdryad.dag.builder;

import org.jdryad.dag.IOKey;
import org.jdryad.dag.VertexID;

/**
 * Factory for creating IOKey from the given input src identifier or
 * from vertex identifiers.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface IOKeyFactory
{
    /** Generates IOKey from the given input src id and the vertex id */
    public IOKey makeKey(String ioSrc);

    /** Generates IOKey from the given input src id and the vertex id */
    public IOKey makeKey(String ioSrc, int numSplit);

    /** Generates IOKey for the data passed between src -> destn */
    public IOKey makeKey(VertexID src, int splitNum);
}
