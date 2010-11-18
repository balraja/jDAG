package org.jdryad.master;

import org.jdryad.commmunicator.Communicator;
import org.jdryad.communicator.impl.AbstractCommModule;
import org.jdryad.communicator.impl.CommunicatorImpl;

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
