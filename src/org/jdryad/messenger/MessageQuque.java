package org.jdryad.messenger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An implementation of message quque.
 *
 * @author subbiahb
 * @version $Id:$
 */
public class MessageQuque
{
	 /** Lock for the quque */
	 private final Lock myQuqueLock;

	 /** Condition to denote that quque is empty */
	 private final Condition myQuqueNotEmpty;

	 /** Buffer for the incoming message */
     private final Queue<Message> myQuque;

     /** CTOR */
     public MessageQuque()
     {
    	 myQuqueLock = new ReentrantLock();
    	 myQuqueNotEmpty = myQuqueLock.newCondition();
    	 myQuque = new LinkedList<Message>();
     }

     /** Adds message to the quque if message maps the criterion */
     public void addToQuque(Message m)
     {
         try {
    	     myQuqueLock.lock();
    	     myQuque.add(m);
    	     myQuqueNotEmpty.signal();
    	 }
    	 finally {
             myQuqueLock.unlock();
         }
     }

     /** Gets message at the top of the quque */
     public Message getFromTop() throws InterruptedException
     {
    	 try {
	         myQuqueLock.lock();
	         if (myQuque.isEmpty()) {
	             myQuqueNotEmpty.await();
	         }
	         return myQuque.poll();
		 }
		 finally {
			 myQuqueLock.unlock();
		 }
     }
}
