package org.jdag.master;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;
import java.util.Set;

import org.jdag.common.persistentds.Snapshot;
import org.jdag.communicator.HostID;
import org.jdag.graph.GraphID;
import org.jdag.graph.VertexID;
import org.jdag.graph.scheduler.VertexSchedule;

/**
 * The state that needs to be persisted.
 */
public class ExecutionRegistryState implements Snapshot
{
    private Set<HostID> myWorkerNodes;

    private Map<VertexID, HostID> myVertex2HostMap;

    private Map<GraphID, VertexSchedule> myGraph2ScheduleMap;

    /**
     * CTOR
     */
    public ExecutionRegistryState()
    {
        myWorkerNodes = null;
        myVertex2HostMap = null;
        myGraph2ScheduleMap = null;
    }

    /** CTOR */
    public ExecutionRegistryState(
        Set<HostID> workerNodes,
        Map<VertexID, HostID> vertex2HostMap,
        Map<GraphID, VertexSchedule> graph2ScheduleMap)
    {
        super();
        myWorkerNodes = workerNodes;
        myVertex2HostMap = vertex2HostMap;
        myGraph2ScheduleMap = graph2ScheduleMap;
    }

    /**
     * Returns the value of workerNodes
     */
    public Set<HostID> getWorkerNodes()
    {
        return myWorkerNodes;
    }

    /**
     * Returns the value of vertex2HostMap
     */
    public Map<VertexID, HostID> getVertex2HostMap()
    {
        return myVertex2HostMap;
    }

    /**
     * Returns the value of graph2ScheduleMap
     */
    public Map<GraphID, VertexSchedule> getGraph2ScheduleMap()
    {
        return myGraph2ScheduleMap;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException
    {
        myGraph2ScheduleMap =
            (Map<GraphID, VertexSchedule>) in.readObject();
        myVertex2HostMap =
            (Map<VertexID, HostID>) in.readObject();
        myWorkerNodes = (Set<HostID>) in.readObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(myGraph2ScheduleMap);
        out.writeObject(myVertex2HostMap);
        out.writeObject(myWorkerNodes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "ExecutionRegistryState [myGraph2ScheduleMap="
                + myGraph2ScheduleMap + ", myVertex2HostMap="
                + myVertex2HostMap + ", myWorkerNodes=" + myWorkerNodes
                + "]";
    }
}