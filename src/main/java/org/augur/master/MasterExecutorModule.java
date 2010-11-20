package org.augur.master;

import com.google.inject.matcher.Matchers;

import org.augur.commmunicator.Communicator;
import org.augur.common.persistentds.Persist;
import org.augur.common.persistentds.PersistentDS;
import org.augur.common.persistentds.PersistentDSManagerAccessor;
import org.augur.communicator.impl.AbstractCommModule;
import org.augur.communicator.impl.CommunicatorImpl;

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
        bind(Communicator.class).to(CommunicatorImpl.class);
        bind(WorkerSchedulingPolicy.class).to(BoundedWorkerSchedulingPolicy.class);
        bindInterceptor(Matchers.subclassesOf(PersistentDS.class),
                        Matchers.annotatedWith(Persist.class),
                        PersistentDSManagerAccessor.getPersistentDSManager());
    }
}
