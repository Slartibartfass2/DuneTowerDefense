package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BossUnitTest {

    static {
        TestHelper.readConfigFile();
    }

    private static final int BOSS_UNIT_SPICE_REWARD = Configuration.getInstance().getIntProperty("BOSS_UNIT_SPICE_REWARD");

    @Test
    void testSlowDown() {
        for (int i = 1; i < 10; i++) {
            var bossUnit = new BossUnit(Vector2.Zero);
            var slowingEffectMultiplier = 0.1f * i;
            bossUnit.slowDown(slowingEffectMultiplier, 100);
            assertEquals(bossUnit.speed * slowingEffectMultiplier, bossUnit.currentSpeed, 0f);
            assertTrue(bossUnit.speed >= bossUnit.currentSpeed);
        }
    }

    @Test
    void testGetSpiceReward() {
        var bossUnit = new BossUnit(Vector2.Zero);
        assertEquals(BOSS_UNIT_SPICE_REWARD, bossUnit.getSpiceReward());
    }
}