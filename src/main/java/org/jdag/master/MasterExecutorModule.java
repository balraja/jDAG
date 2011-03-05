package org.jdag.master;

import com.google.inject.matcher.Matchers;

import org.jdag.commmunicator.Communicator;
import org.jdag.common.persistentds.Persist;
import org.jdag.common.persistentds.PersistentDS;
import org.jdag.common.persistentds.PersistentDSManagerAccessor;
import org.jdag.communicator.impl.AbstractCommModule;
import org.jdag.communicator.impl.CommunicatorImpl;

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
