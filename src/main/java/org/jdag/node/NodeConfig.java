/*******************************************************************************
 * jDAG is a project to build acyclic dataflow graphs for processing massive datasets.
 *
 *     Copyright (C) 2011, Author: Balraja,Subbiah
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package org.jdag.node;

import org.jdag.config.ExpectedResult;
import org.jdag.config.PropertyDef;
import org.jdag.config.Source;

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
