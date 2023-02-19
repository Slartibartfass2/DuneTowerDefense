package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.MathUtils;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HarvesterTest {

    private static final float SLOWING_EFFECT_RESISTANCE_MULTIPLIER = Configuration.getInstance()
            .getFloatProperty("HARVESTER_SLOWING_EFFECT_RESISTANCE_MULTIPLIER");

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testSlowDown() {
        for (int i = 1; i < 10; i++) {
            var harvester = new Harvester(Vector2.ZERO);
            var slowingEffectMultiplier = 0.1f * i;
            harvester.slowDown(slowingEffectMultiplier, 100);
            var tmp = MathUtils.clamp(slowingEffectMultiplier * SLOWING_EFFECT_RESISTANCE_MULTIPLIER, 0f, 1f);
            Assertions.assertEquals(harvester.getSpeed() * tmp, harvester.getCurrentSpeed(), 0f);
            Assertions.assertTrue(harvester.getSpeed() >= harvester.getCurrentSpeed());
        }
    }
}
