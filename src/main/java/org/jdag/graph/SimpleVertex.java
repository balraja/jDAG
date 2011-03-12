package org.jdag.graph;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
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
    private VertexID myID;

    private String myUDFIdentifier;

    private List<IOKey> myInputs;

    private List<IOKey> myOutputs;

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
    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException
    {
        myID = (VertexID) in.readObject();
        myUDFIdentifier = in.readUTF();
        myInputs = (List<IOKey>) in.readObject();
        myOutputs = (List<IOKey>) in.readObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(myID);
        out.writeObject(myUDFIdentifier);
        out.writeObject(myInputs);
        out.writeObject(myOutputs);
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
