package org.augur.master;

import org.augur.commmunicator.Communicator;
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
    }
}
