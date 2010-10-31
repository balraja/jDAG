package org.jdryad.com;

/**
 * A factory for creating a message marshaller.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface MessageMarshallerFactory
{
    /**
     * Returns a <code>MessageMarshaller</code> to be used for
     * converting messages to the binary format.
     */
    public MessageMarshaller makeMarshaller();
}
