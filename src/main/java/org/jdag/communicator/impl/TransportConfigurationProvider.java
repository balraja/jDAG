/*******************************************************************************
 * jDAG is a project to build acyclic dataflow graphs for processing massive datasets.
 *
 *     Copyright (C) 2011, Author: Balraja,Subbiah
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

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
