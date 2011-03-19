package org.jdag.graph;

import java.util.List;

import org.jdag.io.IOKey;

/**
 * A simple type that encapsulates the <code>UserDefinedFunction</code> to be
 * executed on a remote engine.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class SimpleVertex implements Vertex
{
    private final VertexID myID;

    private final String myUDFIdentifier;

    private final List<IOKey> myInputs;

    private final List<IOKey> myOutputs;

    /**
     * CTOR
     */
    public SimpleVertex()
    {
        myID = null;
        myUDFIdentifier = null;
        myInputs = null;
        myOutputs = null;
    }

    /**
     * CTOR
     */
    public SimpleVertex(VertexID id, String udfIdentifier,
            List<IOKey> inputs, List<IOKey> outputs)
    {
        myID = id;
        myUDFIdentifier = udfIdentifier;
        myInputs = inputs;
        myOutputs = outputs;
    }

    /**
     * Returns the value of iD
     */
    public VertexID getID()
    {
        return myID;
    }

    /**
     * Returns the value of userDefinedFunction
     */
    public String getUDFIdentifier()
    {
        return myUDFIdentifier;
    }

    /**
     * Returns the value of inputs
     */
    public List<IOKey> getInputs()
    {
        return myInputs;
    }

    /**
     * Returns the value of outputs
     */
    public List<IOKey> getOutputs()
    {
        return myOutputs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "SimpleVertex [myID=" + myID + ", myInputs=" + myInputs
                + ", myOutputs=" + myOutputs + ", myUDFIdentifier="
                + myUDFIdentifier + "]";
    }
}
