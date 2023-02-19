package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TowerTest {

    @BeforeAll
    static void setUpBeforeAll() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void whenTowerIsUpdatedAndTowerIsDebris_thenCurrentReloadTimeIsNotCountedDown() {
        var tower = TowerTestHelper.createTowerWithReloadTimeAndTargetReturnsTrueOnce(100);

        tower.update(50);

        Assertions.assertEquals(100, tower.getCurrentReloadTimeInMilliseconds());

        tower.setToDebris();

        tower.update(50);

        Assertions.assertEquals(100, tower.getCurrentReloadTimeInMilliseconds());
    }

    @Test
    void whenTowerIsUpdatedAndTowerIsNoDebrisAndTargetReturnsTrue_thenCurrentReloadTimeIsReset() {
        var tower = TowerTestHelper.createTowerWithReloadTimeAndTargetReturnsTrueOnce(100);

        tower.update(50);

        Assertions.assertEquals(100, tower.getCurrentReloadTimeInMilliseconds());
    }

    @Test
    void whenTowerIsUpdatedTwiceAndTowerIsNoDebris_thenCurrentReloadTimeIsCountedDown() {
        var tower = TowerTestHelper.createTowerWithReloadTimeAndTargetReturnsTrueOnce(100);

        tower.update(50);
        // Now currentReloadTimeInMilliseconds is set to 100
        tower.update(50);

        Assertions.assertEquals(50, tower.getCurrentReloadTimeInMilliseconds());
    }

    @Test
    void whenHostileUnitIsInRange_thenHostileUnitsInRangeReturnsHostileUnitInRange() {
        var tower = TowerTestHelper.createTowerWithRange(0);
        var hostileUnit = TowerTestHelper.createHostileUnitWithHealth(Vector2.ZERO, 100);

        var hostileUnitsInRange = tower.getHostileUnitsInRange(hostileUnit);

        Assertions.assertEquals(1, hostileUnitsInRange.size());
        Assertions.assertEquals(hostileUnit, hostileUnitsInRange.get(0));
    }

    @Test
    void whenHostileUnitIsOutOfRange_thenHostileUnitsInRangeReturnsEmptyList() {
        var tower = TowerTestHelper.createTowerWithRange(0);
        var hostileUnit = TowerTestHelper.createHostileUnitWithHealth(new Vector2(1, 0), 100);

        var hostileUnitsInRange = tower.getHostileUnitsInRange(hostileUnit);

        Assertions.assertEquals(0, hostileUnitsInRange.size());
    }

    @Test
    void whenHostileUnitIsInRangeAndDead_thenHostileUnitsInRangeReturnsEmptyList() {
        var tower = TowerTestHelper.createTowerWithRange(0);
        var hostileUnit = TowerTestHelper.createHostileUnitWithHealth(Vector2.ZERO, 0);

        var hostileUnitsInRange = tower.getHostileUnitsInRange(hostileUnit);

        Assertions.assertEquals(0, hostileUnitsInRange.size());
    }
}
