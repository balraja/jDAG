package org.jdryad.persistence.iokeycodec;

import java.util.Collections;
import java.util.List;

import org.jdryad.common.Pair;

/**
 * The codec to be used for interpreting keys of type
 * <code>IOSource#SERIALIZED_FILE</code>.
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