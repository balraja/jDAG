package org.jdryad.persistence.file;

import java.util.Collections;
import java.util.List;

import org.jdryad.common.Pair;
import org.jdryad.persistence.IOKeyCodec;

/**
 * The codec to be used for interpreting keys of type
 * <code>IOSoutce#SERIALIZED_FILE</code>.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class SerrializedIOKeyCodec implements IOKeyCodec
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Pair<String, List<Object>> getDecodedKey(String encodedKey)
    {
        return new Pair<String, List<Object>>(
            encodedKey, Collections.emptyList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEncodedKey(String identifier, Object... attributes)
    {
        return identifier;
    }

}
