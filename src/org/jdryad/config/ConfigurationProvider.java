package org.jdryad.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * A class that proxies the configuration interface and provides configuration
 * details from the underlying properties file using apache configuration.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public class ConfigurationProvider implements InvocationHandler
{
    private final String myBasePrefix;

    private final PropertiesConfiguration myPropertiesConfiguration;

    /**
     * CTOR
     * @throws ConfigurationException
     * @throws ConfigurationException
     */
    private ConfigurationProvider(String src, String basePrefix)
        throws ConfigurationException
    {
        myPropertiesConfiguration = new PropertiesConfiguration(src);
        myBasePrefix = basePrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
    {
        PropertyDef def = method.getAnnotation(PropertyDef.class);
        String property = myBasePrefix + "." + def.name();
        switch (def.resultType()) {
        case INT:
            return myPropertiesConfiguration.getInt(property);
        case STRING:
            return myPropertiesConfiguration.getProperty(property);
        case BOOLEAN:
            return myPropertiesConfiguration.getBoolean(property);
        }
        return null;
    }

    /**
     * A factory method that creates a proxy to read from underlying
     * property configuration.
     */
    @SuppressWarnings("unchecked")
    public static <T> T makeConfiguration(Class<T> configDefinitionClass,
                                          String propertyFile)
    {
        Source s = configDefinitionClass.getAnnotation(Source.class);
        try {
            return (T) Proxy.newProxyInstance(
                configDefinitionClass.getClassLoader(),
                new Class<?>[]{configDefinitionClass},
                new ConfigurationProvider(propertyFile, s.prefix()));
        }
        catch (IllegalArgumentException e) {
             throw new RuntimeException(e);
        }
        catch (ConfigurationException e) {
             throw new RuntimeException(e);
        }
    }
}
