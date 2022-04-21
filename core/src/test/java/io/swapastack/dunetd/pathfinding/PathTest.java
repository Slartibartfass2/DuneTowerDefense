package io.swapastack.dunetd.pathfinding;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.GuardTower;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class PathTest {
    
    static {
        TestHelper.readConfigFile();
    }
    
    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");
    
    @Test
    public void testCalculatePathWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT ; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(width - 1, height - 1));
                assertNotNull(path);
                assertFalse(path.isBlocked());
                assertEquals(width + height - 3, path.getLength());
    
                grid = getEntityGrid(width, height, new Vector2(0f, 1f), new Vector2(1f, 0f));
                path = Path.calculatePath(grid, Vector2.Zero, new Vector2(width - 1, height - 1));
                assertNotNull(path);
                assertTrue(path.isBlocked());
                assertEquals(0, path.getLength());
            }
        }
    }
    
    @Test
    public void testCalculatePathWithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(null, null, null));
        assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(null, null, Vector2.Zero));
        assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(null, Vector2.Zero, null));
        assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(null, Vector2.Zero, Vector2.Zero));
        assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(new Entity[1][1], null, null));
        assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(new Entity[1][1], null, Vector2.Zero));
        assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(new Entity[1][1], Vector2.Zero, null));
        assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(new Entity[0][1], Vector2.Zero, Vector2.Zero));
        assertThrows(IllegalArgumentException.class, () -> Path.calculatePath(new Entity[1][0], Vector2.Zero, Vector2.Zero));
    }
    
    @Test
    public void testGetNextWaypointWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT ; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(width - 1, height - 1));
                var wayPoints = path.getWaypoints();
                for (int i = 0; i < wayPoints.length; i++) {
                    // Test exact next waypoint
                    var nextWaypoint = path.getNextWaypoint(wayPoints[i]);
                    if (i < wayPoints.length - 1)
                        assertEquals(wayPoints[i + 1], nextWaypoint);
                    else
                        assertEquals(wayPoints[i], nextWaypoint);
                    
                    // Test points in between
                    if (i < wayPoints.length - 1) {
                        var direction = wayPoints[i + 1].cpy().sub(wayPoints[i]);
                        float scalar = new Random().nextFloat();
                        var positionInBetween = wayPoints[i].cpy().add(direction.scl(scalar));
                        var nextWaypoint1 = path.getNextWaypoint(positionInBetween);
                        assertEquals(nextWaypoint, nextWaypoint1);
                    }
                }
            }
        }
    }
    
    @Test
    public void testGetNextWaypointWithValidArgumentsButPositionsOutsideGrid() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT ; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(width - 1, height - 1));
                var wayPoints = path.getWaypoints();
                for (var wayPoint : wayPoints) assertNull(path.getNextWaypoint(wayPoint.cpy().add(10000, 10000)));
            }
        }
    }
    
    @Test
    public void testGetNextWaypointWithInvalidArguments() {
        var grid = getEntityGrid(10, 10);
        var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(9, 9));
        assertThrows(IllegalArgumentException.class, () -> path.getNextWaypoint(null));
    }
    
    @Test
    public void testGetWaypointWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT ; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(width - 1, height - 1));
                var wayPoints = path.getWaypoints();
                for (int i = 0; i < wayPoints.length; i++) assertNotNull(path.getWaypoint(i));
            }
        }
    }
    
    @Test
    public void testGetWaypointWithInvalidArguments() {
        var grid = getEntityGrid(10, 10);
        var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(9, 9));
        var wayPoints = path.getWaypoints();
        assertThrows(IndexOutOfBoundsException.class, () -> path.getWaypoint(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> path.getWaypoint(wayPoints.length));
    }
    
    @Test
    public void testCopy() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT ; height++) {
                var grid = getEntityGrid(width, height);
                var path = Path.calculatePath(grid, Vector2.Zero, new Vector2(width - 1, height - 1));
                var path1 = path.copy();
                var wayPoints = path.getWaypoints();
                var wayPoints1 = path1.getWaypoints();
                assertEquals(wayPoints.length, wayPoints1.length);
                for (int i = 0; i < wayPoints.length; i++)
                    assertEquals(wayPoints[i], wayPoints1[i]);
            }
        }
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