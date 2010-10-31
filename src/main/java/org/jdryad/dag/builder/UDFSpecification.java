package org.jdryad.dag.builder;

import java.util.Set;

/**
 * A notational type used for defining the user defined functions and the
 * input sources for them.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class UDFSpecification
{
    private final String myName;

    private final String myIdentifier;

    private final Set<String> myInputSources;

    private final boolean myIsPartInput;

    /**
     * CTOR
     */
    public UDFSpecification(String identifier,
                            String name,
                            Set<String> inputSources,
                            boolean isPartInput)
    {
        myIdentifier = identifier;
        myName = name;
        myInputSources = inputSources;
        myIsPartInput = isPartInput;
    }

    /**
     * Returns the value of identifier
     */
    public String getIdentifier()
    {
        return myIdentifier;
    }

    /**
     * Returns the value of name
     */
    public String getName()
    {
        return myName;
    }

    /**
     * Returns the value of inputSources
     */
    public Set<String> getInputSources()
    {
        return myInputSources;
    }

    /**
     * Returns the value of isPartInput
     */
    public boolean worksOnSplitInput()
    {
        return myIsPartInput;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((myInputSources == null) ? 0 : myInputSources.hashCode());
        result = prime * result + (myIsPartInput ? 1231 : 1237);
        result = prime * result + ((myName == null) ? 0 : myName.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UDFSpecification other = (UDFSpecification) obj;
        if (myInputSources == null) {
            if (other.myInputSources != null)
                return false;
        }
        else if (!myInputSources.equals(other.myInputSources))
            return false;
        if (myIsPartInput != other.myIsPartInput)
            return false;
        if (myName == null) {
            if (other.myName != null)
                return false;
        }
        else if (!myName.equals(other.myName))
            return false;
        return true;
    }
}
