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

import org.jdag.config.ExpectedResult;
import org.jdag.config.PropertyDef;
import org.jdag.config.Source;

/**
 * The configuration to be used by <code>PersistentDSManager</code>.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
@Source(prefix="jdag.persistentds")
public interface PersistentDSManagerConfig
{
    @PropertyDef(name="rootDir", resultType=ExpectedResult.STRING)
    public String getRootDirectory();
}
