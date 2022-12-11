package io.swapastack.dunetd.hostileunits;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.GuardTower;
import io.swapastack.dunetd.pathfinding.Path;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HostileUnitTest {

    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");

    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testMoveWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var startPosition = Vector2.ZERO;
                var path = getNewPath(startPosition, width, height);
                var hostileUnit = getNewHostileUnit(startPosition, 1, 100);
                var position = hostileUnit.getPosition();
                hostileUnit.move(path, 0.016f);
                Assertions.assertNotEquals(position, hostileUnit.getPosition());
            }
        }
    }

    @Test
    void testMoveWithValidArgumentsButTimeIsZero() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var startPosition = Vector2.ZERO;
                var path = getNewPath(startPosition, width, height);
                var hostileUnit = getNewHostileUnit(startPosition, 1, 100);
                var position = hostileUnit.getPosition();
                hostileUnit.move(path, 0f);
                Assertions.assertEquals(position, hostileUnit.getPosition());
            }
        }
    }

    @Test
    void testMoveWithValidArgumentsButTimeIsHigh() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var startPosition = Vector2.ZERO;
                var path = getNewPath(startPosition, width, height);
                var hostileUnit = getNewHostileUnit(startPosition, 1, 100);
                var position = hostileUnit.getPosition();
                hostileUnit.move(path, 20f);
                Assertions.assertNotEquals(position, hostileUnit.getPosition());
            }
        }
    }

    @Test
    void testMoveWithValidArgumentsButHostileUnitIsNotOnPath() {
        var startPosition = Vector2.ZERO;
        var path = getNewPath(startPosition, 10, 10);
        var hostileUnit = getNewHostileUnit(new Vector2(-1f, -1f), 1, 100);
        Assertions.assertThrows(IllegalStateException.class, () -> hostileUnit.move(path, 0.016f));
    }

    @Test
    void testMoveWithInvalidArguments() {
        var hostileUnit = getRandomHostileUnit();
        Assertions.assertThrows(IllegalArgumentException.class, () -> hostileUnit.move(null, 0f));
    }

    @Test
    void testDealDamageWithValidArguments() {
        var hostileUnit = getNewHostileUnit(Vector2.ZERO, 1, 100);
        var health = hostileUnit.getHealth();
        hostileUnit.dealDamage(10);
        Assertions.assertEquals(health - 10, hostileUnit.getHealth());
    }

    @Test
    void testDealDamageWithValidArgumentsAndSoMuchDamageThatHostileUnitDies() {
        var hostileUnit = getNewHostileUnit(Vector2.ZERO, 1, 100);
        hostileUnit.dealDamage(Integer.MAX_VALUE);
        Assertions.assertEquals(0, hostileUnit.getHealth());
    }

    @Test
    void testDealDamageWithValidArgumentsButHostileUnitIsDead() {
        var hostileUnit = getNewHostileUnit(Vector2.ZERO, 1, 0);
        var health = hostileUnit.getHealth();
        hostileUnit.dealDamage(10);
        Assertions.assertEquals(health, hostileUnit.getHealth());
    }

    @Test
    void testDealDamageWithInvalidArguments() {
        var hostileUnit = getNewHostileUnit(Vector2.ZERO, 1, 100);
        Assertions.assertThrows(IllegalArgumentException.class, () -> hostileUnit.dealDamage(0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> hostileUnit.dealDamage(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> hostileUnit.dealDamage(-100));
        Assertions.assertThrows(IllegalArgumentException.class, () -> hostileUnit.dealDamage(-1000));
    }

    @Test
    void testKillWhenHostileUnitIsAlive() {
        var hostileUnit = getNewHostileUnit(Vector2.ZERO, 1, 100);
        hostileUnit.kill();
        Assertions.assertEquals(0, hostileUnit.getHealth());
    }

    @Test
    void testKillWhenHostileUnitIsDead() {
        var hostileUnit = getNewHostileUnit(Vector2.ZERO, 1, 0);
        hostileUnit.kill();
        Assertions.assertEquals(0, hostileUnit.getHealth());
    }

    @Test
    void testIsDeadWhenHostileUnitIsAlive() {
        var hostileUnit = getNewHostileUnit(Vector2.ZERO, 1, 100);
        Assertions.assertFalse(hostileUnit.isDead());
    }

    @Test
    void testIsDeadWhenHostileUnitIsDead() {
        var hostileUnit = getNewHostileUnit(Vector2.ZERO, 1, 0);
        Assertions.assertTrue(hostileUnit.isDead());
    }

    @Test
    void testGetPosition() {
        var hostileUnit = getRandomHostileUnit();
        Assertions.assertEquals(hostileUnit.getPosition(), hostileUnit.getPosition());
    }

    @Test
    void testEqualsAndHashCode() {
        var hostileUnit = getRandomHostileUnit();
        var hostileUnit1 = getRandomHostileUnit();
        Assertions.assertNotEquals(hostileUnit, hostileUnit1);
        Assertions.assertNotEquals(hostileUnit.hashCode(), hostileUnit1.hashCode());
        Assertions.assertEquals(hostileUnit, hostileUnit);
        Assertions.assertEquals(hostileUnit.hashCode(), hostileUnit.hashCode());

        var hostileUnit2 = getNewHostileUnit(Vector2.ZERO, 0, 0);
        var hostileUnit3 = getNewHostileUnit(Vector2.ZERO, 0, 0);
        Assertions.assertNotEquals(hostileUnit2, hostileUnit3);
        Assertions.assertNotEquals(hostileUnit2.hashCode(), hostileUnit3.hashCode());

        var tower = new GuardTower(io.swapastack.dunetd.vectors.Vector2.ZERO, null);
        Assertions.assertNotEquals(hostileUnit, tower);
        Assertions.assertNotEquals(hostileUnit.hashCode(), tower.hashCode());
    }

    @Test
    void testGetHealth() {
        var hostileUnit = getRandomHostileUnit();
        Assertions.assertEquals(hostileUnit.getHealth(), hostileUnit.getHealth());
    }

    @Test
    void testSlowDown() {
        for (int i = 1; i < 10; i++) {
            var hostileUnit = getRandomHostileUnit();
            var slowingEffectMultiplier = 0.1f * i;
            hostileUnit.slowDown(slowingEffectMultiplier, 100);
            Assertions.assertEquals(hostileUnit.getSpeed() * slowingEffectMultiplier, hostileUnit.getCurrentSpeed(), 0);
            Assertions.assertTrue(hostileUnit.getSpeed() >= hostileUnit.getCurrentSpeed());
        }
    }

    HostileUnit getNewHostileUnit(Vector2 position, float speed, int health) {
        return new HostileUnit(position, speed, health, null) {
            @Override
            public int getSpiceReward() {
                return 0;
            }
        };
    }

    HostileUnit getRandomHostileUnit() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();
        var speed = new Random().nextFloat();
        int health = new Random().nextInt();
        return getNewHostileUnit(new Vector2(x, y), speed, health);
    }

    Path getNewPath(Vector2 startPosition, int width, int height) {
        var grid = getEntityGrid(width, height);
        return Path.calculatePath(grid, startPosition, new Vector2(width - 1, height - 1));
    }

    Entity[][] getEntityGrid(int width, int height, Vector2... towerPositions) {
        var grid = new Entity[width][height];
        for (var towerPosition : towerPositions) {
            grid[(int) towerPosition.x()][(int) towerPosition.y()] = new GuardTower(towerPosition, null);
        }
        return grid;
    }
}
