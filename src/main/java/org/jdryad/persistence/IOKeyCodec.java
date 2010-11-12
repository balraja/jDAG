package org.jdryad.persistence;

import java.util.List;

import org.jdryad.common.Pair;

/**
 * A type that encapsulates the specification for encoding the arguments into
 * the io key.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface IOKeyCodec
{
    public String getEncodedKey(String identifier, Object... attributes);

    public Pair<String, List<Object>> getDecodedKey(String encodedKey);

}
