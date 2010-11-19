package org.augur.persistence.medium;

import org.augur.dag.IOKey;
import org.augur.dag.IOSource;

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
