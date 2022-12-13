package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SoundTowerTest {

    private static final float SOUND_TOWER_RANGE = Configuration.getInstance().getFloatProperty("SOUND_TOWER_RANGE");

    private static final float SLOWING_EFFECT_MULTIPLIER = Configuration.getInstance()
            .getFloatProperty("SOUND_TOWER_SLOWING_EFFECT_MULTIPLIER");

    private SoundTower soundTower;

    private Vector2 inRangePosition;

    private Vector2 outOfRangePosition;

    @BeforeAll
    static void setUpBeforeAll() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @BeforeEach
    void setUp() {
        soundTower = new SoundTower(Vector2.ZERO, null);
        inRangePosition = Vector2.ZERO;
        outOfRangePosition = new Vector2(SOUND_TOWER_RANGE + 1, 0);
    }

    @Test
    void whenTowerTargetsHostileUnitInRangeAndNoKillOrder_thenHostileUnitIsNotSlowedDown() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithSpeed(inRangePosition, 100);

        Assertions.assertFalse(soundTower.target(false, hostileUnit));

        Assertions.assertEquals(100, hostileUnit.getCurrentSpeed());
    }

    @Test
    void whenTowerTargetsHostileUnitInRangeAndKillOrder_thenHostileUnitIsSlowedDown() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithSpeed(inRangePosition, 100);

        Assertions.assertTrue(soundTower.target(true, hostileUnit));

        var expectedSpeed = SLOWING_EFFECT_MULTIPLIER * 100;
        Assertions.assertEquals(expectedSpeed, hostileUnit.getCurrentSpeed());
    }

    @Test
    void whenTowerTargetsHostileUnitOutOfRangeAndNoKillOrder_thenHostileUnitIsNotSlowedDown() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithSpeed(outOfRangePosition, 100);

        Assertions.assertFalse(soundTower.target(false, hostileUnit));

        Assertions.assertEquals(100, hostileUnit.getCurrentSpeed());
    }

    @Test
    void whenTowerTargetsHostileUnitOutOfRangeAndKillOrder_thenHostileUnitIsNotSlowedDown() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithSpeed(outOfRangePosition, 100);

        Assertions.assertFalse(soundTower.target(true, hostileUnit));

        Assertions.assertEquals(100, hostileUnit.getCurrentSpeed());
    }
}
