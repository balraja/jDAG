package org.jdag.graph;

import com.google.common.base.Preconditions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Type that defines a job graph that needs to be executed on a distributed
 * cluster
 *
 * TODO:
 * 1. Add a check for detecting cycles.
 *
 * @author subbiahb
 * @version $Id:$
 */
public class Graph implements Externalizable
{
    private GraphID myID;

    private Map<VertexID, Vertex> myVertices;

    private Set<Edge> myEdges;

    private Map<VertexID, List<Edge>> myOutgoingEdges;

    private Map<VertexID, List<Edge>> myIncomingEdges;
    
    /**
     * CTOR
     */
    public Graph()
    {
        myID = null;
        myVertices = null;
        myEdges = null;
        myOutgoingEdges = null;
        myIncomingEdges = null;
    }

    /**
     * CTOR
     */
    public Graph(GraphID id)
    {
        myID = id;
        myVertices = new HashMap<VertexID, Vertex>();
        myEdges = new HashSet<Edge>();
        myOutgoingEdges = new HashMap<VertexID, List<Edge>>();
        myIncomingEdges = new HashMap<VertexID, List<Edge>>();
    }

    /**
     * Returns the value of iD
     */
    public GraphID getID()
    {
        return myID;
    }

    /** Returns Vertex corresponding to the given id */
    public Vertex getVertex(VertexID id)
    {
        return myVertices.get(id);
    }

    /**
     * Returns the value of vertices
     */
    public Collection<Vertex> getVertices()
    {
        return myVertices.values();
    }

    /** Adds a vertex to the graph */
    public void addVertex(Vertex vertex)
    {
        myVertices.put(vertex.getID(), vertex);
    }

    /**
     * Returns all the outgoing edges for the given vertex.
     */
    public List<Edge> getOutgoingEdge(VertexID id)
    {
        return myOutgoingEdges.get(id);
    }

    /**
     * Returns all incoming edges for the given vertex.
     */
    public List<Edge> getIncomingEdge(VertexID id)
    {
        return myIncomingEdges.get(id);
    }

    /**
     * Adds the edge between src and destn vertices as src -> destn.
     */
    public void addEdges(List<Edge> edgeList)
    {
        Preconditions.checkArgument(!edgeList.isEmpty());
        for (Edge e : edgeList) {
            addEdge(e);
        }
    }

    /**
     * Adds the edge between src and destn vertices as src -> destn.
     */
    public void addEdge(Edge edge)
    {
        Preconditions.checkNotNull(myVertices.containsKey(edge.getSource()),
                "Src vertex " + edge.getSource()
                + " is not present in the graph");
        Preconditions.checkNotNull(myVertices.containsKey(edge.getDestination()),
                "Destn vertex " + edge.getDestination()
                + " is not present in the graph");

        myEdges.add(edge);
        List<Edge> outgoingEdges = myOutgoingEdges.get(edge.getSource());
        if (outgoingEdges == null) {
            outgoingEdges = new ArrayList<Edge>();
            myOutgoingEdges.put(edge.getSource(), outgoingEdges);
        }
        outgoingEdges.add(edge);
        List<Edge> incomingEdges = myIncomingEdges.get(edge.getDestination());
        if (incomingEdges == null) {
            incomingEdges = new ArrayList<Edge>();
            myIncomingEdges.put(edge.getDestination(), incomingEdges);
        }
        incomingEdges.add(edge);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException
    {
        myID = (GraphID) in.readObject();
        myVertices = (Map<VertexID, Vertex>) in.readObject();
        myEdges = (Set<Edge>) in.readObject();
        myIncomingEdges = (Map<VertexID, List<Edge>>) in.readObject();
        myOutgoingEdges = (Map<VertexID, List<Edge>>) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(myID);
        out.writeObject(myVertices);
        out.writeObject(myEdges);
        out.writeObject(myIncomingEdges);
        out.writeObject(myOutgoingEdges);
    }
}
