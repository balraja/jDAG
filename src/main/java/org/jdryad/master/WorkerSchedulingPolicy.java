package org.jdryad.master;

import org.jdryad.com.messages.UpAndLiveProtos.UpAndAliveMessage;
import org.jdryad.commmunicator.HostID;
import org.jdryad.dag.GraphVertexID;

/**
 * The type that defines the contract for scheduling vertices over the
 * worker nodes. This is the type that's responsible for selecting a node
 * to which a graph vertex has to be shipped for execution.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface WorkerSchedulingPolicy
{
    /**
     * Process the heart beats from the worker nodes.
     */
    public void process(UpAndAliveMessage upAndAliveMessage);

    /**
     * Returns the identifier for a worker node to which graph vertices
     * can be scheduled or null if a worker is not free.
     */
    public HostID getWorkerNode(GraphVertexID graphVertexID);

    /**
     * Removes the vertex to host mapping.
     */
    public void removeVertexToHostMapping(GraphVertexID graphVertexID);
}
