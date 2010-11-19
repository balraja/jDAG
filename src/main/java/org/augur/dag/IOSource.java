package org.augur.dag;

import java.util.HashMap;
import java.util.Map;

import org.augur.persistence.iokeycodec.FlatFileIOKeyCodec;
import org.augur.persistence.iokeycodec.IOKeyCodec;
import org.augur.persistence.iokeycodec.SerializedIOKeyCodec;

/**
 * An enum to denote the type of the i/p or o/p source
 */
public enum IOSource
{
    SERIALIZED_FILE(1, new SerializedIOKeyCodec()),
    FLAT_FILE(2, new FlatFileIOKeyCodec());

    private static Map<Integer, IOSource> ourID2SrcTypeMap =
    	new HashMap<Integer, IOSource>();

    static {
    	for (IOSource src : values()) {
    		ourID2SrcTypeMap.put(src.getTypeID(), src);
    	}
    }

    /** Returns the type by identifier */
    public static IOSource getTypeByID(int identifier)
    {
    	return ourID2SrcTypeMap.get(Integer.valueOf(identifier));
    }

    private int myTypeId;

    private IOKeyCodec myIOKeyCodec;

    IOSource(int typeId, IOKeyCodec codec)
    {
    	myTypeId = typeId;
    	myIOKeyCodec = codec;
	}

    public int getTypeID()
    {
    	return myTypeId;
    }

    public IOKeyCodec getIOKeyCodec()
    {
        return myIOKeyCodec;
    }
}