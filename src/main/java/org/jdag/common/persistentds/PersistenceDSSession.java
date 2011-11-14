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
