package org.jdag.graph.scheduler;

import java.io.Externalizable;

import org.jdag.graph.Vertex;
import org.jdag.graph.VertexID;

/**
 * Defines the contract for the scheduler.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Schedule extends Externalizable
{
    /**
     * When called returns the next vertex to be executed. If all the
     * vertices are waiting for some previously scheduled vertices
     * then it will return null.
     */
    public Vertex getVertexForExecution();

    /**
     * Notifies that the given vertex id has been successfully executed.
     * Based on these notifications that the scheduler will give appropriate
     * vertices when {@link Schedule#getVertexForExecution()} is called
     * next.
     */
    public void notifyDone(VertexID vertexID);

    /** Returns true if the complete graph has been scheduled */
    public boolean isCompleted();
}