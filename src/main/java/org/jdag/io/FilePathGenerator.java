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
