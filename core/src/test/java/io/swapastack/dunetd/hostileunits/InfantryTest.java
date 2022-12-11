package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.Vector2;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InfantryTest {

    private static final int INFANTRY_SPICE_REWARD = Configuration.getInstance()
            .getIntProperty("INFANTRY_SPICE_REWARD");

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testSlowDown() {
        for (int i = 1; i < 10; i++) {
            var infantry = new Infantry(Vector2.Zero);
            var slowingEffectMultiplier = 0.1f * i;
            infantry.slowDown(slowingEffectMultiplier, 100);
            Assertions.assertEquals(infantry.speed * slowingEffectMultiplier, infantry.currentSpeed, 0f);
            Assertions.assertTrue(infantry.speed >= infantry.currentSpeed);
        }
    }

    @Test
    void testGetSpiceReward() {
        var infantry = new Infantry(Vector2.Zero);
        Assertions.assertEquals(INFANTRY_SPICE_REWARD, infantry.getSpiceReward());
    }
}
