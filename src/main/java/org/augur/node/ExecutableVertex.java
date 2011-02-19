/**
 *
 */
package org.augur.node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.augur.communicator.messages.ExecuteVertexProtos.ExecuteVertexMessage;
import org.augur.dag.ExecutionContext;
import org.augur.dag.ExecutionResult;
import org.augur.dag.IOKey;
import org.augur.dag.InputSplitter;
import org.augur.dag.MapperFunction;
import org.augur.dag.SimpleVertex;
import org.augur.dag.IOSource;
import org.augur.dag.UDFFactory;
import org.augur.dag.UDFIdentityGenerator;
import org.augur.dag.UDF;
import org.augur.dag.VertexID;

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
		super(new VertexID(executeVertexMessage.getVertexId()),
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
			iokeyList.add(new IOKey(IOSource.getTypeByID(key.getTypeId()),
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
        UDF function = null;
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
        catch (Throwable e) {
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
