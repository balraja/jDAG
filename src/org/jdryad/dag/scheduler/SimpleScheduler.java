package org.jdryad.dag.scheduler;

import org.jdryad.dag.Vertex;
import org.jdryad.dag.VertexID;

/**
 * A simple type responsible for scheduling the part of job execution graph
 * on a cluster.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class SimpleScheduler implements Scheduler
{

    /**
     * {@inheritDoc}
     */
    @Override
    public Vertex getVertexForExecution()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDone(VertexID vertexID)
    {
        // TODO Auto-generated method stub

    }

}
