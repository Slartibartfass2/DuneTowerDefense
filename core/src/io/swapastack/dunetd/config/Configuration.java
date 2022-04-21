package io.swapastack.dunetd.config;

import lombok.NonNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Reader for config.properties file. Uses singleton pattern to only read config once.
 */
public final class Configuration {
    
    /** Name of configuration file in properties format */
    private static final String CONFIG_FILE_NAME = "core/assets/config.properties";
    
    /** Singleton reference (singleton pattern) */
    private static Configuration singleton;
    
    /** Properties, where all configuration values are stored */
    private static Properties properties;
    
    /**
     * Create new Configuration and loads properties.
     * Suppresses S3010, because properties gets manually initialized in <code>TestHelper</code> class, for test purposes only.
     */
    @SuppressWarnings("squid:S3010")
    private Configuration() {
        if (properties != null)
            return;
        
        // Load properties
        properties = new Properties();
        try (var bufferedReader = new BufferedReader(new FileReader(CONFIG_FILE_NAME))) {
            properties.load(bufferedReader);
        } catch (IOException e) {
            throw new IllegalStateException("Properties couldn't be loaded.");
        }
    }
    
    /**
     * @return Returns configuration reference
     */
    public static Configuration getInstance() {
        if (singleton == null) singleton = new Configuration();
        return singleton;
    }
    
    /**
     * Returns int value of property in configuration file with specified name.
     *
     * @param propertyName Name of property in configuration file
     * @return Int value of property
     */
    public int getIntProperty(@NonNull String propertyName) {
        var value = properties.getProperty(propertyName);
        if (value != null) return Integer.parseInt(value);
        throw new IllegalArgumentException(propertyName + " not specified in the config.properties file.");
    }
    
    /**
     * Returns float value of property in configuration file with specified name.
     *
     * @param propertyName Name of property in configuration file
     * @return Float value of property
     */
    public float getFloatProperty(@NonNull String propertyName) {
        var value = properties.getProperty(propertyName);
        if (value != null) return Float.parseFloat(value);
        throw new IllegalArgumentException(propertyName + " not specified in the config.properties file.");
    }
}