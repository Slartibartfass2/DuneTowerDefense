package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.hostileunits.BossUnit;
import io.swapastack.dunetd.hostileunits.Harvester;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.hostileunits.Infantry;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.NonNull;

class TowerTest {

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testGetHostileUnitsInRangeWithHostileUnitsInRange() {
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(Vector2.ZERO),
            new Harvester(Vector2.ZERO),
            new BossUnit(Vector2.ZERO),
        }).toList();
        var hostileUnitsInRange = Tower.getHostileUnitsInRange(hostileUnits, Vector2.ZERO, 100);
        Assertions.assertNotNull(hostileUnitsInRange);
        Assertions.assertEquals(3, hostileUnitsInRange.size());
    }

    @Test
    void testGetHostileUnitsInRangeWithHostileUnitsOutOfRange() {
        int range = 10;
        int outOfRange = range + 1;
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(new Vector2(outOfRange, 0f)),
            new Harvester(new Vector2(outOfRange, 0f)),
            new BossUnit(new Vector2(outOfRange, 0f)),
        }).toList();
        var hostileUnitsInRange = Tower.getHostileUnitsInRange(hostileUnits, Vector2.ZERO, range * range);
        Assertions.assertNotNull(hostileUnitsInRange);
        Assertions.assertEquals(0, hostileUnitsInRange.size());
    }

    @Test
    void testGetHostileUnitsInRangeWithTowerInRange() {
        var tower = getNewTower(0, 0, 10, 10, 100);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(Vector2.ZERO),
            new Harvester(Vector2.ZERO),
            new BossUnit(Vector2.ZERO),
        }).toList();
        var hostileUnitsInRange = tower.getHostileUnitsInRange(hostileUnits);
        Assertions.assertNotNull(hostileUnitsInRange);
        Assertions.assertEquals(3, hostileUnitsInRange.size());
    }

    @Test
    void testGetHostileUnitsInRangeWithTowerOutOfRange() {
        int range = 10;
        int outOfRange = range + 1;
        var tower = getNewTower(0, 0, range, 10, 100);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(new Vector2(outOfRange, 0f)),
            new Harvester(new Vector2(outOfRange, 0f)),
            new BossUnit(new Vector2(outOfRange, 0f)),
        }).toList();
        var hostileUnitsInRange = tower.getHostileUnitsInRange(hostileUnits);
        Assertions.assertNotNull(hostileUnitsInRange);
        Assertions.assertEquals(0, hostileUnitsInRange.size());
    }

    @Test
    void testSetToDebris() {
        var tower = getNewRandomTower();
        tower.setToDebris();
        Assertions.assertTrue(tower.isDebris());
    }

    @Test
    void testGetBuildCost() {
        var tower = getNewRandomTower();
        Assertions.assertEquals(tower.getBuildCost(), tower.getBuildCost());
    }

    @Test
    void testIsDebris() {
        var tower = getNewRandomTower();
        tower.setToDebris();
        Assertions.assertTrue(tower.isDebris());
    }

    @Test
    void testEquals() {
        var tower = getNewRandomTower();
        var tower1 = getNewRandomTower();

        Assertions.assertNotEquals(tower, tower1);

        var tower2 = getNewTower(0, 0, 0, 0, 0);
        var tower3 = getNewTower(0, 0, 0, 0, 0);

        Assertions.assertNotEquals(tower2, tower3);
    }

    Tower getNewRandomTower() {
        var position = new Vector2(new Random().nextInt(), new Random().nextInt());
        return new Tower(position, new Random().nextFloat(), new Random().nextInt(), new Random().nextInt(), null, 0f) {
            @Override
            protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
                return false;
            }
        };
    }

    Tower getNewTower(int x, int y, float range, int buildCost, int reloadTimeInMs) {
        var position = new Vector2(x, y);
        return new Tower(position, range, buildCost, reloadTimeInMs, null, 0f) {
            @Override
            protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
                return false;
            }
        };
    }
}
