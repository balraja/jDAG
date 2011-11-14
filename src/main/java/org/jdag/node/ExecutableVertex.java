/*******************************************************************************
 * jDAG is a project to build acyclic dataflow graphs for processing massive datasets.
 *
 *     Copyright (C) 2011, Author: Balraja,Subbiah
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package org.jdag.node;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdag.common.log.LogFactory;
import org.jdag.dsl.Executable;
import org.jdag.graph.ExecutionContext;
import org.jdag.graph.ExecutionResult;
import org.jdag.graph.Vertex;
import org.jdag.graph.VertexID;
import org.jdag.io.IOKey;

/**
 * Extends SimpleVertex to support the execution of a function.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ExecutableVertex  implements Vertex, Callable<ExecutionResult>
{
    /** The logger */
    private final Logger LOG =  LogFactory.getLogger(ExecutableVertex.class);

    private final Vertex myVertex;

	private final ExecutionContext myExecutionContext;

	/**
	 * CTOR
	 */
	public ExecutableVertex()
	{
	    myVertex = null;
	    myExecutionContext = null;
	}

	/**
	 * CTOR
	 */
	public ExecutableVertex(Vertex vertex,
			                          ExecutionContext executionContext)
	{
	    myVertex = vertex;
		myExecutionContext = executionContext;
	}

	/**
     * Executes the specified user defined function and returns the result.
     */
    public ExecutionResult execute(ExecutionContext context)
    {
        try {
             Executable toBeExecuted =
                 (Executable) Class.forName(getUDFIdentifier()).newInstance();
             toBeExecuted.execute(context, getInputs(), getOutputs());
             return ExecutionResult.SUCCESS;
        }
        catch (Throwable e) {
            LOG.log(Level.SEVERE, "Execption while executing a vertex", e);
            return ExecutionResult.ERROR;
        }
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public ExecutionResult call() throws Exception
	{
		return execute(myExecutionContext);
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
    public String getUDFIdentifier()
    {
        return myVertex.getUDFIdentifier();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException
    {
        // Do Nothing
        
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        // Do Nothing
    }
}
