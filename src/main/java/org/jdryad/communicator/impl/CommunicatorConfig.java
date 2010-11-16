package org.jdryad.communicator.impl;

import org.jdryad.config.ExpectedResult;
import org.jdryad.config.PropertyDef;
import org.jdryad.config.Source;

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
