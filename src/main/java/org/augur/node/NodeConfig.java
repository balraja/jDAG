package org.augur.node;

import org.augur.config.ExpectedResult;
import org.augur.config.PropertyDef;
import org.augur.config.Source;

/**
 * A simple type that abstracts the configuration requirements of a node
 * that performs the job execution.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
@Source(prefix="augur.node")
public interface NodeConfig
{
    /**
     * Returns the name of the group via which the node details can be passed
     * on to the master.
     */
    @PropertyDef(name="masterHostID", resultType=ExpectedResult.STRING)
    public String getMasterHostID();

    /**
     * The interval abt which the heart beats are supposed to be sent to the
     * master.
     */
    @PropertyDef(name="heartBeatInterval", resultType=ExpectedResult.INT)
    public int getHeartBeatInterval();
}
