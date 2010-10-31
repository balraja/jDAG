package org.jdryad.dag;

/**
 * An enum to denote the final status of execution.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public enum ExecutionResult
{
    /** Denotes that execution has completed successfully */
    SUCCESS(1),

    /** Denotes that execution has failed */
    ERROR(2);

    private final int myIntegerRepresentation;

    /**
     * CTOR
     */
    private ExecutionResult(int integerRepresentation)
    {
        myIntegerRepresentation = integerRepresentation;
    }

    public int getIntegerRepresentation()
    {
        return myIntegerRepresentation;
    }

    public static ExecutionResult getExecutionResult(int integerRepresentation)
    {
        switch(integerRepresentation) {
        case 1: return ExecutionResult.SUCCESS;
        case 2: return ExecutionResult.ERROR;
        }
        return null;
    }
}
