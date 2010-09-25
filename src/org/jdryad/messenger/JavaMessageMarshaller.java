package org.jdryad.messenger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * An implementation of {@link MessageMarshaller} for marshalling
 * java messages.
 *
 * @author subbiahb
 * @version $Id:$
 */
public class JavaMessageMarshaller implements MessageMarshaller
{
    /** Magic number is used by the server to identify the type of client */
    private static final byte MAGIC_NUMBER = 1;

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] marshal(Message m) throws IOException
    {
        if (m instanceof NetworkMessage) {

            ByteArrayOutputStream byteStream =
                new ByteArrayOutputStream();
            ObjectOutputStream objectStream =
                new ObjectOutputStream(byteStream);

            try {
                NetworkMessage networkMessage = (NetworkMessage) m;
                objectStream.writeObject(networkMessage);
                return byteStream.toByteArray();
            }
            finally {
                byteStream.close();
                objectStream.close();
            }
        }
        else {
            throw new IllegalArgumentException("Unknown format");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message unmarshal(byte[] data) throws IOException
    {
        ByteArrayInputStream byteStream =
            new ByteArrayInputStream(data);
        ObjectInputStream objectStream =
            new ObjectInputStream(byteStream);

        try {
            return (NetworkMessage) objectStream.readObject();
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
