package org.jdag.node;

import com.google.inject.Provides;

import org.jdag.communicator.Communicator;
import org.jdag.communicator.impl.AbstractCommModule;
import org.jdag.communicator.impl.CommunicatorImpl;
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
        bind(Communicator.class).to(CommunicatorImpl.class);
    }

    @Provides
    public NodeConfig provideNodeConfig()
    {
        return
            ConfigurationProvider.makeConfiguration(
                NodeConfig.class, NODE_CONFIG_FILE);
    }
}
