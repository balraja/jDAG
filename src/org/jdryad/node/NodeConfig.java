package org.jdryad.node;

import org.jdryad.config.Source;

/**
 * A simple type that abstracts the configuration requirements of a node
 * that performs the job execution.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
@Source(path="config/node.properties", prefix="jdag.rabbitmq")
public interface NodeConfig
{
    /** Returns the type of the communication factory to be used */
    public String getCommType();

    /**
     * Returns the name of the group via which the node details can be passed
     * on to the master.
     */
    public String getDiscoveryGroupName();
}
