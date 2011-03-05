package org.jdag.node;

import org.jdag.dag.InputSplitter;
import org.jdag.dag.SimpleInputSplitterFactory;
import org.jdag.dag.SimpleInputSplitterFactory.SplitterType;
import org.jdag.data.Function;

public class ReflectiveUDFFactory implements UDFFactory 
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputSplitter makeInputSplitter(String identifier) 
	{
		return SimpleInputSplitterFactory.makeSpliter(
				SplitterType.valueOf(identifier));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Function makeUDF(String identifier) 
	{
		try {
			return (Function) Class.forName(identifier).newInstance();
		} 
		catch (InstantiationException e) {
            throw new RuntimeException(e);
		} 
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} 
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
