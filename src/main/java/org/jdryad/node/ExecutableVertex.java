/**
 *
 */
package org.jdryad.node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.jdryad.com.messages.ExecuteVertexProtos.ExecuteVertexMessage;
import org.jdryad.dag.ExecutionContext;
import org.jdryad.dag.ExecutionResult;
import org.jdryad.dag.IOKey;
import org.jdryad.dag.InputSplitter;
import org.jdryad.dag.MapperFunction;
import org.jdryad.dag.SimpleVertex;
import org.jdryad.dag.UDFFactory;
import org.jdryad.dag.UDFIdentityGenerator;
import org.jdryad.dag.UserDefinedFunction;
import org.jdryad.dag.VertexID;
import org.jdryad.dag.IOKey.SourceType;

/**
 * Extends SimpleVertex to support the execution of a function.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ExecutableVertex extends SimpleVertex
    implements Callable<ExecutionResult>
{
	private final ExecutionContext myExecutionContext;

	/**
	 * CTOR
	 */
	public ExecutableVertex(ExecuteVertexMessage executeVertexMessage,
			                ExecutionContext executionContext)
	{
		super(new VertexID(""),
              executeVertexMessage.getUdfIdentifier(),
			  transform(executeVertexMessage.getInputsList()),
			  transform(executeVertexMessage.getOutputsList()));
		myExecutionContext = executionContext;
	}

	/**
	 * A simple utility method for mapping between IOKeys that comes as
	 * part of the message and the IOKeys that's exepected by the simple vertex.
	 */
	private static List<IOKey> transform(
	    List<ExecuteVertexMessage.IOKey> messageIOKeys)
	{
		ArrayList<IOKey> iokeyList = new ArrayList<IOKey>();
		for (ExecuteVertexMessage.IOKey key : messageIOKeys) {
			iokeyList.add(new IOKey(SourceType.getTypeByID(key.getTypeId()),
					                key.getIoIdentifier()));
		}
		return iokeyList;
	}


	/**
     * Executes the specified user defined function and returns the result.
     */
    public ExecutionResult execute(ExecutionContext context)
    {
        UDFFactory factory = context.makeUDFFactory();
        UDFIdentityGenerator identityGenerator = new UDFIdentityGenerator();
        UserDefinedFunction function = null;
        if (identityGenerator.isSimpleFunction(getUDFIdentifier())) {
            function =
                factory.makeUDF(
                    identityGenerator.getFunctionIdentifier(getUDFIdentifier())
                );
        }
        else {
            InputSplitter splitter =
                factory.makeInputSplitter(
                    identityGenerator.getMapperIdentifier(getUDFIdentifier()));
            function = new MapperFunction(splitter);
        }

        try {
            function.process(getInputs(), getOutputs(), context);
            return ExecutionResult.SUCCESS;
        }
        catch (Exception e) {
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
}
