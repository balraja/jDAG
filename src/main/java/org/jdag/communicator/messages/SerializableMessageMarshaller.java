package org.jdag.communicator.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jdag.communicator.Message;
import org.jdag.communicator.MessageMarshaller;

/**
 * Implements <code>MessageMarshaller</code> for serializing/deserializing the
 * messages.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class SerializableMessageMarshaller implements MessageMarshaller
{
    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] marshal(Message message) throws IOException
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);

        try {
            Serializable networkMessage = (Serializable) message;
            objectStream.writeObject(networkMessage);
            return byteStream.toByteArray();
        }
        finally {
            byteStream.close();
            objectStream.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message unmarshal(byte[] binaryMsg) throws IOException
    {
        ByteArrayInputStream byteStream =
            new ByteArrayInputStream(binaryMsg);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);

        try {
            return (Message) objectStream.readObject();
        }
        catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
        finally {
            byteStream.close();
            objectStream.close();
        }

    }

}
