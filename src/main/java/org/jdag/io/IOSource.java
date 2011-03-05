package org.jdag.io;

/**
 * An enum to define the medium in which data is stored.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public enum IOSource
{
    /**
     * Defines the case where data is stored in a file system.
     */
    FILE_SYSTEM,

    /**
     * Defines the case where data is stored in a relational database.
     */
    RELATIONAL_DB,

    /**
     * Defines the case where data is stored in a mongo db.
     */
    MONGO_DB
}
