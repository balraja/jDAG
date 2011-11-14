/*******************************************************************************
 * jDAG is a project to build acyclic dataflow graphs for processing massive datasets.
 *
 *     Copyright (C) 2011, Author: Balraja,Subbiah
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package org.jdag.graph.scheduler;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.jdag.common.log.LogFactory;
import org.jdag.common.persistentds.Persist;
import org.jdag.graph.Edge;
import org.jdag.graph.Graph;
import org.jdag.graph.InputVertex;
import org.jdag.graph.Vertex;
import org.jdag.graph.VertexID;

/**
 * A simple type responsible for returning a schedule of vertices, when
 * they are to be executed on the cluster.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class TopologicalSortSchedule implements VertexSchedule
{
    /**
     * The serial version id.
     */
    private static final long serialVersionUID = 4186288918149046297L;

    /** The logger */
    private final Logger LOG =
        LogFactory.getLogger(TopologicalSortSchedule.class);

    /**
     * The <code>ExecutionGraph</code> from which we process the vertices.
     */
    private Graph myGraph;

    /** The vertices that have already been returned by this scheduler */
    private Set<VertexID> myReturnedVertices;

    /** The vertices whose processing is completed */
    private Set<VertexID> myDoneVertices;

    /**
     * CTOR
     */
    public TopologicalSortSchedule()
    {
        myGraph = null;
        myReturnedVertices = null;
        myDoneVertices = null;
    }

    /**
     * CTOR
     */
    public TopologicalSortSchedule(Graph graph)
    {
        myGraph = graph;
        myReturnedVertices = new HashSet<VertexID>();
        myDoneVertices = new HashSet<VertexID>();
        // Just mark all the input vertices as done.
        StringBuilder graphInfo = new StringBuilder();
        graphInfo.append("The graph " + myGraph.getID() + " has \n");
        graphInfo.append("Vertices [ ");
        for (Vertex vertex : myGraph.getVertices()) {
            graphInfo.append("\n "  + vertex);
            if (vertex instanceof InputVertex) {
                graphInfo.append("(Input)");
                myDoneVertices.add(vertex.getID());
            }

            List<Edge> incomingEdges =
                myGraph.getIncomingEdge(vertex.getID());
            if (incomingEdges != null && !incomingEdges.isEmpty()) {
                for (Edge e : incomingEdges) {
                   graphInfo.append("\n\t" + e);
                }
            }
        }
        graphInfo.append(" ]");
        LOG.info(graphInfo.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vertex getVertexForExecution()
    {
        LOG.info("Executing vertex search for " + myGraph.getID());
        // If it comes here then it means all the input vertices are done.
        // We have started processing the compute vertices.
        for (Vertex vertex : myGraph.getVertices()) {
            if (myDoneVertices.contains(vertex.getID())
                || myReturnedVertices.contains(vertex.getID()))
            {
                continue;
            }

            List<Edge> incomingEdges =
                myGraph.getIncomingEdge(vertex.getID());
            if (incomingEdges != null && !incomingEdges.isEmpty()) {
                boolean isAllInputsDone = true;
                for (Edge e : incomingEdges) {
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
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Persist
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

    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) 
        throws IOException, ClassNotFoundException
    {
        myGraph = (Graph) in.readObject();
        myReturnedVertices = (Set<VertexID>) in.readObject();
        myDoneVertices = (Set<VertexID>) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(myGraph);
        out.writeObject(myReturnedVertices);
        out.writeObject(myDoneVertices);
    }
}
