package org.jdryad.dag.builder;

import org.jdryad.dag.IOKey;
import org.jdryad.dag.VertexID;
import org.jdryad.dag.IOKey.SourceType;

/**
 * Implements <code>IOKeyFactory<code> where the persistence medium is a
 * shared file system.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FileIOKeyFactory implements IOKeyFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public IOKey makeKey(String ioSrc)
    {
        return new IOKey(SourceType.FILE, ioSrc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IOKey makeKey(String ioSrc, int numSplit)
    {
        return new IOKey(SourceType.FILE, ioSrc + numSplit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IOKey makeKey(VertexID src, int splitNum)
    {
        return new IOKey(SourceType.FILE, src.getName() + splitNum);
    }
}
