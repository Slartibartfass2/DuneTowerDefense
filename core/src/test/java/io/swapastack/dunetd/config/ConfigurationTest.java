package io.swapastack.dunetd.config;

import io.swapastack.dunetd.TestHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    static {
        TestHelper.readConfigFile();
    }

    @Test
    void testGetInstance() {
        assertNotNull(Configuration.getInstance());
    }

    @Test
    void testGetIntPropertyWithValidArgument() {
        assertEquals(20, Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH"));
    }

    @Test
    void testGetIntPropertyWithInvalidArgument() {
        var config = Configuration.getInstance();
        assertThrows(IllegalArgumentException.class, () -> config.getIntProperty(null));
        assertThrows(IllegalArgumentException.class, () -> config.getIntProperty("SPASS_AM_STUDIEREN_WÄHREND_CORONA"));
    }

    @Test
    void testGetFloatPropertyWithValidArgument() {
        assertEquals(0.1f, Configuration.getInstance().getFloatProperty("TOWER_TEAR_DOWN_REFUND"), 0.001f);
    }

    @Test
    void testGetFloatPropertyWithInvalidArgument() {
        var config = Configuration.getInstance();
        assertThrows(IllegalArgumentException.class, () -> config.getFloatProperty(null));
        assertThrows(IllegalArgumentException.class, () -> config.getFloatProperty("SPASS_AM_STUDIEREN_WÄHREND_CORONA"));
    }
}
