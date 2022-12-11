package io.swapastack.dunetd.pathfinding;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.GuardTower;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PathTest {

    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testCalculatePathWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.ZERO, new Vector2(width - 1, height - 1));
                Assertions.assertNotNull(path);
                Assertions.assertFalse(path.isBlocked());
                Assertions.assertEquals(width + height - 3, path.getLength());

                grid = getEntityGrid(width, height, new Vector2(0f, 1f), new Vector2(1f, 0f));
                path = Path.calculatePath(grid, Vector2.ZERO, new Vector2(width - 1, height - 1));
                Assertions.assertNotNull(path);
                Assertions.assertTrue(path.isBlocked());
                Assertions.assertEquals(0, path.getLength());
            }
        }
    }

    @Test
    void testCalculatePathWithInvalidArguments() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Path.calculatePath(null, null, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Path.calculatePath(null, null, Vector2.ZERO));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Path.calculatePath(null, Vector2.ZERO, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Path.calculatePath(null, Vector2.ZERO, Vector2.ZERO));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Path.calculatePath(new Entity[1][1], null, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Path.calculatePath(new Entity[1][1], null, Vector2.ZERO));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Path.calculatePath(new Entity[1][1], Vector2.ZERO, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Path.calculatePath(new Entity[0][1], Vector2.ZERO, Vector2.ZERO));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Path.calculatePath(new Entity[1][0], Vector2.ZERO, Vector2.ZERO));
    }

    @Test
    void testGetNextWaypointWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.ZERO, new Vector2(width - 1, height - 1));
                var wayPoints = path.getWaypoints();
                for (int i = 0; i < wayPoints.length; i++) {
                    // Test exact next waypoint
                    var nextWaypoint = path.getNextWaypoint(wayPoints[i]);
                    if (i < wayPoints.length - 1) {
                        Assertions.assertEquals(wayPoints[i + 1], nextWaypoint);
                    } else {
                        Assertions.assertEquals(wayPoints[i], nextWaypoint);
                    }

                    // Test points in between
                    if (i < wayPoints.length - 1) {
                        var direction = Vector2.subtract(wayPoints[i + 1], wayPoints[i]);
                        var scalar = new Random().nextFloat();
                        var multipliedVector = Vector2.multiply(direction, scalar);
                        var positionInBetween = Vector2.add(wayPoints[i], multipliedVector);
                        var nextWaypoint1 = path.getNextWaypoint(positionInBetween);
                        Assertions.assertEquals(nextWaypoint, nextWaypoint1);
                    }
                }
            }
        }
    }

    @Test
    void testGetNextWaypointWithValidArgumentsButPositionsOutsideGrid() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.ZERO, new Vector2(width - 1, height - 1));
                var wayPoints = path.getWaypoints();
                for (var wayPoint : wayPoints) {
                    Assertions.assertNull(path.getNextWaypoint(Vector2.add(wayPoint, 10000, 10000)));
                }
            }
        }
    }

    @Test
    void testGetNextWaypointWithInvalidArguments() {
        var grid = getEntityGrid(10, 10);
        var path = Path.calculatePath(grid, Vector2.ZERO, new Vector2(9, 9));
        Assertions.assertThrows(IllegalArgumentException.class, () -> path.getNextWaypoint(null));
    }

    @Test
    void testGetWaypointWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.ZERO, new Vector2(width - 1, height - 1));
                var wayPoints = path.getWaypoints();
                for (int i = 0; i < wayPoints.length; i++) {
                    Assertions.assertNotNull(path.getWaypoint(i));
                }
            }
        }
    }

    @Test
    void testGetWaypointWithInvalidArguments() {
        var grid = getEntityGrid(10, 10);
        var path = Path.calculatePath(grid, Vector2.ZERO, new Vector2(9, 9));
        var wayPoints = path.getWaypoints();
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> path.getWaypoint(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> path.getWaypoint(wayPoints.length));
    }

    Entity[][] getEntityGrid(int width, int height, Vector2... towerPositions) {
        var grid = new Entity[width][height];
        for (var towerPosition : towerPositions) {
            int x = (int) towerPosition.x();
            int y = (int) towerPosition.y();

            grid[x][y] = new GuardTower(towerPosition, null);
        }
        return grid;
    }
}
