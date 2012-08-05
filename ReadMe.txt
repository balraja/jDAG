jdag is a library to build to model data flow processing as
distributed acyclic graphs based on simple primitives on
a collection based approach. An user creates a graph by
applying various primitives on the collection. The primitives
when applied on the context of graph builder will aid the
construction of a DAG that can be executed using master
slave approach.

This is inspired by FlumeJava http://dl.acm.org/citation.cfm?id=1806638
and Dryad http://research.microsoft.com/en-us/projects/dryad/ frameworks.

The various primitives used in the construction of a DAG are as follows.

DataCollection

    Represents a collection of records to be processed.

    The interface for a DataCollection looks something like below

    public interface DataCollection<T>
    {
        /**
         * The suffix to be appended to the file name in which data is stored.
         */
        public static final String FILE_SUFFIX = "_data";

        /**
         * Applies the function directly on the data collection.
         */
        public <V> DataCollection<V> apply(Function<T, V> function);

        /**
         * Creates a node in the execution graph that can be used for splitting
         * the data into multiple partitions.
         */
        public ShardedDataCollection<T> partition(Splitter<T> splitter);

        /**
         * Dump the contents of collection to the file.
         */
        public void writeOutput(String fileName, Dumper<T> dumper);
    }

ShardedDataCollection

    It's a form of DataCollection that is split into shards wherein a function can be
    applied on the Shards independently. The ShardedCollection can be created by coding
    a Splitter interface and splitting a data collection using that splitter.

    public interface Splitter<T> extends Executable
    {
        /**
         * Returns the number of partitions.
         */
        public int numPartitions();

        /**
         * Splits the data into multiple partitions.
         */
        public void split(Input<T>        input,
                          List<Output<T>> outputs);
    }

    A simple hash based splitter is as follows.

     /**
     * {@inheritDoc}
     */
    @Override
    public void split(Input<T> input, List<Output<T>> outputs)
    {
        int rc = 0;
        for (T record : new IteratorWrapper<T>(input.getIterator())) {
            int outIndex = rc % myNumPartitions;
            Output<T> output = outputs.get(outIndex);
            output.write(record);
            rc++;
        }

        for (Output<T> output : outputs) {
            output.done();
        }
    }

    The multiple shards can be merged into a DataCollection again by using an Merger interface.

    public interface Merger<T> extends Executable
    {
        /**
         * Merges the data from different shards into a single collection back.
         */
        public void merge(List<Input<T>> shards, Output<T> output);
    }

    Following is an example of Merger that merges wordcount from multiple workers
    into the final result. A Merger can be thought of as a reduce in the context of mapreduce.

    public class WordCountMerger extends MergerBase<Map<String,Integer>>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void merge(List<Input<Map<String, Integer>>> shards,
                          Output<Map<String, Integer>> output)
        {
            Map<String,Integer> resultMap = new HashMap<String, Integer>();
            com.google.common.base.Function<String,Integer> lookupFunction =
                Functions.forMap(resultMap, 0);

            for (Input<Map<String, Integer>> shard : shards) {
                Map<String,Integer> splitWcMap = shard.getIterator().next();
                for (Map.Entry<String, Integer> entry : splitWcMap.entrySet()) {
                    resultMap.put(entry.getKey(),
                                  lookupFunction.apply(
                                      entry.getKey()) + entry.getValue());
                }
            }
            output.write(resultMap);
            output.done();
        }
    }


A simple example of word count using the above concepts is as follows.

         DataProcessor processor =
             new DataProcessor(new GraphID("WordCountTest"),
                               new FilePathGenerator("C:\\temp"));

         DataCollection<String> words =
             processor.fromInputSource(myInputFile, new SimpleInterpreter());

         ShardedDataCollection<String> partitionedInput =
             words.partition(new HashSplitter(4));

         ShardedDataCollection<Map<String,Integer>> partialResults =
             partitionedInput.apply(new CountWords());

         DataCollection<Map<String, Integer>> wordCount =
             partialResults.merge(new WordCountMerger());

         wordCount.writeOutput("wcout.txt", new ExecutableDumper());


Please note that i have developed this project a proof of concept
for having alternate data processesing frameworks other than hadoop .
If you find thisinteresting please send your suggestions for improvements
to coderholic@hotmail.com