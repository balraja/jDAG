package org.augur.dag.test;

import java.util.List;

import org.augur.dag.ExecutionContext;
import org.augur.dag.ExecutionGraph;
import org.augur.dag.ExecutionGraphID;
import org.augur.dag.IOKey;
import org.augur.dag.IOSource;
import org.augur.dag.Record;
import org.augur.dag.UserDefinedFunction;
import org.augur.dag.SimpleInputSplitterFactory.SplitterType;
import org.augur.dag.builder.Collect;
import org.augur.dag.builder.GraphBuilder;
import org.augur.dag.builder.GraphSpecification;
import org.augur.dag.builder.InputSpecification;
import org.augur.dag.builder.Join;
import org.augur.dag.builder.UDFSpecification;
import org.augur.persistence.flatfile.LineInterpreter;

/**
 * A simple testcase for <code>GraphBuilder</code>
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class GraphBuilderTest
{
     private static class Add implements UserDefinedFunction
     {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean process(List<IOKey> inputs, List<IOKey> outputs,
                ExecutionContext graphContext)
        {
            // TODO Auto-generated method stub
            return false;
        }

     }

     private static class Max implements UserDefinedFunction
     {

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean process(List<IOKey> inputs, List<IOKey> outputs,
                ExecutionContext graphContext)
        {
            // TODO Auto-generated method stub
            return false;
        }

     }

     private static class FlattenLineInterpreter implements LineInterpreter
     {

        /**
         * {@inheritDoc}
         */
        @Override
        public String flattenRecord(Record r)
        {
            // TODO Auto-generated method stub
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Record makeRecord(String line)
        {
            // TODO Auto-generated method stub
            return null;
        }

     }

     private GraphSpecification makeGraphSpecification()
     {
         GraphSpecification spec = new GraphSpecification();
         InputSpecification input1 = spec.addInput("File1");
         input1.fromSource("File1.txt", IOSource.FLAT_FILE)
               .splitInto(3)
               .by(SplitterType.DIRECT);

         InputSpecification input2 = spec.addInput("FIle2");
         input2.fromSource("File2.txt", IOSource.FLAT_FILE)
               .splitInto(3)
               .by(SplitterType.DIRECT);

         UDFSpecification map = spec.addUDF("TableJoin", Add.class);
         map.applyOn(new Join(input1, input2))
            .toProduce("join");

         UDFSpecification reduce = spec.addUDF("Reduce", Max.class);
         reduce.applyOn(new Collect(map.getFunctionOutput("join")))
               .toProduce("output")
               .setOutputIoSource(IOSource.FLAT_FILE)
               .setAttribute("RecordClass", FlattenLineInterpreter.class);

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
