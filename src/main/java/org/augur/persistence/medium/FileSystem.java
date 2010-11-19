package org.augur.persistence.medium;

import com.google.common.base.Preconditions;

import java.io.File;

import org.augur.dag.ExecutionGraphID;
import org.augur.dag.IOKey;
import org.augur.dag.IOSource;

/**
 * This type should be used when the data is stored in the file system.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class FileSystem implements PersistenceMedium
{
    private final String myRootDirectory;

    private final ExecutionGraphID myExecutionGraphID;

    /**
     * CTOR
     */
    public FileSystem(String rootDirectory, ExecutionGraphID executionGraphID)
    {
        myRootDirectory = rootDirectory;
        myExecutionGraphID = executionGraphID;
        File root = new File(myRootDirectory);
        if (!root.exists()) {
            root.mkdirs();
        }
        File graphDir = new File(root, myExecutionGraphID.getID());
        if (!graphDir.exists()) {
            graphDir.mkdir();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IOKey makeKey(String key, IOSource source)
    {
        Preconditions.checkArgument(source == IOSource.SERIALIZED_FILE
                                    || source == IOSource.FLAT_FILE);
        String intermediateFile =
            myRootDirectory + File.separator + key;
        return new IOKey(source, intermediateFile);
    }
}
