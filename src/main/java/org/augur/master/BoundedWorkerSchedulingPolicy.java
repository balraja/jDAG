package org.augur.master;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import java.util.Set;

import org.augur.commmunicator.HostID;
import org.augur.dag.GraphVertexID;

/**
 * A simple scheduling policy that says at a given point of time only one
 * vertex from a graph can be alive in a node. Also it also restricts the
 * number of active vertices that can be executed in a node.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class BoundedWorkerSchedulingPolicy implements WorkerSchedulingPolicy
{
    /**
     * {@inheritDoc}
     */
    @Override
    public HostID getWorkerNode(GraphVertexID graphVertexID,
                                ExecutionStateRegistry stateRegistry)
    {
        Set<HostID> workers = stateRegistry.getWorkers();
        if (workers.isEmpty()) {
            return null;
        }
        Set<HostID> currentWorkersForGraph =
            stateRegistry.getWorkersForGraph(graphVertexID.getGraphID());

        if (currentWorkersForGraph.isEmpty()) {
            return workers.iterator().next();
        }
        else {
            SetView<HostID> availableWorkers =
                Sets.difference(workers, currentWorkersForGraph);
            if (availableWorkers.isEmpty()) {
                return null;
            }
            else {
                return availableWorkers.iterator().next();
            }
        }
    }
}
