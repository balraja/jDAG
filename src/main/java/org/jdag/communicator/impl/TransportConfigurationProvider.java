package org.jdag.communicator.impl;

import static org.hornetq.core.remoting.impl.netty.TransportConstants.PORT_PROP_NAME;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.HashMap;
import java.util.Map;

import org.hornetq.api.core.TransportConfiguration;

/**
 * Implements google provider to support transport configuration injection.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class TransportConfigurationProvider implements
    Provider<TransportConfiguration>
{
    private final CommunicatorConfig myConfig;

    /**
     * CTOR
     */
    @Inject
    public TransportConfigurationProvider(CommunicatorConfig config)
    {
        myConfig = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransportConfiguration get()
    {
        Map<String, Object> connectionParams = new HashMap<String, Object>();
        connectionParams.put(PORT_PROP_NAME, myConfig.getPortNumber());
        return new TransportConfiguration(
            "org.hornetq.core.remoting.impl.netty.NettyConnectorFactory",
            connectionParams);
    }
}
