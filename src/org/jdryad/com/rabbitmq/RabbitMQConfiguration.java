package org.jdryad.com.rabbitmq;

/**
 * Type that defines the configuration data supposed to be used by
 * <code>RabbitMqCommunicator</code>
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface RabbitMQConfiguration
{
    public boolean shouldDeclareGlobalExchange();

    public String getGlobalExchange();

    public String getHostName();

    public String getHost();

    public int getPort();

    public String getVirtualHost();

    public String getUserName();

    public String getPassword();
}
