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

package org.jdag.common.persistentds;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jdag.common.log.LogFactory;

/**
 * Implements <code>MethodInterceptor</code> that intercepts calls to methods
 * annotated with <code>@Persist</code> and persists the changes to a
 * snapshot file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class PersistentDSManager implements MethodInterceptor
{

    private final PersistentDSManagerConfig myConfig;

    private final Map<String, PersistenceDSSession>
        myType2PersistenceSession;

    /**
     * CTOR
     */
    public PersistentDSManager(PersistentDSManagerConfig config)
    {
        myConfig = config;
        myType2PersistenceSession =
            new HashMap<String, PersistenceDSSession>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        Object result = invocation.proceed();
        Preconditions.checkState(
            PersistentDS.class.isAssignableFrom(
                invocation.getThis().getClass()));
       PersistentDS ds = (PersistentDS) invocation.getThis();
       PersistenceDSSession session =
            myType2PersistenceSession.get(ds.id());
        if (session == null) {
            session = new PersistenceDSSession(ds.id(), myConfig);
        }
        session.saveSnapshot(ds.makeSnapshot());
        return result;
    }

    /** Returns the snapshot for the given id */
    public Snapshot getSnapshot(String ID)
    {
        PersistenceDSSession session =
            myType2PersistenceSession.get(ID);
        if (session == null) {
            session = new PersistenceDSSession(ID, myConfig);
            myType2PersistenceSession.put(ID, session);
        }
        return session.loadSnapshot();
    }
}
