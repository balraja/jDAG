package org.jdryad.master;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jdryad.com.HostID;
import org.jdryad.com.messages.UpAndLiveProtos.UpAndAliveMessage;
import org.jdryad.dag.ExecutionGraphID;
import org.jdryad.dag.GraphVertexID;

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
    private final Set<HostID> myWorkerNodes;

    private final SetMultimap<ExecutionGraphID, HostID> myGraph2HostRegistry;

    private final Map<GraphVertexID, HostID> myVertex2HostMap;

    /**
     * CTOR
     */
    public BoundedWorkerSchedulingPolicy()
    {
        myWorkerNodes = new HashSet<HostID>();
        myGraph2HostRegistry = HashMultimap.<ExecutionGraphID, HostID>create();
        myVertex2HostMap = new HashMap<GraphVertexID, HostID>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HostID getWorkerNode(GraphVertexID graphVertexID)
    {
        if (myWorkerNodes.isEmpty()) {
            return null;
        }
        Set<HostID> hosts = myGraph2HostRegistry.get(graphVertexID.getGraphID());
        if (hosts.isEmpty()) {
            HostID host = myWorkerNodes.iterator().next();
            myGraph2HostRegistry.put(graphVertexID.getGraphID(), host);
            myVertex2HostMap.put(graphVertexID, host);
            return host;
        }
        else {
            HashSet<HostID> workers = new HashSet<HostID>(myWorkerNodes);
            workers.removeAll(hosts);
            if (workers.isEmpty()) {
                return null;
            }
            else {
                HostID host = workers.iterator().next();
                myGraph2HostRegistry.put(graphVertexID.getGraphID(), host);
                myVertex2HostMap.put(graphVertexID, host);
                return host;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(UpAndAliveMessage upAndAliveMessage)
    {
        HostID hostID = new HostID(upAndAliveMessage.getIdentifier());
        if (!myWorkerNodes.contains(hostID)) {
            myWorkerNodes.add(hostID);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeVertexToHostMapping(GraphVertexID graphVertexID)
    {
        HostID host = myVertex2HostMap.get(graphVertexID);
        myGraph2HostRegistry.remove(graphVertexID.getGraphID(), host);
    }
}
