package org.jdryad.master;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.HashSet;
import java.util.Set;

import org.jdryad.com.HostID;
import org.jdryad.dag.ExecutionGraphID;
import org.jdryad.dag.VertexID;

/**
 * A simple scheduling policy that bounds the number of graph verteices that
 * can be executed on a node.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class BoundedWorkerSchedulingPolicy implements WorkerSchedulingPolicy
{
    private final Set<HostID> myWorkerNodes;

    private final SetMultimap<HostID, VertexID> myHostToVertexRegistry;

    /**
     * CTOR
     */
    public BoundedWorkerSchedulingPolicy()
    {
        myWorkerNodes = new HashSet<HostID>();
        myHostToVertexRegistry = HashMultimap.<HostID, VertexID>create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HostID getWorkerNode(ExecutionGraphID graphID,
                                VertexID vertexID);
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void process(UpAndAliveMessage upAndAliveMessage)
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeVertexToHostMapping(HostID host,
                                          ExecutionGraphID graphID,
                                          VertexID vertex)
    {
        // TODO Auto-generated method stub

    }

}
