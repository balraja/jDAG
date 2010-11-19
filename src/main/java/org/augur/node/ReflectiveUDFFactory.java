package org.augur.node;

import org.augur.dag.InputSplitter;
import org.augur.dag.SimpleInputSplitterFactory;
import org.augur.dag.UDFFactory;
import org.augur.dag.UserDefinedFunction;
import org.augur.dag.SimpleInputSplitterFactory.SplitterType;

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
