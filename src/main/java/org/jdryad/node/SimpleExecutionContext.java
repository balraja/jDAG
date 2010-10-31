package org.jdryad.node;

import org.jdryad.dag.ExecutionContext;
import org.jdryad.dag.IOFactory;
import org.jdryad.dag.UDFFactory;
import org.jdryad.persistence.file.FileIOFactory;

public class SimpleExecutionContext implements ExecutionContext 
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IOFactory makeIOFactory() 
	{
		return new FileIOFactory();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UDFFactory makeUDFFactory() 
	{
		return new ReflectiveUDFFactory();
	}

}
