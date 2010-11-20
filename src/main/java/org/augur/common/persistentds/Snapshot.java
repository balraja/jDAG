package org.augur.common.persistentds;

import java.io.Externalizable;

/**
 * This is a marker interface used for defining a snapshot to be persisted
 * for recovering the data later.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Snapshot extends Externalizable
{
}
