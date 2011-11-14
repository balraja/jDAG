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

package org.jdag.common.log;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A simple class that defines the factory for creating loggers from the class
 * name.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public final class LogFactory
{
    /** The property that defines the log file */
    public static final String PROP_LOG_FILE =
        "org.jdag.logfileName";

    private static final FileHandler ourLogFileHandler;

    static {
        String fileName = System.getProperty(PROP_LOG_FILE);
        if (fileName == null) {
            String basedir = System.getProperty("basedir");
            if (basedir == null) {
                throw new RuntimeException("Base dir not specified");
            }
            String appName = System.getProperty("app.name");
            fileName = basedir + File.separator + appName + ".log";
        }
        try {
            ourLogFileHandler = new FileHandler(fileName);
            ourLogFileHandler.setFormatter(new SimpleFormatter());
        }
        catch (SecurityException e) {
             throw new RuntimeException(e);
        }
        catch (IOException e) {
             throw new RuntimeException(e);
        }
    }

    /** Private CTOR to avoid initialization */
    private LogFactory()
    {
    }

    /** Creates a logger class and sets the appropriate handler */
    public static Logger getLogger(Class<?> loggerClass)
    {
        Logger logger = Logger.getLogger(loggerClass.getSimpleName());
        logger.addHandler(ourLogFileHandler);
        return logger;
    }

    /** Creates a logger class and sets the appropriate handler */
    public static Logger getLogger(String logName)
    {
        Logger logger = Logger.getLogger(logName);
        logger.addHandler(ourLogFileHandler);
        return logger;
    }
}
