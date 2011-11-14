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

package org.jdag.communicator;

import java.io.IOException;

/**
 * Type that defines the protocol used for serializing/deserializing
 * the messages into a binary format.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface MessageMarshaller
{
    /** Marshals the <code>Message</code> to a byte stream */
    public byte[] marshal(Message message) throws IOException;

    /** Deserializes the byte stream to <code>Message</code> */
    public Message unmarshal(byte[] binaryMsg) throws IOException;
}
