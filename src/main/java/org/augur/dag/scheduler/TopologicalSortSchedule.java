package org.augur.dag.scheduler;

import java.util.HashSet;
import java.util.Set;

import org.augur.dag.Edge;
import org.augur.dag.ExecutionGraph;
import org.augur.dag.Vertex;
import org.augur.dag.VertexID;

/**
 * A simple type responsible for returning a schedule of vertices, when
 * they are to be executed on the cluster.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class TopologicalSortSchedule implements Schedule
{
    /**
     * The <code>ExecutionGraph</code> from which we process the vertices.
     */
    private final ExecutionGraph myGraph;

    /** The vertices that have already been returned by this scheduler */
    private final Set<VertexID> myReturnedVertices;

    /** The vertices whose processing is completed */
    private final Set<VertexID> myDoneVertices;

    /**
     * CTOR
     */
    public TopologicalSortSchedule(ExecutionGraph graph)
    {
        myGraph = graph;
        myReturnedVertices = new HashSet<VertexID>();
        myDoneVertices = new HashSet<VertexID>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vertex getVertexForExecution()
    {
        for (Vertex inputVertex : myGraph.getVertices()) {
            if (!myDoneVertices.contains(inputVertex.getID())
                && !myReturnedVertices.contains(inputVertex.getID())
                && myGraph.getIncomingEdge(inputVertex.getID()).isEmpty())
            {
                    myReturnedVertices.add(inputVertex.getID());
                    return inputVertex;
            }
        }
        // If it comes here then it means all the input vertices are done.
        // We have started processing the compute vertices.
        for (Vertex vertex : myGraph.getVertices()) {
            if (myDoneVertices.contains(vertex.getID())
                || myReturnedVertices.contains(vertex.getID()))
            {
                continue;
            }
            boolean isAllInputsDone = true;
            for (Edge e : myGraph.getIncomingEdge(vertex.getID())) {
                if (!myDoneVertices.contains(e.getSource())) {
                    isAllInputsDone = false;
                    break;
                }
            }
            if (isAllInputsDone) {
                myReturnedVertices.add(vertex.getID());
                return vertex;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDone(VertexID vertexID)
    {
        myDoneVertices.add(vertexID);
    }

    /** Returns true if the vertex has been marked done */
    public boolean isDone(VertexID vertexID)
    {
        return myDoneVertices.contains(vertexID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCompleted()
    {
        return myDoneVertices.size() == myGraph.getVertices().size();
    }
}
