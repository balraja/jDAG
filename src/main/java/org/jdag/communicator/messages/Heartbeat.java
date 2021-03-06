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

package org.jdag.communicator.messages;

import java.io.Serializable;

import org.jdag.communicator.HostID;
import org.jdag.communicator.Message;

/**
 * This periodic message is sent from the clients to the server to notify whether a
 * client is alive or not.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class Heartbeat implements Serializable,Message
{
    private final HostID myNodeID;

    private final long myAliveMillis;

    /**
     * CTOR
     */
    public Heartbeat(HostID nodeID, long aliveMillis)
    {
        super();
        myNodeID = nodeID;
        myAliveMillis = aliveMillis;
    }

    /**
     * Returns the value of nodeID
     */
    public HostID getNodeID()
    {
        return myNodeID;
    }

    /**
     * Returns the value of aliveMillis
     */
    public long getAliveMillis()
    {
        return myAliveMillis;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + (int) (myAliveMillis ^ myAliveMillis >>> 32);
        result = prime * result
                + (myNodeID == null ? 0 : myNodeID.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Heartbeat other = (Heartbeat) obj;
        if (myAliveMillis != other.myAliveMillis) {
            return false;
        }
        if (myNodeID == null) {
            if (other.myNodeID != null) {
                return false;
            }
        }
        else if (!myNodeID.equals(other.myNodeID)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "Heartbeat [myAliveMillis=" + myAliveMillis + ", myNodeID="
                + myNodeID + "]";
    }
}
