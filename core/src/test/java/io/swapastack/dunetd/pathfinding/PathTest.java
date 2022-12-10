package io.swapastack.dunetd.pathfinding;

import com.badlogic.gdx.math.Vector2;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.GuardTower;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PathTest {

    static {
        TestHelper.readConfigFile();
    }

    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");

    @Test
    void testCalculatePathWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(width - 1, height - 1));
                Assertions.assertNotNull(path);
                Assertions.assertFalse(path.isBlocked());
                Assertions.assertEquals(width + height - 3, path.getLength());

                grid = getEntityGrid(width, height, new Vector2(0f, 1f), new Vector2(1f, 0f));
                path = Path.calculatePath(grid, Vector2.Zero, new Vector2(width - 1, height - 1));
                Assertions.assertNotNull(path);
                Assertions.assertTrue(path.isBlocked());
                Assertions.assertEquals(0, path.getLength());
            }
        }
    }

    @Test
    void testCalculatePathWithInvalidArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(null, null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(null, null, Vector2.Zero));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(null, Vector2.Zero, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Path.calculatePath(null, Vector2.Zero, Vector2.Zero));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(new Entity[1][1], null, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Path.calculatePath(new Entity[1][1], null, Vector2.Zero));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Path.calculatePath(new Entity[1][1], Vector2.Zero, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(new Entity[0][1], Vector2.Zero,
                Vector2.Zero));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(new Entity[1][0], Vector2.Zero,
                Vector2.Zero));
    }

    @Test
    void testGetNextWaypointWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(width - 1, height - 1));
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
                        var direction = wayPoints[i + 1].cpy().sub(wayPoints[i]);
                        var scalar = new Random().nextFloat();
                        var positionInBetween = wayPoints[i].cpy().add(direction.scl(scalar));
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
                var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(width - 1, height - 1));
                var wayPoints = path.getWaypoints();
                for (var wayPoint : wayPoints) {
                    Assertions.assertNull(path.getNextWaypoint(wayPoint.cpy().add(10000, 10000)));
                }
            }
        }
    }

    @Test
    void testGetNextWaypointWithInvalidArguments() {
        var grid = getEntityGrid(10, 10);
        var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(9, 9));
        Assertions.assertThrows(IllegalArgumentException.class, () -> path.getNextWaypoint(null));
    }

    @Test
    void testGetWaypointWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(width - 1, height - 1));
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
        var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(9, 9));
        var wayPoints = path.getWaypoints();
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> path.getWaypoint(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> path.getWaypoint(wayPoints.length));
    }

    @Test
    void testCopy() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(width - 1, height - 1));
                var path1 = path.copy();
                var wayPoints = path.getWaypoints();
                var wayPoints1 = path1.getWaypoints();
                Assertions.assertEquals(wayPoints.length, wayPoints1.length);
                for (int i = 0; i < wayPoints.length; i++) {
                    Assertions.assertEquals(wayPoints[i], wayPoints1[i]);
                }
            }
        }
    }

    Entity[][] getEntityGrid(int width, int height, Vector2... towerPositions) {
        var grid = new Entity[width][height];
        for (var towerPosition : towerPositions) {
            int x = (int) towerPosition.x;
            int y = (int) towerPosition.y;

            grid[x][y] = new GuardTower(x, y);
        }
        return grid;
    }
}
