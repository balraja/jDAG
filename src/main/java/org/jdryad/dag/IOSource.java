package org.jdryad.dag;

import java.util.HashMap;
import java.util.Map;

/**
 * An enum to denote the type of the i/p or o/p source
 */
public enum IOSource
{
    SERIALIZED_FILE(1),
    FLAT_FILE(2);

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

    IOSource(int typeId)
    {
    	myTypeId = typeId;
	}

    public int getTypeID()
    {
    	return myTypeId;
    }
}