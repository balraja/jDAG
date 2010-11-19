package org.augur.persistence.iokeycodec;

import java.util.List;

import org.augur.common.Pair;

/**
 * A type that encapsulates the specification for encoding the arguments into
 * the io key.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public interface IOKeyCodec
{
    /**
     * Returns an encoded key generated from the identifier and from various
     * attributes.
     */
    public String getEncodedKey(String identifier, Object... attributes);

    /**
     * Returns the key and various other attributes encoded as part of the
     * key.
     */
    public Pair<String, List<Object>> getDecodedKey(String encodedKey);

}
