package org.jdag.graph;

import java.util.Collections;

import org.jdag.io.IOKey;

/**
 * Special vertex that is used for representing the input data to be processed.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class InputVertex extends SimpleVertex
{
    /**
     * CTOR
     */
    public InputVertex()
    {
         super();
    }

    /**
     * CTOR
     */
      public InputVertex(VertexID id, IOKey key)
      {
          super(id,  "", Collections.<IOKey>emptyList(), Collections.singletonList(key));
      }
}
