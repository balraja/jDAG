package org.jdryad.com.messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jdryad.com.Message;
import org.jdryad.com.MessageMarshaller;

/**
 * A simple message marshaller that translates
 *
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public class ProtoBufMessageMarshaller implements MessageMarshaller
{
    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] marshal(Message message)
    {
        try {
            JDryadMessageType type = (JDryadMessageType) message.getMessageType();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            switch (type)  {
            case UP_AND_ALIVE_MESSAGE:
                UpAndAlive upAndAlive = (UpAndAlive) message;
                UpAndLiveProtos.WiredUpAndAliveMessage wiredMessage =
                    UpAndLiveProtos.WiredUpAndAliveMessage
                                   .newBuilder()
                                   .setMessageType(type.asInteger())
                                   .setHostID(upAndAlive.getHostID().getIdentifier())
                                   .setAliveMillis(upAndAlive.getUpMillis())
                                   .build();
                wiredMessage.writeTo(bout);
                return bout.toByteArray();
            }
            return null;
        }
        catch (IOException e) {
             throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message unmarshal(byte[] binaryMsg)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
