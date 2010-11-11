package org.jdryad.persistence.medium;

import org.jdryad.dag.IOKey;
import org.jdryad.dag.IOSource;

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
