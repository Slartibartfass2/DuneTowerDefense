package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfantryTest {

    static {
        TestHelper.readConfigFile();
    }

    private static final int INFANTRY_SPICE_REWARD = Configuration.getInstance()
            .getIntProperty("INFANTRY_SPICE_REWARD");

    @Test
    void testSlowDown() {
        for (int i = 1; i < 10; i++) {
            var infantry = new Infantry(Vector2.Zero);
            var slowingEffectMultiplier = 0.1f * i;
            infantry.slowDown(slowingEffectMultiplier, 100);
            assertEquals(infantry.speed * slowingEffectMultiplier, infantry.currentSpeed, 0f);
            assertTrue(infantry.speed >= infantry.currentSpeed);
        }
    }

    @Test
    void testGetSpiceReward() {
        var infantry = new Infantry(Vector2.Zero);
        assertEquals(INFANTRY_SPICE_REWARD, infantry.getSpiceReward());
    }
}
