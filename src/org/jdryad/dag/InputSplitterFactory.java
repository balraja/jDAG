package org.jdryad.dag;

import java.util.Set;

import org.apache.commons.collections.set.ListOrderedSet;

/**
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class InputSplitterFactory
{
    /**
     * An enum to denote the various split functions that can be created
     * by this factory.
     */
    public static enum SplitterType
    {
        MOD,

        DIRECT
    }


    /**
     * Splitter selects the o/p based on the position of record in the
     * input.
     */
    private static class ModSplitter implements InputSplitter
    {

        /**
         * {@inheritDoc}
         */
        @Override
        public IOKey getOutput(Record record,
                               int position,
                               IOKey input,
                               Set<IOKey> outputs)
        {
            ListOrderedSet setAsList = ListOrderedSet.decorate(outputs);
            int index = position % (outputs.size());
            return (IOKey) setAsList.get(index);
        }
    }

    /**
     * Splitter selects the o/p based on the position of record in the
     * input.
     */
    private static class DirectSplitter implements InputSplitter
    {

        /**
         * {@inheritDoc}
         */
        @Override
        public IOKey getOutput(Record record,
                               int position,
                               IOKey input,
                               Set<IOKey> outputs)
        {
            ListOrderedSet setAsList = ListOrderedSet.decorate(outputs);
            int index = (position / (outputs.size()));
            return (IOKey) setAsList.get(index);
        }
    }


    /**
     * Factory method for creating a splitter based on the SplitterType.
     */
    public static InputSplitter makeSpliter(SplitterType type)
    {
        if (type == SplitterType.MOD) {
            return new ModSplitter();
        }
        else if (type == SplitterType.DIRECT) {
            return new DirectSplitter();
        }
        return null;
    }

    /** private ctor */
    private InputSplitterFactory()
    {
    }
}
