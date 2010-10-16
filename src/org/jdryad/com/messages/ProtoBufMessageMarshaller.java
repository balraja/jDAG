package org.jdryad.com.messages;

import com.google.protobuf.InvalidProtocolBufferException;

import org.jdryad.com.Message;
import org.jdryad.com.MessageMarshaller;

/**
 * A simple message marshaller that translates
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ProtoBufMessageMarshaller implements MessageMarshaller
{
    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] marshal(Message message)
    {
        SimpleMessage wireMessage = (SimpleMessage) message;
        JDAGMessageType type = (JDAGMessageType) message.getMessageType();

        switch (type)  {
        case UP_AND_ALIVE_MESSAGE:
            UpAndLiveProtos.UpAndAliveMessage upAndAliveMessage =
                (UpAndLiveProtos.UpAndAliveMessage)
                    wireMessage.getPayload();
            WiredMessageProtos.WiredMessage wiredMessage =
                WiredMessageProtos.WiredMessage
                                  .newBuilder()
                                  .setMessageType(type.asInteger())
                                  .setPayload(upAndAliveMessage.toByteString())
                                  .build();

            return wiredMessage.toByteArray();
        }
        throw new IllegalArgumentException("Unknown message type " + type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message unmarshal(byte[] binaryMsg)
    {
        try {
            WiredMessageProtos.WiredMessage messageOnWire =
                WiredMessageProtos.WiredMessage.parseFrom(binaryMsg);
            JDAGMessageType type =
                JDAGMessageType.fromInteger(messageOnWire.getMessageType());
            Object payload = null;
            switch (type)  {
            case UP_AND_ALIVE_MESSAGE:
                payload =
                    UpAndLiveProtos.UpAndAliveMessage.parseFrom(
                        messageOnWire.getPayload());
            }
            return new SimpleMessage(type, payload);
        }
        catch (InvalidProtocolBufferException e) {
             throw new RuntimeException(e);
        }
    }
}
