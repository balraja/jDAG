package org.jdag.communicator.impl;

import com.google.inject.Provider;

import org.jdag.config.ConfigurationProvider;

/**
 * Implements <code>Provider</code> to create a proxy that can be used for
 * reading configuration information.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class CommunicatorConfigProvider
    implements Provider<CommunicatorConfig>
{
    private final CommunicatorConfig myCommunicatorConfig;

    /**
     * The property that defines the configuration file from which the
     * configuration information for comm is read
     */
    private static final String PROP_COMM_CONFIG_FILE =
        "org.jdag.commconfig.file";

    /**
     * The property that defines the configuration file from which the
     * configuration information for comm is read
     */
    private static final String DEFAULT_COMM_CONFIG_FILE =
        "comm.hornetq.properties";

    /**
     * CTOR
     */
    public CommunicatorConfigProvider()
    {
        String fileName =
            System.getProperty(PROP_COMM_CONFIG_FILE,
                               DEFAULT_COMM_CONFIG_FILE);
        myCommunicatorConfig =
            ConfigurationProvider.makeConfiguration(
                CommunicatorConfig.class, fileName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommunicatorConfig get()
    {
        return myCommunicatorConfig;
    }
}
