package org.augur.common.persistentds;

import org.augur.config.ConfigurationProvider;

/**
 * A singleton accessor class for getting <code>PersistentDSManager</code>
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public final class PersistenceManagerAccessor
{
    private static PersistentDSManager ourPersistentDSManager;

    /**
     * CTOR
     */
    private PersistenceManagerAccessor()
    {
    }

    public static PersistentDSManager getPersistentDSManager()
    {
        if (ourPersistentDSManager == null) {
            PersistentDSManagerConfig config =
                ConfigurationProvider.makeConfiguration(
                    PersistentDSManagerConfig.class, null);
            ourPersistentDSManager =
                new PersistentDSManager(config);
        }
        return ourPersistentDSManager;
    }
}
