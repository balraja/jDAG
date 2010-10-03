package org.jdryad.dag.scheduler;

import org.jdryad.dag.Vertex;
import org.jdryad.dag.VertexID;

/**
 * Defines the contract for the scheduler.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface Scheduler
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
     * vertices when {@link Scheduler#getVertexForExecution()} is called
     * next.
     */
    public void notifyDone(VertexID vertexID);
}