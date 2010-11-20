package org.augur.common.persistentds;

import org.augur.config.ExpectedResult;
import org.augur.config.PropertyDef;
import org.augur.config.Source;

/**
 * The configuration to be used by <code>PersistentDSManager</code>.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
@Source(prefix="augur.persistentds")
public interface PersistentDSManagerConfig
{
    @PropertyDef(name="rootDir", resultType=ExpectedResult.STRING)
    public String getRootDirectory();
}
