package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GuardTowerTest {

    private static final float GUARD_TOWER_RANGE = Configuration.getInstance().getFloatProperty("GUARD_TOWER_RANGE");

    private static final int GUARD_TOWER_DAMAGE = Configuration.getInstance().getIntProperty("GUARD_TOWER_DAMAGE");

    private GuardTower guardTower;

    private Vector2 inRangePosition;

    private Vector2 outOfRangePosition;

    @BeforeAll
    static void setUpBeforeAll() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @BeforeEach
    void setUp() {
        guardTower = new GuardTower(Vector2.ZERO, null);
        inRangePosition = Vector2.ZERO;
        outOfRangePosition = new Vector2(GUARD_TOWER_RANGE + 1, 0);
    }

    @Test
    void whenTowerTargetsHostileUnitInRangeAndNoKillOrder_thenHostileUnitIsNotDamaged() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithHealth(inRangePosition, 100);

        Assertions.assertFalse(guardTower.target(false, hostileUnit));

        Assertions.assertEquals(100, hostileUnit.getHealth());
    }

    @Test
    void whenTowerTargetsHostileUnitInRangeAndKillOrder_thenHostileUnitIsDamaged() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithHealth(inRangePosition, 100);

        Assertions.assertTrue(guardTower.target(true, hostileUnit));

        Assertions.assertEquals(100 - GUARD_TOWER_DAMAGE, hostileUnit.getHealth());
    }

    @Test
    void whenTowerTargetsHostileUnitOutOfRangeAndNoKillOrder_thenHostileUnitIsNotDamaged() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithHealth(outOfRangePosition, 100);

        Assertions.assertFalse(guardTower.target(false, hostileUnit));

        Assertions.assertEquals(100, hostileUnit.getHealth());
    }

    @Test
    void whenTowerTargetsHostileUnitOutOfRangeAndKillOrder_thenHostileUnitIsNotDamaged() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithHealth(outOfRangePosition, 100);

        Assertions.assertFalse(guardTower.target(true, hostileUnit));

        Assertions.assertEquals(100, hostileUnit.getHealth());
    }
}
