package org.augur.communicator.impl;

import org.augur.config.ExpectedResult;
import org.augur.config.PropertyDef;
import org.augur.config.Source;

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

    @PropertyDef(name="ququeName", resultType=ExpectedResult.STRING)
    public String getQuqueName();

}
