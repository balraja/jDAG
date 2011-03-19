package org.jdag.communicator.impl;

import org.jdag.config.ExpectedResult;
import org.jdag.config.PropertyDef;
import org.jdag.config.Source;

/**
 * A simple configuration for creating the communicator.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
@Source(prefix="jdag.communicator")
public interface CommunicatorConfig
{
    @PropertyDef(name="portNumber", resultType=ExpectedResult.INT)
    public int getPortNumber();

    @PropertyDef(name="clientName", resultType=ExpectedResult.STRING)
    public String getClientName();

}
