package org.augur.common.log;

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
    private static final String PROP_LOG_FILE =
        "org.jdag.logfile";

    private static final FileHandler ourLogFileHandler;

    static {
        String fileName = System.getProperty(PROP_LOG_FILE);
        if (fileName == null) {
            throw new RuntimeException("Log file not specified");
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
}
