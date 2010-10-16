package org.jdryad.node;

import org.jdryad.config.Source;

/**
 * A simple type that abstracts the configuration requirements of a node
 * that performs the job execution.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
@Source(prefix="jdag.node")
public interface NodeConfig
{
    /** Returns the type of the communication factory to be used */
    public String getCommType();

    /**
     * Returns the name of the group via which the node details can be passed
     * on to the master.
     */
    public String getMasterHostID();

    /**
     * The interval abt which the heart beats are supposed to be sent to the
     * master.
     */
    public int getHeartBeatInterval();
}
