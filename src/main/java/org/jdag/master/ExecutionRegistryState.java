package org.jdag.master;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jdag.common.persistentds.Snapshot;
import org.jdag.communicator.HostID;
import org.jdag.graph.GraphID;
import org.jdag.graph.VertexID;
import org.jdag.graph.scheduler.Schedule;

/**
 * The state that needs to be persisted.
 */
public class ExecutionRegistryState implements Snapshot
{
    private Set<HostID> myWorkerNodes;

    private Map<VertexID, HostID> myVertex2HostMap;

    private Map<GraphID, Schedule> myGraph2ScheduleMap;

    /**
     * CTOR
     */
    public ExecutionRegistryState()
    {
        myWorkerNodes = new HashSet<HostID>();
        myVertex2HostMap = new HashMap<VertexID, HostID>();
        myGraph2ScheduleMap = new HashMap<GraphID, Schedule>();
    }

    /** CTOR */
    public ExecutionRegistryState(
        Set<HostID> workerNodes,
        Map<VertexID, HostID> vertex2HostMap,
        Map<GraphID, Schedule> graph2ScheduleMap)
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
    public Map<GraphID, Schedule> getGraph2ScheduleMap()
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
            (Map<GraphID, Schedule>) in.readObject();
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