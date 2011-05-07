package org.jdag.launcher;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
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
 * processes of jDAG.
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
    
    private static final String PROPERTY_BASE_DIR = "basedir";
    
    private static final String PROPERTY_APP_NAME = "app.name";

    private static final String TOPOLOGY_FILE = "topology.xml";

    private static final String OPTION_JDAG_HOME = "JDAG_HOME";
    
    private static final String LIB_DIR = "lib";
    
    private static final String CONF_DIR = "conf";

    private final XMLConfiguration myTopologyConfiguration;

    private final String myClassPath;
    
    private final String myJavaCommand;

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
                             .create(OPTION_JDAG_HOME);

            Options options = new Options();
            options.addOption(classPathFileOption);
            CommandLineParser commandLineParser = new GnuParser();
            CommandLine commandLine = commandLineParser.parse(options, args);

            if (!commandLine.hasOption(OPTION_JDAG_HOME)) {
                LOG.severe("The file containing class path is not specified"
                           + " Hence aborting");
            }

            File libDir =
                new File(commandLine.getOptionValue(OPTION_JDAG_HOME)
                        + File.separator
                        + LIB_DIR);
            
            if (!libDir.exists()) {
                throw new RuntimeException(
                    "Not able to resolve the library directory"
                    + " Please check " + libDir.getAbsolutePath()
                    + " exists");
            }
            
            File[] dependentLibraries = libDir.listFiles();
            if (dependentLibraries.length == 0) {
                throw new RuntimeException("Not able to locate dependent jars "
                                           + " in " + libDir);
            }
            
            StringBuilder builder = new StringBuilder();
            File confDir = 
                new File(commandLine.getOptionValue(OPTION_JDAG_HOME)
                         + File.separator
                         + CONF_DIR);
            if (!confDir.exists()) {
                throw new RuntimeException("Unable to locate the conf directory"
                                           + "  " + confDir.getAbsolutePath());
            }
            builder.append(confDir.getAbsolutePath());
            for (File f : dependentLibraries) {
                builder.append(File.pathSeparator);
                builder.append(f.getAbsolutePath());
            }
            myClassPath = builder.toString();
            
            String cmd = System.getenv("JAVA_HOME") 
                         + File.separator 
                         + "bin" 
                         + File.separator
                         + JAVA_COMMAND;
            
            myJavaCommand = cmd.replaceAll("Program Files", 
                                           "\"Program Files\"");
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
    }

    /** Starts the master server */
    public void startMaster()
    {
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append(myJavaCommand);
        cmdBuilder.append(" ");
        cmdBuilder.append(" -cp " + myClassPath);
        String name =
            myTopologyConfiguration.getString("master.name");
        cmdBuilder.append(addProperty("jdag.communicator.clientName", name));
        String logFile =
            myTopologyConfiguration.getString("master.logfile");
        cmdBuilder.append(addProperty(LogFactory.PROP_LOG_FILE, logFile));
        String stateDir =
            myTopologyConfiguration.getString("master.statedir");
        cmdBuilder.append(addProperty("jdag.persistentds.rootDir", stateDir));
        cmdBuilder.append(addProperty(PROPERTY_BASE_DIR, 
                                      System.getProperty(PROPERTY_BASE_DIR)));
        cmdBuilder.append(addProperty(PROPERTY_APP_NAME, name));
        cmdBuilder.append(MASTER_CLASS_NAME);
        LOG.info("starting master");
        String cmd = cmdBuilder.toString();
        LOG.info(cmd);
        
        try {
            new ProcessBuilder(Collections.<String>singletonList(cmd)).start();
        }
        catch (IOException e) {
             LOG.log(Level.SEVERE, "Unable to start master", e);
             e.printStackTrace();
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
            cmdBuilder.append(myJavaCommand);
            cmdBuilder.append(" ");
            cmdBuilder.append(" -cp " + myClassPath);
            cmdBuilder.append(addProperty("augur.node.masterHostID",
                                           myTopologyConfiguration.getString(
                                               "master.name")));
            HierarchicalConfiguration worker =
                (HierarchicalConfiguration) it.next();
            String name = worker.getString("name");
            cmdBuilder.append(addProperty("jdag.communicator.clientName", name));
            String logFile = worker.getString("logfile");
            cmdBuilder.append(addProperty(LogFactory.PROP_LOG_FILE, logFile));
            cmdBuilder.append(addProperty(PROPERTY_BASE_DIR, 
                                          System.getProperty(PROPERTY_BASE_DIR)));
            cmdBuilder.append(addProperty(PROPERTY_APP_NAME, name));
            cmdBuilder.append(WORKER_CLASS_NAME);
            LOG.info("Starting worker " + name );
            LOG.info(cmdBuilder.toString());
            try {
                new ProcessBuilder(cmdBuilder.toString()).start();
            }
            catch (IOException e) {
                 LOG.log(Level.SEVERE, "Unable to start worker", e);
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
        //launcher.startWorkers();
    }
}
