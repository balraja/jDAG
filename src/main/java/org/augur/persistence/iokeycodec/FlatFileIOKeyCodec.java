package org.augur.persistence.iokeycodec;

import com.google.common.collect.Lists;

import java.util.List;

import org.augur.common.Pair;

/**
 * Implements <code>IOKeyCodec</code> that encapsulates the line reader to be
 * used for deserializing the records from a file.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FlatFileIOKeyCodec implements IOKeyCodec
{
    private static final String SEPERATOR = "#";

    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<String, List<Object>> getDecodedKey(String encodedKey)
    {
        String[] splits = encodedKey.split(SEPERATOR);
        return new Pair<String,List<Object>>(splits[0],
                                             Lists.<Object>newArrayList(splits[1]));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEncodedKey(String identifier, Object... attributes)
    {
        return identifier + SEPERATOR + attributes[0];
    }
}
