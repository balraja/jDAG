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

package org.jdag.common;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdag.common.log.LogFactory;

/**
 * A simple class that takes care of running an <code>Application</code>
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ApplicationExecutor
{
    /** The logger */
    private final Logger LOG =  LogFactory.getLogger(ApplicationExecutor.class);

    private final Application myApplication;

    private static class TerminatingThreadGroup extends ThreadGroup
    {
        /**
         * CTOR
         */
        public TerminatingThreadGroup(String name)
        {
            super(name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void uncaughtException(Thread t, Throwable e)
        {
            System.exit(0);
        }
    }

    /**
     * CTOR
     */
    public ApplicationExecutor(Application application)
    {
        myApplication = application;
    }

    /**
     * Starts the application.
     */
    public void run()
    {
        TerminatingThreadGroup tgp =
            new TerminatingThreadGroup(
                myApplication.getClass().getSimpleName() + " App");

        Thread t = new Thread(tgp, new Runnable() {

            /**
             * {@inheritDoc}
             */
            @Override
            public void run()
            {
                try {
                    myApplication.start();
                    while (true) {
                        // loop infinitely for application.
                    }
                }
                catch (Throwable e) {
                    LOG.log(Level.SEVERE, " exception", e);
                    throw new RuntimeException(e);
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run()
            {
                myApplication.stop();
            }
        });
        t.setName(myApplication.getClass().getSimpleName() + "-AppThread");
        t.start();
    }
}
