package org.jdryad.dag.builder;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jdryad.common.Pair;
import org.jdryad.dag.Edge;
import org.jdryad.dag.ExecutionGraph;
import org.jdryad.dag.ExecutionGraphID;
import org.jdryad.dag.IOKey;
import org.jdryad.dag.SimpleVertex;
import org.jdryad.dag.UDFIdentityGenerator;
import org.jdryad.dag.Vertex;
import org.jdryad.dag.VertexID;

/**
 * A simple type for building graphs. It takes a <code>GraphSpecification</code>
 * and builds a ExecutionGraph from it.
 *
 * TODO:
 * 1. Add functionality where a function can produce multiple outputs to be
 *    feed into multiple other functions.
 * 2. Add a case where an input can be fed into a function without splitting.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class GraphBuilder
{
    public static final String SPLIT_VERTEX_SUFFIX = "splitter";

    private final IOKeyFactory myIOKeyFactory;

    private final UDFIdentityGenerator myUdfIdentityGenerator;

    private final Map<String, VertexID> myInputToSplitVertex;

    private final Map<String, List<VertexID>> myFunctionToParallelVertices;

    /**
     * CTOR
     */
    public GraphBuilder(IOKeyFactory iOKeyFactory)
    {
        myIOKeyFactory = iOKeyFactory;
        myUdfIdentityGenerator = new UDFIdentityGenerator();
        myInputToSplitVertex = new HashMap<String, VertexID>();
        myFunctionToParallelVertices =
            new HashMap<String, List<VertexID>>();
    }

    /** Builds the <code>ExecutionGraph</code> from the given specification */
    public ExecutionGraph build(GraphSpecification specification,
                                ExecutionGraphID graphID)
    {
        // Asserts that we have atleast one input to be split.
        Preconditions.checkArgument(
            !specification.getInputSpecs().isEmpty());
        Preconditions.checkArgument(
            !specification.getFunctions().isEmpty());

        ArrayList<Vertex> inVertices = new ArrayList<Vertex>();
        for (InputSpecification ioSpec : specification.getInputSpecs()) {
            IOKey input =
                myIOKeyFactory.makeKey(ioSpec.getIdentifier());
            // TODO Add support for user defined split function.
            VertexID vertexID =
                new VertexID(ioSpec.getIdentifier() + SPLIT_VERTEX_SUFFIX);
            // TODO Add support for user defined split ratio.
            ArrayList<IOKey> outputs = new ArrayList<IOKey>();
            for (int i = 0; i < ioSpec.getNumSplits(); i++) {
                outputs.add(myIOKeyFactory.makeKey(vertexID, i));
            }
            myInputToSplitVertex.put(ioSpec.getIdentifier(), vertexID);
            inVertices.add(
                new SimpleVertex(vertexID,
                                 myUdfIdentityGenerator.getMapperIdentifier(
                                     ioSpec.getSplitter()),
                                 Collections.singletonList(input),
                                 outputs));
        }

        ExecutionGraph graph =
            new ExecutionGraph(graphID, new HashSet<Vertex>(inVertices));
        for (UDFSpecification udfSpec : specification.getFunctions()) {
             if (udfSpec.worksOnSplitInput()) {
                 processParallelFunctionSpec(udfSpec, graph);
             }
             else {
                 processCombineFunctionSpec(udfSpec, graph);
             }
        }
        return graph;
    }

    /**
     * Creates vertices for a function that's to be executed in parallel for
     * splits of inputs.
     */
    private void processCombineFunctionSpec(
        UDFSpecification spec, ExecutionGraph executionGraph)
    {
        // A combiner can have only one input.
        Preconditions.checkArgument(
           spec.getInputSources().size() == 1,
           "Get more than one argument for combiner function "
           + spec.getName());

        List<Pair<VertexID, IOKey>> parameters = null;
        for (String paramID : spec.getInputSources()) {
            if (myInputToSplitVertex.containsKey(paramID)) {
                // The input is a split input
                parameters = getIOSplits(paramID, executionGraph);
            }
            else {
                // Its a o/p from a parallel function.
                parameters =
                    getParallelFunctionOutputs(paramID, executionGraph);
            }
        }

        String udfId =
            myUdfIdentityGenerator.getFunctionIdentifier(
                spec.getIdentifier());
        List<Edge> edges = new ArrayList<Edge>();
        ArrayList<IOKey> inputs = new ArrayList<IOKey>();
        VertexID vertexID = new VertexID(spec.getName() + ".MERGER");
        for (Pair<VertexID, IOKey> param : parameters) {
            inputs.add(param.getSecond());
            edges.add(new Edge(param.getFirst(),
                               vertexID,
                               param.getSecond()));
        }
        IOKey opKey = myIOKeyFactory.makeKey(vertexID.getName() + " OUT");
        executionGraph.addVertex(
             new SimpleVertex(vertexID,
                              udfId,
                              inputs,
                              Collections.singletonList(opKey)));
        executionGraph.addEdges(edges);
        myFunctionToParallelVertices.put(spec.getName(),
                                         Collections.singletonList(vertexID));
    }

    /** Returns the <code>IOKey</code>s corresponding to the input that's split */
    private List<Pair<VertexID, IOKey>> getIOSplits(
            String ioIdentifier, ExecutionGraph executionGraph)
    {
        // The input is a split input
        Vertex v = executionGraph.getVertex(
                       myInputToSplitVertex.get(ioIdentifier));
        List<Pair<VertexID, IOKey>> splitInputs =
            new ArrayList<Pair<VertexID,IOKey>>();
        for (IOKey opKey : v.getOutputs()) {
            splitInputs.add(new Pair<VertexID, IOKey>(v.getID(), opKey));
        }
        return splitInputs;
    }

    /**
     * Returns the <code>IOKey</code>s corresponding to a function's
     * output.
     */
    private List<Pair<VertexID, IOKey>> getParallelFunctionOutputs(
            String functionIdentifier, ExecutionGraph executionGraph)
    {
        // Its a o/p from a parallel function.
        // TODO Here we assume a function to be many to one.
        // FIx this assumption.
        List<Pair<VertexID, IOKey>> ops =
            new ArrayList<Pair<VertexID,IOKey>>();
        for (VertexID vertexId :
                 myFunctionToParallelVertices.get(functionIdentifier))
        {
            Vertex v = executionGraph.getVertex(vertexId);
            ops.add(new Pair<VertexID, IOKey>(v.getID(),
                                              v.getOutputs().get(0)));
        }
        return ops;
    }

    /**
     * Creates vertices for a function that needs to be executed in parallel for
     * splits of inputs.
     */
    private void processParallelFunctionSpec(
        UDFSpecification spec, ExecutionGraph executionGraph)
    {
        ArrayList<List<Pair<VertexID, IOKey>>> parameters =
            new ArrayList<List<Pair<VertexID,IOKey>>>();
        for (String paramID : spec.getInputSources()) {
            if (myInputToSplitVertex.containsKey(paramID)) {
                // The input is a split input
                parameters.add(getIOSplits(paramID, executionGraph));
            }
            else {
                // Its a o/p from a parallel function.
                parameters.add(
                    getParallelFunctionOutputs(paramID, executionGraph));
            }
        }

        String udfIdentifier =
            myUdfIdentityGenerator.getFunctionIdentifier(
                spec.getIdentifier());
        int firstParamSplitSize = parameters.get(0).size();
        ArrayList<VertexID> parallelVertices = new ArrayList<VertexID>();
        for (int i = 0; i < firstParamSplitSize; i++) {
            VertexID vertexID = new VertexID(spec.getName() + i);
            List<Edge> edges = new ArrayList<Edge>();
            ArrayList<IOKey> inputs = new ArrayList<IOKey>();
            for (List<Pair<VertexID, IOKey>> param : parameters) {
                Pair<VertexID, IOKey> inputWithSrc = param.get(i);
                inputs.add(param.get(i).getSecond());
                edges.add(new Edge(inputWithSrc.getFirst(),
                                   vertexID,
                                   inputWithSrc.getSecond()));
            }
            IOKey opKey = myIOKeyFactory.makeKey(vertexID, i);
            executionGraph.addVertex(
                 new SimpleVertex(vertexID,
                                  udfIdentifier,
                                  inputs,
                                  Collections.singletonList(opKey)));
            executionGraph.addEdges(edges);
            parallelVertices.add(vertexID);
        }
        myFunctionToParallelVertices.put(spec.getName(), parallelVertices);
    }
}
