package org.jdryad.persistence.flatfile;

import java.util.List;

import org.jdryad.common.Pair;
import org.jdryad.persistence.IOKeyCodec;

/**
 * @author Balraja Subbiah
 * @version $Id:$
 *
 */
public class FlatFileCodec implements IOKeyCodec
{
    private static final String SEPERATOR = "#";

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<String, List<Object>> getDecodedKey(String encodedKey)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEncodedKey(String identifier, Object... attributes)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
