package org.jdag.common.persistentds;

import org.jdag.config.ExpectedResult;
import org.jdag.config.PropertyDef;
import org.jdag.config.Source;

/**
 * The configuration to be used by <code>PersistentDSManager</code>.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
@Source(prefix="jdag.persistentds")
public interface PersistentDSManagerConfig
{
    @PropertyDef(name="rootDir", resultType=ExpectedResult.STRING)
    public String getRootDirectory();
}
