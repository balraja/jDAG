package org.jdryad.dag.builder;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jdryad.dag.Edge;
import org.jdryad.dag.ExecutionGraph;
import org.jdryad.dag.ExecutionGraphID;
import org.jdryad.dag.IOKey;
import org.jdryad.dag.IOSource;
import org.jdryad.dag.SimpleVertex;
import org.jdryad.dag.UDFIdentityGenerator;
import org.jdryad.dag.VertexID;
import org.jdryad.persistence.medium.PersistenceMedium;

/**
 * A simple type for building graphs. It takes a <code>GraphSpecification</code>
 * and builds a ExecutionGraph from it.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class GraphBuilder
{
    public static final String SPLIT_VERTEX_SUFFIX = "splitter";

    private final GraphSpecification myGraphSpecification;

    private final ExecutionGraphID myGraphID;

    private final PersistenceMedium myPersistenceMedium;

    private final UDFIdentityGenerator myUdfIdentityGenerator;

    private final IOSource myIntermediateIOSrc;

    /**
     * CTOR
     */
    public GraphBuilder(PersistenceMedium persistenceMedium,
                        GraphSpecification specification,
                        ExecutionGraphID graphID,
                        IOSource intermediateIOSource)
    {
        // Asserts that we have atleast one input to be split.
        Preconditions.checkArgument(
            !specification.getInputSpecs().isEmpty());
        Preconditions.checkArgument(
            !specification.getFunctions().isEmpty());

        myPersistenceMedium = persistenceMedium;
        myIntermediateIOSrc = intermediateIOSource;
        myGraphID = graphID;
        myGraphSpecification = specification;
        myUdfIdentityGenerator = new UDFIdentityGenerator();
    }

    /** Builds the <code>ExecutionGraph</code> from the given specification */
    public ExecutionGraph build()
    {
        ExecutionGraph graph = new ExecutionGraph(myGraphID);
        for (InputSpecification ioSpec : myGraphSpecification.getInputSpecs()) {
            IOKey input =
                myPersistenceMedium.makeKey(ioSpec.getSource(),
                                            ioSpec.getPersistenceSourceType());

            VertexID vertexID =
                new VertexID(ioSpec.getIdentifier());
            ArrayList<IOKey> outputs = new ArrayList<IOKey>();
            for (String outputKey : ioSpec.getFragementIds()) {
                outputs.add(
                    myPersistenceMedium.makeKey(outputKey,
                                                myIntermediateIOSrc));
            }
            graph.addVertex(
                new SimpleVertex(vertexID,
                                 myUdfIdentityGenerator.getMapperIdentifier(
                                     ioSpec.getSplitter()),
                                 Collections.singletonList(input),
                                 outputs));
        }


        for (UDFSpecification udfSpec : myGraphSpecification.getFunctions()) {
             if (udfSpec.getFunctionInputs().getNumCombinations() > 1) {
                 processParallelFunctionSpec(udfSpec, graph);
             }
             else {
                 processSimpleFunctionSpec(udfSpec, graph);
             }
        }
        return graph;
    }

    /**
     * Creates vertices for a function that's to be executed in parallel for
     * splits of inputs.
     */
    private void processSimpleFunctionSpec(
        UDFSpecification spec, ExecutionGraph graph)
    {
        // A simple function can have only one input combination
        Preconditions.checkArgument(
           spec.getFunctionInputs().getNumCombinations() == 1,
           "Get more than one argument for combiner function "
           + spec.getIdentifier());

        List<String> params =
            spec.getFunctionInputs().getInputAt(0);
        List<String> sources =
            spec.getFunctionInputs().getFragmentSourcesFor(0);
        List<IOKey> inputs = new ArrayList<IOKey>();
        String udfID =
            myUdfIdentityGenerator.getFunctionIdentifier(
                spec.getUDFName());
        List<Edge> edges = new ArrayList<Edge>();

        VertexID vertexID = new VertexID(spec.getParallelFunctionIds().get(0));
        for (String param : params) {
            IOKey key =  myPersistenceMedium.makeKey(param, myIntermediateIOSrc);
            inputs.add(key);
            edges.add(new Edge(new VertexID(sources.get(0)),
                               vertexID,
                               key));
        }

        ArrayList<IOKey> outputs = new ArrayList<IOKey>();
        for (FunctionOutput output : spec.getFunctionOutputs()) {
            outputs.add(
                myPersistenceMedium.makeKey(
                    output.getFragementIds().get(0),
                    spec.getOutputIoSource() != null ?
                        spec.getOutputIoSource()
                        : myIntermediateIOSrc));
        }

        graph.addVertex(
             new SimpleVertex(vertexID,
                              udfID,
                              inputs,
                              outputs));
        graph.addEdges(edges);
    }

    /**
     * Creates vertices for a function that needs to be executed in parallel for
     * splits of inputs.
     */
    private void processParallelFunctionSpec(
        UDFSpecification spec, ExecutionGraph graph)
    {
        String udfIdentifier =
            myUdfIdentityGenerator.getFunctionIdentifier(
                spec.getIdentifier());
        List<String> functionIds = spec.getParallelFunctionIds();
        for (int i = 0;
             i < spec.getFunctionInputs().getNumCombinations();
             i++)
        {
            VertexID vertexID = new VertexID(functionIds.get(i));
            List<Edge> edges = new ArrayList<Edge>();
            ArrayList<IOKey> inputs = new ArrayList<IOKey>();
            List<String> parameters = spec.getFunctionInputs().getInputAt(i);
            List<String> sources = spec.getFunctionInputs()
                                       .getFragmentSourcesFor(i);
            for (int j = 0; j < parameters.size(); j++)
            {
                IOKey key =
                    myPersistenceMedium.makeKey(parameters.get(j),
                                                myIntermediateIOSrc);
                inputs.add(key);
                edges.add(new Edge(new VertexID(sources.get(j)),
                                   vertexID,
                                   key));
            }

            ArrayList<IOKey> outputs = new ArrayList<IOKey>();
            for (FunctionOutput output : spec.getFunctionOutputs()) {
                outputs.add(
                    myPersistenceMedium.makeKey(
                        output.getFragementIds().get(i),
                        spec.getOutputIoSource() != null ?
                            spec.getOutputIoSource()
                            : myIntermediateIOSrc));
            }

            graph.addVertex(
                 new SimpleVertex(vertexID,
                                  udfIdentifier,
                                  inputs,
                                  outputs));
            graph.addEdges(edges);
        }
    }
}
