package org.jdryad.dag;

import java.io.Externalizable;
import java.util.List;

/**
 * Defines the contract for a vertex that can be executed.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Vertex extends Externalizable
{
    /**
     * Returns <code>VertexID</code> of vertex.
     */
    public VertexID getID();

    /**
     * Returns the value of inputs
     */
    public List<IOKey> getInputs();

    /**
     * Returns the value of outputs
     */
    public List<IOKey> getOutputs();

    /**
     * Returns the unique identifier corresponding to a function that will be
     * executed as part of this vertex.
     */
    public String getUDFIdentifier();
}