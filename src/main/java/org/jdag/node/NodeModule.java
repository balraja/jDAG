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

package org.jdag.node;

import com.google.inject.Provides;

import org.jdag.communicator.Communicator;
import org.jdag.communicator.impl.AbstractCommModule;
import org.jdag.communicator.impl.HornetqCommunicator;
import org.jdag.config.ConfigurationProvider;

/**
 * Extends Google guice module to support dependency injection bindings.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class NodeModule extends AbstractCommModule
{
    private static final String NODE_CONFIG_FILE = "node.config.properties";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure()
    {
        super.configure();
        bind(Communicator.class).to(HornetqCommunicator.class);
    }

    @Provides
    public NodeConfig provideNodeConfig()
    {
        return
            ConfigurationProvider.makeConfiguration(
                NodeConfig.class, NODE_CONFIG_FILE);
    }
}
