package org.augur.node;

import org.augur.dag.ExecutionContext;
import org.augur.dag.IOFactory;
import org.augur.dag.UDFFactory;
import org.augur.persistence.file.FileIOFactory;

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
