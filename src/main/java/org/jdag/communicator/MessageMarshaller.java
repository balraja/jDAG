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
