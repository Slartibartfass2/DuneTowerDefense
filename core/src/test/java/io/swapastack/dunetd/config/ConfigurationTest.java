package io.swapastack.dunetd.config;

import io.swapastack.dunetd.TestHelper;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigurationTest {

    static {
        TestHelper.readConfigFile();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(Configuration.getInstance());
    }

    @Test
    public void testGetIntPropertyWithValidArgument() {
        assertEquals(20, Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH"));
    }

    @Test
    public void testGetIntPropertyWithInvalidArgument() {
        var config = Configuration.getInstance();
        assertThrows(IllegalArgumentException.class, () -> config.getIntProperty(null));
        assertThrows(IllegalArgumentException.class, () -> config.getIntProperty("SPASS_AM_STUDIEREN_WÄHREND_CORONA"));
    }

    @Test
    public void testGetFloatPropertyWithValidArgument() {
        assertEquals(0.1f, Configuration.getInstance().getFloatProperty("TOWER_TEAR_DOWN_REFUND"), 0.001f);
    }

    @Test
    public void testGetFloatPropertyWithInvalidArgument() {
        var config = Configuration.getInstance();
        assertThrows(IllegalArgumentException.class, () -> config.getFloatProperty(null));
        assertThrows(IllegalArgumentException.class, () -> config.getFloatProperty("SPASS_AM_STUDIEREN_WÄHREND_CORONA"));
    }
}