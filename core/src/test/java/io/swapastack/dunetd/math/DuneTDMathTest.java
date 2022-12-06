package io.swapastack.dunetd.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.GuardTower;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.swapastack.dunetd.math.DuneTDMath.*;
import static org.junit.jupiter.api.Assertions.*;

class DuneTDMathTest {

    static {
        TestHelper.readConfigFile();
    }

    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");

    @Test
    void testGetAngleWithNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> getAngle(null, null));
        assertThrows(IllegalArgumentException.class, () -> getAngle(Vector2.Zero, null));
        assertThrows(IllegalArgumentException.class, () -> getAngle(null, Vector2.Zero));
    }

    @Test
    void testGetAngleWithZeroVectors() {
        assertEquals(0f, getAngle(Vector2.Zero, Vector2.Zero), 0f);
    }

    @Test
    void testGetAngleWithSomeAngles() {
        assertEquals(0f, getAngle(Vector2.Zero, new Vector2(0f, 1f)), 0f);
        assertEquals(45f, getAngle(Vector2.Zero, new Vector2(1f, 1f)), 0f);
        assertEquals(90f, getAngle(Vector2.Zero, new Vector2(1f, 0f)), 0f);
        assertEquals(135f, getAngle(Vector2.Zero, new Vector2(1f, -1f)), 0f);
        assertEquals(180f, getAngle(Vector2.Zero, new Vector2(0f, -1f)), 0f);
        assertEquals(225f, getAngle(Vector2.Zero, new Vector2(-1f, -1f)), 0f);
        assertEquals(270f, getAngle(Vector2.Zero, new Vector2(-1f, 0f)), 0f);
        assertEquals(315f, getAngle(Vector2.Zero, new Vector2(-1f, 1f)), 0f);
        assertEquals(315f, getAngle(Vector2.Zero, new Vector2(-1f, 1f)), 0f);
    }

    @Test
    void testSameAngleButMultipliedVectors() {
        var from = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
        var to = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));

        var angle1 = getAngle(from.cpy(), to.cpy());

        var scalar = new Random().nextFloat();

        from.scl(scalar);
        to.scl(scalar);

        var angle2 = getAngle(from, to);

        assertEquals(angle1, angle2, 0.001f);
    }

    @Test
    void testSameAngleButAddedVectors() {
        var from = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
        var to = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));

        var angle1 = getAngle(from.cpy(), to.cpy());

        var addVector = new Vector2(new Random().nextFloat(), new Random().nextFloat());

        from.add(addVector.cpy());
        to.add(addVector.cpy());

        var angle2 = getAngle(from, to);

        assertEquals(angle1, angle2, 0.001f);
    }

    @Test
    void testIsPositionInsideGridWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var grid = getEntityGrid(width, height);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        assertTrue(isPositionInsideGrid(grid, x, y));
                    }
                }
            }
        }
    }

    @Test
    void testIsPositionInsideGridWithInvalidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var grid = getEntityGrid(width, height);
                assertFalse(isPositionInsideGrid(grid, -1, -1));
                assertFalse(isPositionInsideGrid(grid, 0, -1));
                assertFalse(isPositionInsideGrid(grid, -1, 0));
                assertFalse(isPositionInsideGrid(grid, width, -1));
                assertFalse(isPositionInsideGrid(grid, width, 0));
                assertFalse(isPositionInsideGrid(grid, -1, height));
                assertFalse(isPositionInsideGrid(grid, 0, height));
            }
        }
    }

    @Test
    void testIsPositionAvailableWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var grid = getEntityGrid(width, height);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        assertTrue(isPositionAvailable(grid, x, y));
                    }
                }
            }
        }
    }

    @Test
    void testIsPositionAvailableWithInvalidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var towers = new Vector2[width * height];
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        towers[x + y * width] = new Vector2(x, y);
                    }
                }
                var grid = getEntityGrid(width, height, towers);
                assertFalse(isPositionAvailable(grid, -1, -1));
                assertFalse(isPositionAvailable(grid, 0, -1));
                assertFalse(isPositionAvailable(grid, -1, 0));
                assertFalse(isPositionAvailable(grid, width, -1));
                assertFalse(isPositionAvailable(grid, width, 0));
                assertFalse(isPositionAvailable(grid, -1, height));
                assertFalse(isPositionAvailable(grid, 0, height));
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        assertFalse(isPositionAvailable(grid, x, y));
                    }
                }
            }
        }
    }

    Entity[][] getEntityGrid(int width, int height, Vector2... towerPositions) {
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
