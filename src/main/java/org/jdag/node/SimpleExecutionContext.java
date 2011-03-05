package org.jdag.node;

import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOFactory;
import org.jdag.io.serialiazition.FileIOFactory;

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
}
