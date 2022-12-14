package io.swapastack.dunetd;

import io.swapastack.dunetd.config.Configuration;

import java.io.IOException;
import java.util.Properties;

public final class TestHelper {

    public static void readConfigFile() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Get properties field and initialize it
        var propertiesField = Configuration.class.getDeclaredField("properties");
        propertiesField.setAccessible(true);
        var properties = (Properties) propertiesField.get(null);
        properties = new Properties();

        try (var inputStream = TestHelper.class.getClassLoader().getResourceAsStream("test_config.properties")) {
            properties.load(inputStream);
        }

        propertiesField.set(null, properties);
    }
}
