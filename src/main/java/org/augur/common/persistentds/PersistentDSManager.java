package org.augur.common.persistentds;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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

    private final Map<Class<? extends PersistentDS>, PersistenceDSSession>
        myType2PersistenceSession;

    /**
     * Type to be used for persisting the changes to a data structure.
     */
    public PersistentDSManager(PersistentDSManagerConfig config)
    {
        myConfig = config;
        myType2PersistenceSession =
            new HashMap<Class<? extends PersistentDS>, PersistenceDSSession>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        Object result = invocation.proceed();
        PersistentDS ds = (PersistentDS) invocation.getThis();
        PersistenceDSSession session =
            myType2PersistenceSession.get(ds.getClass());
        session.saveSnapshot(ds.makeSnapshot());
        return result;
    }
}
