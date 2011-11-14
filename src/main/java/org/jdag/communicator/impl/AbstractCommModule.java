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

import com.google.inject.AbstractModule;

import org.hornetq.api.core.TransportConfiguration;
import org.jdag.communicator.MessageMarshaller;
import org.jdag.communicator.messages.SerializableMessageMarshaller;

/**
 * Implements guice <code>AbstractModule</code> to define bindings corresponding
 * to the dependency injection.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public abstract class AbstractCommModule extends AbstractModule
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure()
    {
        bind(MessageMarshaller.class).to(SerializableMessageMarshaller.class);
        bind(CommunicatorConfig.class).toProvider(CommunicatorConfigProvider.class);
        bind(TransportConfiguration.class).toProvider(TransportConfigurationProvider.class);
    }
}
