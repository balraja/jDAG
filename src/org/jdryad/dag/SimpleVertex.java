package org.jdryad.dag;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

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

    private UserDefinedFunction myUserDefinedFunction;

    private List<IOKey> myInputs;

    private List<IOKey> myOutputs;

    /**
     * CTOR
     */
    public SimpleVertex()
    {
        myID = null;
        myUserDefinedFunction = null;
        myInputs = null;
        myOutputs = null;
    }

    /**
     * CTOR
     */
    public SimpleVertex(VertexID id, UserDefinedFunction userDefinedFunction,
            List<IOKey> inputs, List<IOKey> outputs)
    {
        myID = id;
        myUserDefinedFunction = userDefinedFunction;
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
    public UserDefinedFunction getUserDefinedFunction()
    {
        return myUserDefinedFunction;
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
     * Sets the value of userDefinedFunction.
     */
    public void setUserDefinedFunction(UserDefinedFunction userDefinedFunction)
    {
        myUserDefinedFunction = userDefinedFunction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException
    {
        myID = (VertexID) in.readObject();
        myUserDefinedFunction = (UserDefinedFunction) in.readObject();
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
        out.writeObject(myUserDefinedFunction);
        out.writeObject(myInputs);
        out.writeObject(myOutputs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionResult execute(ExecutionContext context)
    {
        // TODO fix null.
        myUserDefinedFunction.process(myInputs, myOutputs, context);
        return ExecutionResult.SUCCESS;
    }
}

