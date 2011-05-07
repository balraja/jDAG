package org.jdag.common.persistentds;

import com.google.common.base.Preconditions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

import org.jdag.common.log.LogFactory;

/**
 * The session class that's responsible for persisting the changes made to
 * a type of single PersistentDS.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class PersistenceDSSession
{
    /** The logger */
    private final Logger LOG =
        LogFactory.getLogger(PersistenceDSSession.class);

    private final PersistentDSManagerConfig myConfig;

    private final String myDirectoryName;

    private File mySnapshotFile;

    /**
     * CTOR
     */
    public PersistenceDSSession(String id,
                                PersistentDSManagerConfig config)
    {
        Preconditions.checkNotNull(config, "Config is empty");
        myConfig = config;
        myDirectoryName = myConfig.getRootDirectory() + File.separator + id;
        LOG.info("Initializing state directory as " + myDirectoryName);
        File directory = new File(myDirectoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        mySnapshotFile = null;
        File[] files = directory.listFiles();
        Preconditions.checkState(files.length <= 1);
        if (files.length == 1) {
            mySnapshotFile = files[0];
        }
    }

    /** Saves <code>Snapshot</code> to the file */
    public void saveSnapshot(Snapshot snapshot)
    {
        try {
            if (mySnapshotFile != null) {
                 mySnapshotFile.delete();
             }

             mySnapshotFile = new File(myDirectoryName
                                       + File.separator
                                       + Long.toString(System.nanoTime()));

             LOG.info("Writing state to file " + mySnapshotFile);

             ObjectOutputStream oOut =
                 new ObjectOutputStream(new FileOutputStream(mySnapshotFile));
             LOG.info(snapshot.getClass().getName());
             LOG.info(snapshot.toString());
             oOut.writeObject(snapshot);
             oOut.close();
        }
        catch (FileNotFoundException e) {
             throw new RuntimeException(e);
        }
        catch (IOException e) {
             throw new RuntimeException(e);
        }
    }

    /** Loads snapshot from the file */
    public Snapshot loadSnapshot()
    {
        if (mySnapshotFile == null) {
            return null;
        }
        
        ObjectInputStream oIn = null;
        try {
            oIn = new ObjectInputStream(new FileInputStream(mySnapshotFile));
            Snapshot snapshot = (Snapshot) oIn.readObject();
            return snapshot;
        }
        catch (FileNotFoundException e) {
             throw new RuntimeException(e);
        }
        catch (IOException e) {
             throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e) {
             throw new RuntimeException(e);
        }
        finally {
            try {
                oIn.close();
            } 
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
