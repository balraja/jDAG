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
import org.jdryad.dag.InputSplitter;
import org.jdryad.dag.InputSplitterFactory;
import org.jdryad.dag.InputVertex;
import org.jdryad.dag.SimpleVertex;
import org.jdryad.dag.UserDefinedFunction;
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

    private final UDFFactory myUDFFactory;

    private final Map<String, VertexID> myInputToSplitVertex;

    private final Map<String, List<VertexID>> myFunctionToParallelVertices;

    /**
     * CTOR
     */
    public GraphBuilder(IOKeyFactory iOKeyFactory, UDFFactory uDFFactory)
    {
        myIOKeyFactory = iOKeyFactory;
        myUDFFactory = uDFFactory;
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

        ArrayList<InputVertex> inVertices = new ArrayList<InputVertex>();
        for (InputSpecification ioSpec : specification.getInputSpecs()) {
            IOKey input =
                myIOKeyFactory.makeKey(ioSpec.getIdentifier());
            // TODO Add support for user defined split function.
            InputSplitter splitter =
                InputSplitterFactory.makeSpliter(ioSpec.getSplitter());
            VertexID vertexID =
                new VertexID(ioSpec.getIdentifier() + SPLIT_VERTEX_SUFFIX);
            // TODO Add support for user defined split ratio.
            ArrayList<IOKey> outputs = new ArrayList<IOKey>();
            for (int i = 0; i < ioSpec.getNumSplits(); i++) {
                myIOKeyFactory.makeKey(vertexID, i);
            }
            myInputToSplitVertex.put(ioSpec.getIdentifier(), vertexID);
            inVertices.add(
                new InputVertex(vertexID,
                                input,
                                outputs,
                                splitter));
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
     * Creates vertices for a function thats to be executed in parallel for
     * splits of inputs.
     */
    private void processCombineFunctionSpec(
        UDFSpecification spec, ExecutionGraph executionGraph)
    {
        // A combiner can have only one input.
        Preconditions.checkArgument(spec.getInputSources().size() == 1);
        new ArrayList<Pair<VertexID,IOKey>>();
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

        UserDefinedFunction function =
            myUDFFactory.makeFunction(spec.getName());

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
                                  function,
                                  inputs,
                                  Collections.singletonList(opKey)));
            executionGraph.addEdges(edges);
            parallelVertices.add(vertexID);
        }
    }
}
