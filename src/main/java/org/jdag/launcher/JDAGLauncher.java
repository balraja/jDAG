package org.jdag.launcher;

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
import org.jdag.common.log.LogFactory;

/**
 * A simple type that reads a topology.xml file and creates master and worker
 * processes of augur.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class JDAGLauncher
{
    /** The logger */
    private static final Logger LOG =  LogFactory.getLogger(JDAGLauncher.class);

    private static final String JAVA_COMMAND =
        "java";

    private static final String MASTER_CLASS_NAME =
        "org.jdag.master.MasterExecutor";

    private static final String WORKER_CLASS_NAME =
        "org.jdag.node.NodeExecutor";

    private static final String TOPOLOGY_FILE = "topology.xml";

    private static final String OPTION_CLASSPATH_FILE_NAME = "classPathFile";

    private final XMLConfiguration myTopologyConfiguration;

    private final String myClassPath;

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
    public JDAGLauncher(String[] args)
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
                new File(commandLine.getOptionValue(OPTION_CLASSPATH_FILE_NAME));

            myClassPath =
                Files.readLines(classPathFile,
                                Charset.defaultCharset(),
                                new ClassPathLineProcessor());

            File f =
                new File(ClassLoader.getSystemClassLoader()
                                    .getResource(TOPOLOGY_FILE)
                                    .getFile());
            LOG.info("Parsing topology from " + f.toString());
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
        cmdBuilder.append(" -cp " + myClassPath);
        String name =
            myTopologyConfiguration.getString("master.name");
        cmdBuilder.append(addProperty("augur.communicator.clientName", name));
        String logFile =
            myTopologyConfiguration.getString("master.logfile");
        cmdBuilder.append(addProperty(LogFactory.PROP_LOG_FILE, logFile));
        cmdBuilder.append(MASTER_CLASS_NAME);

       try {
            LOG.info("starting master");
            LOG.info(cmdBuilder.toString());
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
        List<Object> workers =
            myTopologyConfiguration.configurationsAt("workers.worker");
        Iterator<Object> it = workers.iterator();
        while (it.hasNext())
        {
            StringBuilder cmdBuilder = new StringBuilder();
            cmdBuilder.append(JAVA_COMMAND);
            cmdBuilder.append(" ");

            cmdBuilder.append(" -cp " + myClassPath);
            cmdBuilder.append(addProperty("augur.node.masterHostID",
                    myTopologyConfiguration.getString("master.name")));
            HierarchicalConfiguration worker =
                (HierarchicalConfiguration) it.next();
            String name = worker.getString("name");
            cmdBuilder.append(addProperty("augur.communicator.clientName", name));
            String logFile =
                myTopologyConfiguration.getString("logfile");
            cmdBuilder.append(addProperty(LogFactory.PROP_LOG_FILE, logFile));

            LOG.info("Starting worker " + name );
            LOG.info(cmdBuilder.toString());
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
        return " -D" + PropertyName + "=" + value + " ";
    }

    /** Starting point for the launcher */
    public static void main(String[] args)
    {
        JDAGLauncher launcher = new JDAGLauncher(args);
        launcher.startMaster();
        launcher.startWorkers();
    }
}
