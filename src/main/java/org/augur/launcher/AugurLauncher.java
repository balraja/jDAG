package org.augur.launcher;

import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.augur.common.log.LogFactory;

/**
 * A simple type that reads a topology.xml file and creates master and worker
 * processes of augur.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class AugurLauncher
{
    /** The logger */
    private final Logger LOG =  LogFactory.getLogger(AugurLauncher.class);

    private static final String JAVA_COMMAND =
        "C:\\Program Files\\Java\\jdk1.6.0_21\\bin\\javaw";

    private static final String MASTER_CLASS_NAME =
        "org.augur.master.MasterExecutor";

    private static final String WORKER_CLASS_NAME =
        "org.augur.node.NodeExecutor";

    private static final String TOPOLOGY_FILE = "topology.xml";

    private static final String OPTION_CLASSPATH_FILE_NAME = "classPathFile";

    private final XMLConfiguration myTopologyConfiguration;

    private final String myClassPath;

    private String myMasterName;

    /**
     * Reads lines of class path file and builds them to a single string.
     */
    private static class ClassPathLineProcessor implements LineProcessor<String>
    {
        private final StringBuilder myStringBuilder;

        /**
         * CTOR
         */
        public ClassPathLineProcessor()
        {
            myStringBuilder = new StringBuilder();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getResult()
        {
            return myStringBuilder.toString();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean processLine(String line) throws IOException
        {
            myStringBuilder.append(line);
            return true;
        }
    }

    /**
     * CTOR
     */
    @SuppressWarnings("static-access")
    public AugurLauncher(String[] args)
    {
        try {
            Option classPathFileOption =
                OptionBuilder.withDescription("<the file containing class path>")
                             .hasArg()
                             .create(OPTION_CLASSPATH_FILE_NAME);

            Options options = new Options();
            options.addOption(classPathFileOption);
            CommandLineParser commandLineParser = new GnuParser();
            CommandLine commandLine = commandLineParser.parse(options, args);

            if (!commandLine.hasOption(OPTION_CLASSPATH_FILE_NAME)) {
                LOG.severe("The file containing class path is not specified"
                           + " Hence aborting");
            }

            File classPathFile =
                new File(ClassLoader.getSystemClassLoader()
                        .getResource(commandLine.getOptionValue(
                             OPTION_CLASSPATH_FILE_NAME))
                        .getFile());

            myClassPath =
                Files.readLines(classPathFile,
                                Charset.defaultCharset(),
                                new ClassPathLineProcessor());

            File f =
                new File(ClassLoader.getSystemClassLoader()
                                    .getResource(TOPOLOGY_FILE)
                                    .getFile());
            myTopologyConfiguration = new XMLConfiguration(f);
        }
        catch (ConfigurationException e) {
             LOG.log(Level.SEVERE, "Exception when loading the topology file", e);
             throw new RuntimeException(e);
        }
        catch (ParseException e) {
            LOG.log(Level.SEVERE,
                    "Exception while parsing command line arguments",
                    e);
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE,
                    "Exception while reading class path file",
                    e);
             throw new RuntimeException(e);
        }
    }

    /** Starts the master server */
    public void startMaster()
    {
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append(JAVA_COMMAND);
        cmdBuilder.append(" ");
        cmdBuilder.append(MASTER_CLASS_NAME);
        cmdBuilder.append(" -cp " + myClassPath);
        // XXX For now everything is local host.

        String name =
            myTopologyConfiguration.getString("master.name");
        cmdBuilder.append(addProperty("augur.communicator.clientName", name));
        String logFile =
            myTopologyConfiguration.getString("master.logFile");
        cmdBuilder.append(addProperty(LogFactory.PROP_LOG_FILE, logFile));

        try {
            new ProcessBuilder(cmdBuilder.toString()).start();
        }
        catch (IOException e) {
             LOG.log(Level.SEVERE, "Unable to start master", e);
             throw new RuntimeException(e);
        }
    }

    /** Starts the workers */
    @SuppressWarnings("unchecked")
    public void startWorkers()
    {
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append(JAVA_COMMAND);
        cmdBuilder.append(" ");
        cmdBuilder.append(WORKER_CLASS_NAME);
        cmdBuilder.append(" -cp " + myClassPath);
        cmdBuilder.append(addProperty("augur.node.masterHostID", myMasterName));
        // XXX For now everything is local host.

        List<Object> workers =
            myTopologyConfiguration.configurationsAt("workers.worker");
        for(Iterator<Object> it = workers.iterator(); it.hasNext();)
        {
            HierarchicalConfiguration worker =
                (HierarchicalConfiguration) it.next();
            String name = worker.getString("name");
            cmdBuilder.append(addProperty("augur.communicator.clientName", name));
            String logFile =
                myTopologyConfiguration.getString("logFile");
            cmdBuilder.append(addProperty(LogFactory.PROP_LOG_FILE, logFile));

            try {
                new ProcessBuilder(cmdBuilder.toString()).start();
            }
            catch (IOException e) {
                 LOG.log(Level.SEVERE, "Unable to start master", e);
                 throw new RuntimeException(e);
            }
        }
    }

    /** Returns a property that can be added to a command line */
    private String addProperty(String PropertyName, String value)
    {
        return " -D " + PropertyName + "=" + value;
    }

    /** Starting point for the launcher */
    public static void main(String[] args)
    {
        AugurLauncher launcher = new AugurLauncher(args);
        launcher.startMaster();
        launcher.startWorkers();
    }
}
