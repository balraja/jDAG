package org.jdryad.com.messages;

import org.jdryad.com.MessageMarshaller;
import org.jdryad.com.MessageMarshallerFactory;

/**
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ProtobufMessageMarshallerFactory implements
        MessageMarshallerFactory
{

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageMarshaller makeMarshaller()
    {
        return new ProtoBufMessageMarshaller();
    }

}
