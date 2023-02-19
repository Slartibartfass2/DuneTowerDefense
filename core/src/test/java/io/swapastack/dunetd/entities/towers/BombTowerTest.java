package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BombTowerTest {

    private static final float BOMB_TOWER_RANGE = Configuration.getInstance().getFloatProperty("BOMB_TOWER_RANGE");

    private static final int BOMB_TOWER_DAMAGE = Configuration.getInstance().getIntProperty("BOMB_TOWER_DAMAGE");

    private static final float BOMB_TOWER_AREA_DAMAGE_RANGE = Configuration.getInstance()
            .getFloatProperty("BOMB_TOWER_AREA_DAMAGE_RANGE");

    private BombTower bombTower;

    private Vector2 inRangePosition;

    private Vector2 outOfRangePosition;

    private Vector2 justInRangePosition;

    @BeforeAll
    static void setUpBeforeAll() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @BeforeEach
    void setUp() {
        bombTower = new BombTower(Vector2.ZERO, null);
        inRangePosition = Vector2.ZERO;
        outOfRangePosition = new Vector2(BOMB_TOWER_RANGE + 1, 0);
        justInRangePosition = new Vector2(BOMB_TOWER_RANGE, 0);
    }

    @Test
    void whenTowerTargetsHostileUnitInRangeAndNoKillOrder_thenHostileUnitIsNotDamaged() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithHealth(inRangePosition, 100);

        Assertions.assertFalse(bombTower.target(false, hostileUnit));

        Assertions.assertEquals(100, hostileUnit.getHealth());
    }

    @Test
    void whenTowerTargetsHostileUnitInRangeAndKillOrder_thenHostileUnitIsDamaged() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithHealth(inRangePosition, 100);

        Assertions.assertTrue(bombTower.target(true, hostileUnit));

        Assertions.assertEquals(100 - BOMB_TOWER_DAMAGE, hostileUnit.getHealth());
    }

    @Test
    void whenTowerTargetsHostileUnitOutOfRangeAndNoKillOrder_thenHostileUnitIsNotDamaged() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithHealth(outOfRangePosition, 100);

        Assertions.assertFalse(bombTower.target(false, hostileUnit));

        Assertions.assertEquals(100, hostileUnit.getHealth());
    }

    @Test
    void whenTowerTargetsHostileUnitOutOfRangeAndKillOrder_thenHostileUnitIsNotDamaged() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithHealth(outOfRangePosition, 100);

        Assertions.assertFalse(bombTower.target(true, hostileUnit));

        Assertions.assertEquals(100, hostileUnit.getHealth());
    }

    @Test
    void whenTowerTargetsHostileUnitInRangeAndOtherHostileUnitInAreaRangeAndKillOrder_thenBothHostileUnitsAreDamaged() {
        var hostileUnit = TowerTestHelper.createHostileUnitWithHealth(justInRangePosition, 100);
        var inAreDamageRange = Vector2.add(justInRangePosition, new Vector2(BOMB_TOWER_AREA_DAMAGE_RANGE, 0));
        var hostileUnitInAreDamageRange = TowerTestHelper.createHostileUnitWithHealth(inAreDamageRange, 100);

        Assertions.assertTrue(bombTower.target(true, hostileUnit, hostileUnitInAreDamageRange));

        Assertions.assertEquals(100 - BOMB_TOWER_DAMAGE, hostileUnit.getHealth());
        Assertions.assertEquals(100 - BOMB_TOWER_DAMAGE, hostileUnitInAreDamageRange.getHealth());
    }
}
