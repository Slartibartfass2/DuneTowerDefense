package io.swapastack.dunetd.entities.towers;

import com.badlogic.gdx.math.Vector2;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.hostileunits.BossUnit;
import io.swapastack.dunetd.hostileunits.Harvester;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.hostileunits.Infantry;

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
            new Infantry(Vector2.Zero),
            new Harvester(Vector2.Zero),
            new BossUnit(Vector2.Zero),
        }).toList();
        var hostileUnitsInRange = Tower.getHostileUnitsInRange(hostileUnits, Vector2.Zero, 100);
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
        var hostileUnitsInRange = Tower.getHostileUnitsInRange(hostileUnits, Vector2.Zero, range * range);
        Assertions.assertNotNull(hostileUnitsInRange);
        Assertions.assertEquals(0, hostileUnitsInRange.size());
    }

    @Test
    void testGetHostileUnitsInRangeWithTowerInRange() {
        var tower = getNewTower(0, 0, 10, 10, 100);
        var hostileUnits = Arrays.stream(new HostileUnit[]{
            new Infantry(Vector2.Zero),
            new Harvester(Vector2.Zero),
            new BossUnit(Vector2.Zero),
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
    void testRePosition() {
        var tower = getNewRandomTower();

        int x = new Random().nextInt();
        int y = new Random().nextInt();

        tower.rePosition(x, y);

        Assertions.assertEquals(x, tower.getX());
        Assertions.assertEquals(y, tower.getY());
    }

    @Test
    void testSetToDebris() {
        var tower = getNewRandomTower();
        tower.setToDebris();
        Assertions.assertTrue(tower.isDebris);
    }

    @Test
    void testGetRange() {
        var range = new Random().nextFloat();
        var tower = getNewTower(0, 0, range, 100, 100);
        Assertions.assertEquals(range, tower.range, 0f);
        Assertions.assertEquals(range, tower.getRange(), 0f);
    }

    @Test
    void testGetBuildCost() {
        var tower = getNewRandomTower();
        Assertions.assertEquals(tower.buildCost, tower.getBuildCost());
    }

    @Test
    void testIsDebris() {
        var tower = getNewRandomTower();
        tower.isDebris = true;
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
        return new Tower(new Random().nextInt(), new Random().nextInt(), new Random().nextFloat(),
                new Random().nextInt(), new Random().nextInt(), null, 0f) {
            @Override
            protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
                return false;
            }
        };
    }

    Tower getNewTower(int x, int y, float range, int buildCost, int reloadTimeInMs) {
        return new Tower(x, y, range, buildCost, reloadTimeInMs, null, 0f) {
            @Override
            protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
                return false;
            }
        };
    }
}
