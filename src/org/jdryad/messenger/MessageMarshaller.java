package org.jdryad.messenger;

import java.io.IOException;

/**
 * An interface for marshalling/unmarshalling messages.
 *
 * @author subbiahb
 * @version $Id:$
 */
public interface MessageMarshaller
{
    /** Marshals message to bytes */
    public byte[] marshal(Message m) throws IOException;

    /** Unpacksbytes into a message */
    public Message unmarshal(byte[] m) throws IOException;

}
