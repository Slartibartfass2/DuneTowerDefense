package io.swapastack.dunetd.hostileunits;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.GuardTower;
import io.swapastack.dunetd.pathfinding.Path;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HostileUnitTest {

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testMoveWithValidArguments() {
        var startPosition = Vector2.ZERO;
        var grid = getEntityGrid(1, 3);
        var path = Path.calculatePath(grid, startPosition, new Vector2(0, 2));
        var hostileUnit = getNewHostileUnit(startPosition, 1, 100);
        hostileUnit.move(path, 1f);
        Assertions.assertEquals(new Vector2(0, 1), hostileUnit.getPosition());
    }

    @Test
    void testMoveAndReachEndOfPath() {
        var startPosition = Vector2.ZERO;
        var grid = getEntityGrid(1, 2);
        var path = Path.calculatePath(grid, startPosition, new Vector2(0, 1));
        var hostileUnit = getNewHostileUnit(startPosition, 1, 100);
        hostileUnit.move(path, 2f);
        Assertions.assertEquals(new Vector2(0, 1), hostileUnit.getPosition());
    }

    @Test
    void testMoveWhileSlowedDown() {
        var startPosition = Vector2.ZERO;
        var grid = getEntityGrid(1, 3);
        var path = Path.calculatePath(grid, startPosition, new Vector2(0, 2));
        var hostileUnit = getNewHostileUnit(startPosition, 1, 100);
        hostileUnit.slowDown(0.1f, 100);
        hostileUnit.move(path, 1f);
        Assertions.assertEquals(new Vector2(0, 0.1f), hostileUnit.getPosition());
    }

    @Test
    void testMoveWithValidArgumentsButTimeIsZero() {
        var startPosition = Vector2.ZERO;
        var grid = getEntityGrid(10, 10);
        var path = Path.calculatePath(grid, startPosition, new Vector2(0, 9));
        var hostileUnit = getNewHostileUnit(startPosition, 1, 100);
        var position = hostileUnit.getPosition();
        hostileUnit.move(path, 0f);
        Assertions.assertEquals(position, hostileUnit.getPosition());
    }

    @Test
    void testMoveWithValidArgumentsButHostileUnitIsNotOnPath() {
        var startPosition = Vector2.ZERO;
        var grid = getEntityGrid(10, 10);
        var path = Path.calculatePath(grid, startPosition, new Vector2(0, 9));
        var hostileUnit = getNewHostileUnit(new Vector2(-1f, -1f), 1, 100);
        Assertions.assertThrows(IllegalStateException.class, () -> hostileUnit.move(path, 0.016f));
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
        hostileUnit.dealDamage(10);
        Assertions.assertEquals(0, hostileUnit.getHealth());
    }

    @Test
    void testDealDamageWithNegativeDamage() {
        var hostileUnit = getNewHostileUnit(Vector2.ZERO, 1, 100);
        Assertions.assertThrows(IllegalArgumentException.class, () -> hostileUnit.dealDamage(-1));
    }

    @Test
    void testSlowDown() {
        var hostileUnit = getNewHostileUnit(Vector2.ZERO, 10, 10);
        var slowingEffectMultiplier = 0.1f;
        hostileUnit.slowDown(slowingEffectMultiplier, 100);
        Assertions.assertEquals(hostileUnit.getSpeed() * slowingEffectMultiplier, hostileUnit.getCurrentSpeed(), 0);
        Assertions.assertTrue(hostileUnit.getSpeed() >= hostileUnit.getCurrentSpeed());
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

    HostileUnit getNewHostileUnit(Vector2 position, float speed, int health) {
        return new HostileUnit(position, speed, health, null) {
            @Override
            public int getSpiceReward() {
                return 0;
            }
        };
    }

    Entity[][] getEntityGrid(int width, int height, Vector2... towerPositions) {
        var grid = new Entity[width][height];
        for (var towerPosition : towerPositions) {
            grid[(int) towerPosition.x()][(int) towerPosition.y()] = new GuardTower(towerPosition, null);
        }
        return grid;
    }
}
