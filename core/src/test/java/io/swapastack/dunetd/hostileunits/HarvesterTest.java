package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import org.junit.Test;

import static org.junit.Assert.*;

public class HarvesterTest {

    static {
        TestHelper.readConfigFile();
    }

    private static final int HARVESTER_SPICE_REWARD = Configuration.getInstance().getIntProperty("HARVESTER_SPICE_REWARD");
    private static final float SLOWING_EFFECT_RESISTANCE_MULTIPLIER = Configuration.getInstance().getFloatProperty("HARVESTER_SLOWING_EFFECT_RESISTANCE_MULTIPLIER");

    @Test
    public void testConstructor1WithValidArguments() {
        var harvester = new Harvester(Vector2.Zero);
        assertNotNull(harvester);
    }

    @Test
    public void testConstructor1WithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new Harvester(null));
    }

    @Test
    public void testConstructor2WithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new Harvester(null, null));
        assertThrows(IllegalArgumentException.class, () -> new Harvester(Vector2.Zero, null));
    }

    @Test
    public void testSlowDown() {
        for (int i = 1; i < 10; i++) {
            var harvester = new Harvester(Vector2.Zero);
            var slowingEffectMultiplier = 0.1f * i;
            harvester.slowDown(slowingEffectMultiplier, 100);
            var tmp = MathUtils.clamp(slowingEffectMultiplier * SLOWING_EFFECT_RESISTANCE_MULTIPLIER, 0f, 1f);
            assertEquals(harvester.speed * tmp, harvester.currentSpeed, 0f);
            assertTrue(harvester.speed >= harvester.currentSpeed);
        }
    }

    @Test
    public void testGetSpiceReward() {
        var harvester = new Harvester(Vector2.Zero);
        assertEquals(HARVESTER_SPICE_REWARD, harvester.getSpiceReward());
    }
}