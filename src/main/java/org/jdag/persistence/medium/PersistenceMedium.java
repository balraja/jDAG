package org.jdag.persistence.medium;

import org.jdag.dag.IOSource;
import org.jdag.io.IOKey;

/**
 * Mainly used for generating IOKey based on the persistence medium on which
 * intermediate data is stored.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface PersistenceMedium
{
    public IOKey makeKey(String key, IOSource source);
}
