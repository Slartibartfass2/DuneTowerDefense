package io.swapastack.dunetd;

import io.swapastack.dunetd.config.Configuration;

import java.io.IOException;
import java.util.Properties;

public class TestHelper {

    public static void readConfigFile() {
        try {
            // Get properties field and initialize it
            var propertiesField = Configuration.class.getDeclaredField("properties");
            propertiesField.setAccessible(true);
            var properties = (Properties) propertiesField.get(null);
            properties = new Properties();

            // Load properties from test config file
            try (var inputStream = TestHelper.class.getClassLoader().getResourceAsStream("test_config.properties")) {
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