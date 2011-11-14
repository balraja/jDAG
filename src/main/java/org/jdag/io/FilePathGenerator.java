/*******************************************************************************
 * jDAG is a project to build acyclic dataflow graphs for processing massive datasets.
 *
 *     Copyright (C) 2011, Author: Balraja,Subbiah
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package org.jdag.io;

import java.io.File;

import org.jdag.graph.GraphID;
import org.jdag.io.flatfile.FlatFileIOKey;

/**
 * This type should be used when the data is stored in the file system.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FilePathGenerator implements KeyGenerator
{
    private final String myRootDirectory;

    /**
     * CTOR
     */
    public FilePathGenerator(String rootDirectory)
    {
        myRootDirectory = rootDirectory;
        File root = new File(myRootDirectory);
        if (!root.exists()) {
            root.mkdirs();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IOKey generateIdentifier(GraphID graphID, String localIdentifier)
    {
        File graphDir = new File(myRootDirectory, graphID.getID());
        if (!graphDir.exists()) {
            graphDir.mkdir();
        }
        String intermediateFile = myRootDirectory + File.separator + localIdentifier;
        return new IOKey(IOSource.SERIALIZED_FILE, intermediateFile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IOKey generateFlatFileIdentifier(
            GraphID graphID, String localIdentifier, String interpreterClassName)
    {
        File graphDir = new File(myRootDirectory, graphID.getID());
        if (!graphDir.exists()) {
            graphDir.mkdir();
        }
        String intermediateFile = myRootDirectory + File.separator + localIdentifier;
        return new FlatFileIOKey(
                IOSource.FLAT_FILE, intermediateFile, interpreterClassName);
    }
}
