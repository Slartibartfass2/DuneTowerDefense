package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.GuardTower;
import io.swapastack.dunetd.game.HostileUnitController;
import io.swapastack.dunetd.pathfinding.Path;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class HostileUnitTest {

    static {
        TestHelper.readConfigFile();
    }

    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");

    @Test
    public void testConstructor1WithValidArguments() {
        var hostileUnit = getNewHostileUnit(Vector2.Zero, 10, 100);
        assertNotNull(hostileUnit);
    }

    @Test
    public void testConstructor1WithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> getNewHostileUnit(null, 0f, 0));
    }

    @Test
    public void testConstructor2WithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> getNewHostileUnit(null, 0f, 0, null));
        assertThrows(IllegalArgumentException.class, () -> getNewHostileUnit(Vector2.Zero, 0f, 0, null));
    }

    @Test
    public void testMoveWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var startPosition = Vector2.Zero;
                var path = getNewPath(startPosition, width, height);
                var hostileUnit = getNewHostileUnit(startPosition, 1, 100);
                var position = hostileUnit.position.cpy();
                hostileUnit.move(path, 0.016f);
                assertNotEquals(position, hostileUnit.position);
            }
        }
    }

    @Test
    public void testMoveWithValidArgumentsButTimeIsZero() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var startPosition = Vector2.Zero;
                var path = getNewPath(startPosition, width, height);
                var hostileUnit = getNewHostileUnit(startPosition, 1, 100);
                var position = hostileUnit.position.cpy();
                hostileUnit.move(path, 0f);
                assertEquals(position, hostileUnit.position);
            }
        }
    }

    @Test
    public void testMoveWithValidArgumentsButTimeIsHigh() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var startPosition = Vector2.Zero;
                var path = getNewPath(startPosition, width, height);
                var hostileUnit = getNewHostileUnit(startPosition, 1, 100);
                var position = hostileUnit.position.cpy();
                hostileUnit.move(path, 20f);
                assertNotEquals(position, hostileUnit.position);
            }
        }
    }

    @Test
    public void testMoveWithValidArgumentsButHostileUnitIsNotOnPath() {
        var startPosition = Vector2.Zero;
        var path = getNewPath(startPosition, 10, 10);
        var hostileUnit = getNewHostileUnit(new Vector2(-1f, -1f), 1, 100);
        assertThrows(IllegalStateException.class, () -> hostileUnit.move(path, 0.016f));
    }

    @Test
    public void testMoveWithValidArgumentsWithSlowingEffect() {
        var startPosition = Vector2.Zero;
        var path = getNewPath(startPosition, 10, 10);
        var hostileUnit = getNewHostileUnit(startPosition, 1, 100);
        hostileUnit.slowingEffectDurationInMs = 1000;
        hostileUnit.move(path, 0.016f);
    }

    @Test
    public void testMoveWithInvalidArguments() {
        var hostileUnit = getRandomHostileUnit();
        assertThrows(IllegalArgumentException.class, () -> hostileUnit.move(null, 0f));
    }

    @Test
    public void testDealDamageWithValidArguments() {
        var hostileUnit = getNewHostileUnit(Vector2.Zero, 1, 100);
        var health = hostileUnit.health;
        hostileUnit.dealDamage(10);
        assertEquals(health - 10, hostileUnit.health);
    }

    @Test
    public void testDealDamageWithValidArgumentsAndSoMuchDamageThatHostileUnitDies() {
        var hostileUnit = getNewHostileUnit(Vector2.Zero, 1, 100);
        hostileUnit.dealDamage(Integer.MAX_VALUE);
        assertEquals(0, hostileUnit.health);
    }

    @Test
    public void testDealDamageWithValidArgumentsButHostileUnitIsDead() {
        var hostileUnit = getNewHostileUnit(Vector2.Zero, 1, 0);
        var health = hostileUnit.health;
        hostileUnit.dealDamage(10);
        assertEquals(health, hostileUnit.health);
    }

    @Test
    public void testDealDamageWithInvalidArguments() {
        var hostileUnit = getNewHostileUnit(Vector2.Zero, 1, 100);
        assertThrows(IllegalArgumentException.class, () -> hostileUnit.dealDamage(0));
        assertThrows(IllegalArgumentException.class, () -> hostileUnit.dealDamage(-1));
        assertThrows(IllegalArgumentException.class, () -> hostileUnit.dealDamage(-100));
        assertThrows(IllegalArgumentException.class, () -> hostileUnit.dealDamage(-1000));
    }

    @Test
    public void testKillWhenHostileUnitIsAlive() {
        var hostileUnit = getNewHostileUnit(Vector2.Zero, 1, 100);
        hostileUnit.kill();
        assertEquals(0, hostileUnit.health);
    }

    @Test
    public void testKillWhenHostileUnitIsDead() {
        var hostileUnit = getNewHostileUnit(Vector2.Zero, 1, 0);
        hostileUnit.kill();
        assertEquals(0, hostileUnit.health);
    }

    @Test
    public void testIsDeadWhenHostileUnitIsAlive() {
        var hostileUnit = getNewHostileUnit(Vector2.Zero, 1, 100);
        assertFalse(hostileUnit.isDead());
    }

    @Test
    public void testIsDeadWhenHostileUnitIsDead() {
        var hostileUnit = getNewHostileUnit(Vector2.Zero, 1, 0);
        assertTrue(hostileUnit.isDead());
    }

    @Test
    public void testGetPosition() {
        var hostileUnit = getRandomHostileUnit();
        assertEquals(hostileUnit.position, hostileUnit.getPosition());
    }

    @Test
    public void testEqualsAndHashCode() {
        var hostileUnit = getRandomHostileUnit();
        var hostileUnit1 = getRandomHostileUnit();
        assertNotEquals(hostileUnit, hostileUnit1);
        assertNotEquals(hostileUnit.hashCode(), hostileUnit1.hashCode());
        assertEquals(hostileUnit, hostileUnit);
        assertEquals(hostileUnit.hashCode(), hostileUnit.hashCode());

        var hostileUnit2 = getNewHostileUnit(Vector2.Zero, 0, 0);
        var hostileUnit3 = getNewHostileUnit(Vector2.Zero, 0, 0);
        assertNotEquals(hostileUnit2, hostileUnit3);
        assertNotEquals(hostileUnit2.hashCode(), hostileUnit3.hashCode());

        var tower = new GuardTower(0, 0);
        assertNotEquals(hostileUnit, tower);
        assertNotEquals(hostileUnit.hashCode(), tower.hashCode());
    }

    @Test
    public void testGetHealth() {
        var hostileUnit = getRandomHostileUnit();
        assertEquals(hostileUnit.health, hostileUnit.getHealth());
    }

    public HostileUnit getNewHostileUnit(Vector2 position, float speed, int health) {
        return new HostileUnit(position, speed, health) {
            @Override
            public void slowDown(float slowingEffectMultiplier, int slowingEffectDurationInMs) {

            }

            @Override
            public int getSpiceReward() {
                return 0;
            }
        };
    }

    public HostileUnit getNewHostileUnit(Vector2 position, float speed, int health, HostileUnitController hostileUnitController) {
        return new HostileUnit(position, speed, health, hostileUnitController) {
            @Override
            public void slowDown(float slowingEffectMultiplier, int slowingEffectDurationInMs) {

            }

            @Override
            public int getSpiceReward() {
                return 0;
            }
        };
    }

    public HostileUnit getRandomHostileUnit() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();
        float speed = new Random().nextFloat();
        int health = new Random().nextInt();
        return getNewHostileUnit(new Vector2(x, y), speed, health);
    }

    public Path getNewPath(Vector2 startPosition, int width, int height) {
        var grid = getEntityGrid(width, height);
        return Path.calculatePath(grid, startPosition, new Vector2(width - 1, height - 1));
    }

    public Entity[][] getEntityGrid(int width, int height, Vector2...towerPositions) {
        var grid = new Entity[width][height];
        for (var towerPosition : towerPositions) {
            int x = (int) towerPosition.x;
            int y = (int) towerPosition.y;

            try {
                grid[x][y] = new GuardTower(x, y);
            } catch (Exception ignored) {
            }
        }
        return grid;
    }
}