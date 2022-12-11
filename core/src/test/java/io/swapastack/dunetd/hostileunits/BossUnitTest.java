package io.swapastack.dunetd.hostileunits;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BossUnitTest {

    private static final int BOSS_UNIT_SPICE_REWARD = Configuration.getInstance()
            .getIntProperty("BOSS_UNIT_SPICE_REWARD");

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testGetSpiceReward() {
        var bossUnit = new BossUnit(Vector2.ZERO);
        Assertions.assertEquals(BOSS_UNIT_SPICE_REWARD, bossUnit.getSpiceReward());
    }
}
