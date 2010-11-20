package org.augur.common.persistentds;

import org.augur.config.ConfigurationProvider;

/**
 * A singleton accessor class for getting <code>PersistentDSManager</code>.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public final class PersistentDSManagerAccessor
{
    /**
     * Cached instance.
     */
    private static PersistentDSManager ourPersistentDSManager;

    /**
     * CTOR
     */
    private PersistentDSManagerAccessor()
    {
    }

    /** Returns an instance of PersistenceDSManager */
    public static PersistentDSManager getPersistentDSManager()
    {
        if (ourPersistentDSManager == null) {
            PersistentDSManagerConfig config =
                ConfigurationProvider.makeConfiguration(
                    PersistentDSManagerConfig.class, null);
            ourPersistentDSManager = new PersistentDSManager(config);
        }
        return ourPersistentDSManager;
    }
}
