package org.jdag.master;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jdag.commmunicator.HostID;
import org.jdag.common.persistentds.Persist;
import org.jdag.common.persistentds.PersistentDS;
import org.jdag.common.persistentds.Snapshot;
import org.jdag.graph.GraphID;
import org.jdag.graph.GraphVertexID;
import org.jdag.graph.Vertex;
import org.jdag.graph.scheduler.Schedule;

/**
 * The registry to be used for maintaining the state of the executions.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ExecutionStateRegistry implements PersistentDS
{
    private static final String MY_ID = "ExecutionState";

    private final Set<HostID> myWorkerNodes;

    private final Map<GraphVertexID, HostID> myVertex2HostMap;

    private final Map<GraphID, Schedule> myGraph2ScheduleMap;

    /**
     * The state that needs to be persisted.
     */
    public static class ExecutionRegistryState implements Snapshot
    {
        private Set<HostID> myWorkerNodes;

        private Map<GraphVertexID, HostID> myVertex2HostMap;

        private Map<GraphID, Schedule> myGraph2ScheduleMap;

        /** CTOR */
        public ExecutionRegistryState(
            Set<HostID> workerNodes,
            Map<GraphVertexID, HostID> vertex2HostMap,
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
        public Map<GraphVertexID, HostID> getVertex2HostMap()
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
                (Map<GraphVertexID, HostID>) in.readObject();
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
    }

    /**
     * CTOR
     */
    public ExecutionStateRegistry()
    {
        myWorkerNodes = new HashSet<HostID>();
        myVertex2HostMap = new HashMap<GraphVertexID, HostID>();
        myGraph2ScheduleMap = new HashMap<GraphID, Schedule>();
    }

    /** Adds a worker */
    @Persist
    public void addWorker(HostID workerID)
    {
        myWorkerNodes.add(workerID);
    }

    /** Returns true if the worker has already been added to the registry */
    public boolean hasWorker(HostID workerID)
    {
        return myWorkerNodes.contains(workerID);
    }

    /** Returns the list of worker nodes available in the registry */
    public Set<HostID> getWorkers()
    {
        return Sets.newHashSet(myWorkerNodes);
    }

    /** Removes the worker */
    @Persist
    public void removeWorker(HostID worker)
    {
        myWorkerNodes.remove(worker);
    }

    /** Replaces the existing worker with the new worker */
    @Persist
    public void replaceWoker(HostID existing, HostID newWorker)
    {
        myWorkerNodes.remove(existing);
        myWorkerNodes.add(newWorker);
    }

    /**
     * Updates the mapping between vertex to the host that executes the
     * vertex.
     */
    @Persist
    public void updateVertex2HostMapping(
        GraphVertexID graphVertexID, HostID hostID)
    {
        myVertex2HostMap.put(graphVertexID, hostID);
    }

    /** Returns the <code>HostID</code> where the graph is executed now */
    public Set<HostID> getWorkersForGraph(GraphID graphID)
    {
        HashSet<HostID> hosts = new HashSet<HostID>();
        for (Map.Entry<GraphVertexID, HostID> entry :
                myVertex2HostMap.entrySet())
        {
            if (entry.getKey().getGraphID().equals(graphID)) {
                hosts.add(entry.getValue());
            }
        }
        return hosts;
    }

    /**
     * {@inheritDoc}
     */
    @Persist
    public void removeVertexToHostMapping(GraphVertexID graphVertexID)
    {
         myVertex2HostMap.remove(graphVertexID);
    }

    /** Updates the mapping between graph and its schedule */
    @Persist
    public void addSchedule(GraphID graphID, Schedule schedule)
    {
        myGraph2ScheduleMap.put(graphID, schedule);
    }

    /**
     * Removes the vertex to the worker mapping and updates the schedule for the
     * given graph that the vertex is done.
     *
     * @return True if the execution of the graph is done, false otherwise.
     */
    @Persist
    public boolean markDone(GraphVertexID graphVertexID)
    {
        myVertex2HostMap.remove(graphVertexID);
        Schedule schedule = myGraph2ScheduleMap.get(graphVertexID.getGraphID());
        schedule.notifyDone(graphVertexID.getVertexID());
        if (schedule.isCompleted()) {
            myGraph2ScheduleMap.remove(graphVertexID.getGraphID());
            return true;
        }
        else {
            return false;
        }
    }

    /** Returns a vertex for execution */
    public Vertex getVertexForExecution(GraphID graphID)
    {
        Schedule schedule = myGraph2ScheduleMap.get(graphID);
        Preconditions.checkNotNull(schedule);
        return schedule.getVertexForExecution();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String ID()
    {
        return MY_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initFromSnapshot(Snapshot snapshot)
    {
        Preconditions.checkState(snapshot instanceof ExecutionRegistryState);
        ExecutionRegistryState executionRegistryState =
            (ExecutionRegistryState) snapshot;
        myWorkerNodes.clear();
        myWorkerNodes.addAll(executionRegistryState.getWorkerNodes());
        myGraph2ScheduleMap.clear();
        myGraph2ScheduleMap.putAll(executionRegistryState.getGraph2ScheduleMap());
        myVertex2HostMap.clear();
        myVertex2HostMap.putAll(executionRegistryState.getVertex2HostMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Snapshot makeSnapshot()
    {
        return new ExecutionRegistryState(
            myWorkerNodes, myVertex2HostMap, myGraph2ScheduleMap);
    }
}
