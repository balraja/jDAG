package org.jdryad.com.rabbitmq;

import org.jdryad.config.ExpectedResult;
import org.jdryad.config.PropertyDef;
import org.jdryad.config.Source;

/**
 * Type that defines the configuration data supposed to be used by
 * <code>RabbitMqCommunicator</code>
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
@Source(prefix="jdag.rabbitmq")
public interface RabbitMQConfiguration
{
    @PropertyDef(resultType=ExpectedResult.BOOLEAN, name="declareGlobalExchange")
    public boolean shouldDeclareGlobalExchange();

    @PropertyDef(resultType=ExpectedResult.STRING, name="globalExchange")
    public String getGlobalExchange();

    @PropertyDef(resultType=ExpectedResult.STRING, name="hostName")
    public String getHostName();

    @PropertyDef(resultType=ExpectedResult.STRING, name="host")
    public String getHost();

    @PropertyDef(resultType=ExpectedResult.INT, name="port")
    public int getPort();

    @PropertyDef(resultType=ExpectedResult.STRING, name="virtualhost")
    public String getVirtualHost();

    @PropertyDef(resultType=ExpectedResult.STRING, name="userName")
    public String getUserName();

    @PropertyDef(resultType=ExpectedResult.STRING, name="passwd")
    public String getPassword();
}
