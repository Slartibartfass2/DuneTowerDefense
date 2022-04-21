package io.swapastack.dunetd;

import io.swapastack.dunetd.config.Configuration;

import java.io.*;
import java.util.Properties;

public class TestHelper {
    
    private static final String TEST_CONFIG_FILE = "src/test/java/io/swapastack/dunetd/test_config.properties";
    
    public static void readConfigFile() {
        try {
            // Get properties field and initialize it
            var propertiesField = Configuration.class.getDeclaredField("properties");
            propertiesField.setAccessible(true);
            var properties = (Properties) propertiesField.get(null);
            properties = new Properties();
            
            // Load properties from test config file
            try (var inputStream = new BufferedReader(new FileReader(TEST_CONFIG_FILE))) {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            propertiesField.set(null, properties);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}
