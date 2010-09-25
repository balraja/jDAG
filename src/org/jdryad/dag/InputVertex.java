package org.jdryad.dag;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.List;

/**
 * A special implementation of vertex that acts as an input for the whole
 * system.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class InputVertex implements Vertex
{
    private Vertex myVertex;

    /**
     * CTOR
     */
    public InputVertex()
    {
    }

    /**
     * CTOR
     */
    public InputVertex(VertexID id,
                       IOKey input,
                       List<IOKey> outputs,
                       InputSplitter splitter)
    {
        myVertex =
            new SimpleVertex(id,
                             new MapperFunction(splitter),
                             Collections.<IOKey>singletonList(input),
                             outputs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutionResult execute(ExecutionContext context)
    {
        return myVertex.execute(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VertexID getID()
    {
        return myVertex.getID();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IOKey> getInputs()
    {
        return myVertex.getInputs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IOKey> getOutputs()
    {
        return myVertex.getOutputs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException
    {
        myVertex = (Vertex) in.readObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(myVertex);
    }
}
