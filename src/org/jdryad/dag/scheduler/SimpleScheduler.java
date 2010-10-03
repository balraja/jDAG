package org.jdryad.dag.scheduler;

import java.util.HashSet;
import java.util.Set;

import org.jdryad.dag.Edge;
import org.jdryad.dag.ExecutionGraph;
import org.jdryad.dag.Vertex;
import org.jdryad.dag.VertexID;

/**
 * A simple type responsible for returning a schedule of vertices, when
 * they are to be executed on the cluster.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class SimpleScheduler implements Scheduler
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
    public SimpleScheduler(ExecutionGraph graph)
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
        for (VertexID inputVertex : myGraph.getInputs()) {
            if (!myDoneVertices.contains(inputVertex)
                && !myReturnedVertices.contains(inputVertex))
            {
                    myReturnedVertices.add(inputVertex);
                    return myGraph.getVertex(inputVertex);
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

    /** Returns tru if the vertex has been marked done */
    public boolean isDOne(VertexID vertexID)
    {
        return myDoneVertices.contains(vertexID);
    }
}
