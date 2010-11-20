package org.augur.common.persistentds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * The session class that's responsible for persisting the changes made to
 * a type of single PersistentDS.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class PersistenceDSSession
{
    private PersistentDSManagerConfig myConfig;

    private final String myDirectoryName;

    private File mySnapshotFile;

    /**
     * CTOR
     */
    public PersistenceDSSession(Class<?> persistenceClass,
                                PersistentDSManagerConfig config)
    {
        myDirectoryName =
            myConfig.getRootDirectory() + persistenceClass.getSimpleName();
        File directory = new File(myDirectoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        mySnapshotFile = null;
        File[] files = directory.listFiles();
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
                                       + new Date().toString());
             ObjectOutputStream oOut =
                 new ObjectOutputStream(new FileOutputStream(mySnapshotFile));
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
        try {
            ObjectInputStream oIn =
                new ObjectInputStream(new FileInputStream(mySnapshotFile));
            Snapshot snapshot = (Snapshot) oIn.readObject();
            oIn.close();
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
    }
}
