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

package org.jdag.master;

import com.google.inject.matcher.Matchers;

import org.jdag.common.persistentds.Persist;
import org.jdag.common.persistentds.PersistentDS;
import org.jdag.common.persistentds.PersistentDSManagerAccessor;
import org.jdag.communicator.Communicator;
import org.jdag.communicator.impl.AbstractCommModule;
import org.jdag.communicator.impl.HornetqCommunicator;
import org.jdag.graph.scheduler.BoundedWorkerSchedulingPolicy;
import org.jdag.graph.scheduler.WorkerSchedulingPolicy;

/**
 * Implements GUICE module to support dependency injection.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class MasterExecutorModule extends AbstractCommModule
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void configure()
    {
        super.configure();
        bind(Communicator.class).to(HornetqCommunicator.class);
        bind(WorkerSchedulingPolicy.class).to(
                BoundedWorkerSchedulingPolicy.class);
        bind(ExecutionRegistry.class);
        bindInterceptor(Matchers.subclassesOf(PersistentDS.class),
                        Matchers.annotatedWith(Persist.class),
                        PersistentDSManagerAccessor.getPersistentDSManager());
    }
}
