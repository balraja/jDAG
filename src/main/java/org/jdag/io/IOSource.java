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
     * Defines the case where data is stored in a file system as serialized
     * java objects.
     */
    SERIALIZED_FILE,

    /**
     * Defines the case where data is stored in a file system as raw files.
     */
    FLAT_FILE
}
