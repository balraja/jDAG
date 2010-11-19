package org.augur.dag;

import com.google.common.base.Preconditions;

import org.augur.dag.SimpleInputSplitterFactory.SplitterType;

/**
 * A type to be used for generating an unique identity for the user defined
 * functions.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class UDFIdentityGenerator
{
    private final String SEPERATOR = ":";

    private final String MAPPER_FUNCTION_PREFIX = "mapper";

    private final String SIMPLE_FUNCTION_PREFIX = "simple";

    public String getUDFIdentifier(String functionIdentifier)
    {
        return SIMPLE_FUNCTION_PREFIX + SEPERATOR + functionIdentifier;
    }

    public String getMapperIdentifier(String splitterType)
    {
        return MAPPER_FUNCTION_PREFIX + SEPERATOR + splitterType;
    }

    public String getMapperIdentifier(SplitterType splitterType)
    {
        return MAPPER_FUNCTION_PREFIX + SEPERATOR + splitterType.name();
    }

    public boolean isSimpleFunction(String identifier)
    {
        return identifier.startsWith(SIMPLE_FUNCTION_PREFIX);
    }

    public String getFunctionIdentifier(String identifier)
    {
        Preconditions.checkArgument(isSimpleFunction(identifier),
                                    " Invalid didentifier " + identifier);
        return identifier.split(SEPERATOR)[1];
    }

    public boolean isMapperFunction(String identifier)
    {
        return identifier.startsWith(MAPPER_FUNCTION_PREFIX);
    }

    public String getSplitterIdentifier(String identifier)
    {
        Preconditions.checkArgument(isMapperFunction(identifier),
                                    " Invalid didentifier " + identifier);
        return identifier.split(SEPERATOR)[1];
    }
}
