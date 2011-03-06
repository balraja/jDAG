package org.jdag.node;

import org.jdag.graph.ExecutionContext;
import org.jdag.io.IOFactory;
import org.jdag.io.IOSource;
import org.jdag.io.flatfile.FlatFileIOFactory;
import org.jdag.io.serialiazition.FileIOFactory;

public class SimpleExecutionContext implements ExecutionContext
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IOFactory makeIOFactory(IOSource source)
	{
		return source == IOSource.FLAT_FILE ?
		    new FlatFileIOFactory() :  new FileIOFactory();
	}
}
