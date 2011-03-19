package org.jdag.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * A class that proxies the configuration interface and provides configuration
 * details from the underlying properties file using apache configuration.
 *
 * @author Balraja Subbiah
 * @version $Id:$
 */
public final class ConfigurationProvider implements InvocationHandler
{
    private final String myBasePrefix;

    private final PropertiesConfiguration myPropertiesConfiguration;

    private final Class<?> myConfigDefnClass;

    /**
     * CTOR
     */
    private ConfigurationProvider(String src,
                                  String basePrefix,
                                  Class<?> configDefinitionClass)
        throws ConfigurationException
    {
        myPropertiesConfiguration = new PropertiesConfiguration(src);
        myBasePrefix = basePrefix;
        myConfigDefnClass = configDefinitionClass;
    }

    /**
     * CTOR
     */
    private ConfigurationProvider(String basePrefix,
                                  Class<?> configDefinitionClass)
    {
        myBasePrefix = basePrefix;
        myPropertiesConfiguration = null;
        myConfigDefnClass = configDefinitionClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
    {
        try {
            Method actualDefnMethod =
                myConfigDefnClass.getDeclaredMethod(method.getName());
            PropertyDef def = actualDefnMethod.getAnnotation(PropertyDef.class);
            String property = myBasePrefix + "." + def.name();
            String value = System.getProperty(property);
            switch (def.resultType()) {
            case INT:
                return value != null ? Integer.parseInt(value)
                                     : myPropertiesConfiguration != null ?
                                           myPropertiesConfiguration.getInt(property)
                                           : null;
            case STRING:
                return value != null ? value
                                     : myPropertiesConfiguration != null ?
                                           myPropertiesConfiguration.getProperty(property)
                                           : null;
            case BOOLEAN:
                return value != null ? Boolean.parseBoolean(value)
                                     : myPropertiesConfiguration != null ?
                                           myPropertiesConfiguration.getBoolean(property)
                                           : null;
            case SOCKET_ADDRESS:
                return value != null ?
                        new InetSocketAddress(Integer.parseInt(value))
                        : myPropertiesConfiguration != null ?
                              new InetSocketAddress(
                                  myPropertiesConfiguration.getInt(property))
                              : null;
            }
            return null;
        }
        catch (SecurityException e) {
             throw new RuntimeException(e);
        }
        catch (NoSuchMethodException e) {
             throw new RuntimeException(e);
        }
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
            ConfigurationProvider provider =
                propertyFile != null ?
                    new ConfigurationProvider(
                            propertyFile, s.prefix(), configDefinitionClass)
                    : new ConfigurationProvider(
                            s.prefix(), configDefinitionClass);

            return (T) Proxy.newProxyInstance(
                configDefinitionClass.getClassLoader(),
                new Class<?>[]{configDefinitionClass},
                provider);
        }
        catch (IllegalArgumentException e) {
             throw new RuntimeException(e);
        }
        catch (ConfigurationException e) {
             throw new RuntimeException(e);
        }
    }
}
