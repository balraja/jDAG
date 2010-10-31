package org.jdryad.node;

import org.jdryad.dag.InputSplitter;
import org.jdryad.dag.SimpleInputSplitterFactory;
import org.jdryad.dag.UDFFactory;
import org.jdryad.dag.UserDefinedFunction;
import org.jdryad.dag.SimpleInputSplitterFactory.SplitterType;

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
	public UserDefinedFunction makeUDF(String identifier) 
	{
		try {
			return (UserDefinedFunction) Class.forName(identifier).newInstance();
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
