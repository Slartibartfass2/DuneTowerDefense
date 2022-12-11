package io.swapastack.dunetd.config;

import io.swapastack.dunetd.TestHelper;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ConfigurationTest {

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testGetInstance() {
        Assertions.assertNotNull(Configuration.getInstance());
    }

    @Test
    void testGetIntPropertyWithValidArgument() {
        Assertions.assertEquals(20, Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH"));
    }

    @Test
    void testGetIntPropertyWithInvalidArgument() {
        var config = Configuration.getInstance();
        Assertions.assertThrows(IllegalArgumentException.class, () -> config.getIntProperty(null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> config.getIntProperty("SPASS_AM_STUDIEREN_WÄHREND_CORONA"));
    }

    @Test
    void testGetFloatPropertyWithValidArgument() {
        Assertions.assertEquals(0.1f, Configuration.getInstance().getFloatProperty("TOWER_TEAR_DOWN_REFUND"), 0.001f);
    }

    @Test
    void testGetFloatPropertyWithInvalidArgument() {
        var config = Configuration.getInstance();
        Assertions.assertThrows(IllegalArgumentException.class, () -> config.getFloatProperty(null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> config.getFloatProperty("SPASS_AM_STUDIEREN_WÄHREND_CORONA"));
    }
}
