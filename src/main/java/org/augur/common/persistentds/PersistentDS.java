package org.augur.common.persistentds;

/**
 * All the data structures, where every intermediate change to be persisted
 * has to implement this interface.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface PersistentDS
{
    /** Returns the identifier of the persistent data structure */
    public String ID();

    /** Returns the snapshot that has to be persisted */
    public Snapshot makeSnapshot();

    /** Should implement this method for initializing from the snapshot */
    public void initFromSnapshot(Snapshot snapshot);
}
