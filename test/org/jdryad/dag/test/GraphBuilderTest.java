package org.jdryad.dag.test;

import com.google.common.collect.Sets;

import org.jdryad.dag.ExecutionGraph;
import org.jdryad.dag.ExecutionGraphID;
import org.jdryad.dag.SimpleInputSplitterFactory.SplitterType;
import org.jdryad.dag.builder.FileIOKeyFactory;
import org.jdryad.dag.builder.GraphBuilder;
import org.jdryad.dag.builder.GraphSpecification;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * A simple testcase for <code>GraphBuilder</code>
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class GraphBuilderTest
{
     private GraphSpecification makeGraphSpecification()
     {
         GraphSpecification spec = new GraphSpecification();
         spec.addInput("File1", SplitterType.DIRECT, 3) // Take two files and
             .addInput("File2", SplitterType.DIRECT, 3) // split into 3 parts
             .addUDFSpecification(                      // Apply sum on partial
                 "org.jdryad.dag.test.SumListFunction",     // inputs
                 "partial add",
                 Sets.newHashSet("File1", "File2"),
                 true)
             .addUDFSpecification(                      // combine the sum from
                 "org.jdryad.dag.test.SumListFunction",     // partial ops.
                 "combine",
                 Sets.newHashSet("org.jdryad.dag.test.SumListFunction"),
                 false);
         return spec;
     }

     @Test(groups={"dag"}, testName="DAGBuilderTest")
     public void graphBuilderTest()
     {
         GraphSpecification spec = makeGraphSpecification();
         GraphBuilder builder =
             new GraphBuilder(new FileIOKeyFactory());
         ExecutionGraph graph =
             builder.build(spec, new ExecutionGraphID("TestGraph"));
         Assert.assertEquals(graph.getInputs().size(), 2);
         Assert.assertEquals(graph.getVertices().size(), 6);
         Assert.assertEquals(
             graph.getOutgoingEdge(graph.getInputs().iterator().next()).size(),
             3);
     }
}
