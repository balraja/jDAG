package org.jdryad.messenger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * A default implementation of messenger with NIO.
 *
 * @author subbiahb
 * @version $Id:$
 */
public class Messenger extends Thread
{
    /** Size of the buffer */
    private static final int BUFFER_SIZE = 100;

    /** Logger */
    private static final Logger LOG =
        Logger.getLogger(Messenger.class.getName());

    /** Buffer for holding the send messages */
    private final List<SendMessageWrapper> myToBeSentList;

    /** Lock for send queue */
    private final Lock mySendLock;

    /** The address to which the messengers attaches itself */
    private final InetSocketAddress myAddress;

    /** Marshaller of the message */
    private final MessageMarshaller myMarshaller;

    /** Handler to denote received messages */
    private final ReceiveMessageHandler myReceiveMessageHandler;

    /** Warps message to be sent with the destination address */
    private class SendMessageWrapper
    {
        /** Message to be sent */
        private Message myMessage;

        /** Actual address to which we have to send that */
        private InetSocketAddress myAddress;

        /** CTOR */
        public SendMessageWrapper(
            Message message, InetSocketAddress address)
        {
            this.myMessage = message;
            this.myAddress = address;
        }

        // Getters and setters.

        public Message getMessage()
        {
            return myMessage;
        }

        public InetSocketAddress getAddress()
        {
            return myAddress;
        }
    }

    /**
     * AN implementation of {@link ReceiveMessageHandler} to put messages in
     * the quque.
     */
    private static class MessageQuqueHandler implements ReceiveMessageHandler
    {
        /** Message quque */
        private final MessageQuque myQuque;

        /** CTOR */
        public MessageQuqueHandler(MessageQuque quque)
        {
            myQuque = quque;
        }

        /** {@inheritDoc} */
        @Override
        public void messageReceived(Message m)
        {
            myQuque.addToQuque(m);
        }
    }

    /** CTOR */
    public Messenger(InetSocketAddress address,
                     MessageMarshaller marshaller,
                     MessageQuque quque)
    {
        this(address, marshaller, new MessageQuqueHandler(quque));
    }

    /** CTOR */
    public Messenger(InetSocketAddress address,
                     MessageMarshaller marshaller,
                     ReceiveMessageHandler receiveMessageHandler)
    {
        myMarshaller = marshaller;
        myReceiveMessageHandler = receiveMessageHandler;
        myAddress = address;
        mySendLock = new ReentrantLock();
        myToBeSentList = new ArrayList<SendMessageWrapper>();
        setDaemon(true);
    }

    /**
     * {@inheritDoc}
     */
    public void send(Message m, InetSocketAddress address)
    {
        try {
            mySendLock.lock();
            myToBeSentList.add(new SendMessageWrapper(m, address));
        }
        finally {
            mySendLock.unlock();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void run()
    {
        try {
            LOG.info("Binding Messenger to " + myAddress);
            final ServerSocketChannel receiver = ServerSocketChannel.open();
            // Bind the receiver to our address.
            receiver.socket().bind(myAddress);
            LOG.info("Binding successful");
            // Mark this as non-blocking channel.
            receiver.configureBlocking(false);

            // Open the selector.
            final Selector selector = Selector.open();
            receiver.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                try {
                    mySendLock.lock();
                    //LOG.info("Processing to be sent list");
                    for (SendMessageWrapper wrapper : myToBeSentList) {

                        InetSocketAddress address = wrapper.getAddress();
                        if (address == null) {
                            throw new RuntimeException(
                                "Destination not known ");
                        }

                        SocketChannel socketChannel =
                            SocketChannel.open(address);
                        LOG.info("Opening socket channel with " + address);
                        socketChannel.configureBlocking(false);
                        SelectionKey key =
                            socketChannel.register(
                                selector,
                                SelectionKey.OP_WRITE);
                        key.attach(wrapper.getMessage());
                    }
                    myToBeSentList.clear();
                }
                finally {
                    mySendLock.unlock();
                }

                final int n = selector.selectNow();
                if (n == 0) {
                    continue;
                }
                final Iterator<SelectionKey> iterator =
                    selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    LOG.info("Got some selection keys");
                    final SelectionKey key = iterator.next();

                    // Do perform accept.
                    if (key.isAcceptable()) {
                        LOG.info("Accepting a incoming connection");
                        final ServerSocketChannel server =
                            (ServerSocketChannel) key.channel();
                        final SocketChannel client = server.accept();
                        if (client != null) {
                            // should this be non-blocking ?
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                        }
                    }

                    // Do perform write.
                    if (key.isWritable()) {
                        LOG.info("Writing data to the connection");
                        final Message m = (Message) key.attachment();
                        final SocketChannel channel =
                            (SocketChannel) key.channel();
                        final ByteBuffer sendBuffer =
                            ByteBuffer.wrap(myMarshaller.marshal(m));
                        channel.write(sendBuffer);
                        channel.close();
                    }

                    // Do perform read.
                    if (key.isValid() && key.isReadable()) {
                        LOG.info("Reading data from the connection");
                        int count;
                        final SocketChannel channel =
                            (SocketChannel) key.channel();
                        final ByteBuffer buffer =
                            ByteBuffer.allocate(BUFFER_SIZE);
                        byte[] result = null;
                        int index = 0;
                        while ((count = channel.read(buffer)) > 0) {
                            buffer.flip();
                            final int length = buffer.remaining();
                            result = expand(result, length);
                            buffer.get(result, index, length);
                            index += length;
                            // Clear the buffer for subsequent reading.
                            buffer.clear();
                        }

                        if (result != null) {
                            final Message m =
                                myMarshaller.unmarshal(result);
                            myReceiveMessageHandler.messageReceived(m);
                        }

                        if (count == -1) {
                            channel.close();
                        }
                    }

                    // Remove the underlying key as it had been processed.
                    iterator.remove();
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException("I/o error", e);
        }
    }

    /** Expands the given array to a new size */
    private byte[] expand(byte[] array, int expansionSize)
    {
        if (array == null) {
            return new byte[expansionSize];
        }
        else {
            byte[] expandedArray =
                new byte[array.length + expansionSize];
            System.arraycopy(array, 0, expandedArray, 0, array.length);
            return expandedArray;
        }
    }
}
