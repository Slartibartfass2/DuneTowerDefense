package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import org.junit.Test;

import static org.junit.Assert.*;

public class InfantryTest {

    static {
        TestHelper.readConfigFile();
    }

    private static final int INFANTRY_SPICE_REWARD = Configuration.getInstance().getIntProperty("INFANTRY_SPICE_REWARD");

    @Test
    public void testConstructor1WithValidArguments() {
        var infantry = new Infantry(Vector2.Zero);
        assertNotNull(infantry);
    }

    @Test
    public void testConstructor1WithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new Infantry(null));
    }

    @Test
    public void testConstructor2WithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new Infantry(null, null));
        assertThrows(IllegalArgumentException.class, () -> new Infantry(Vector2.Zero, null));
    }

    @Test
    public void testSlowDown() {
        for (int i = 1; i < 10; i++) {
            var infantry = new Infantry(Vector2.Zero);
            float slowingEffectMultiplier = 0.1f * i;
            infantry.slowDown(slowingEffectMultiplier, 100);
            assertEquals(infantry.speed * slowingEffectMultiplier, infantry.currentSpeed, 0f);
            assertTrue(infantry.speed >= infantry.currentSpeed);
        }
    }

    @Test
    public void testGetSpiceReward() {
        var infantry = new Infantry(Vector2.Zero);
        assertEquals(INFANTRY_SPICE_REWARD, infantry.getSpiceReward());
    }
}