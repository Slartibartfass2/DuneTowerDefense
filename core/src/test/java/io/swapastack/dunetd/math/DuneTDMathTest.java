package io.swapastack.dunetd.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.GuardTower;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DuneTDMathTest {

    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testGetAngleWithNullArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> DuneTDMath.getAngle(null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> DuneTDMath.getAngle(Vector2.Zero, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> DuneTDMath.getAngle(null, Vector2.Zero));
    }

    @Test
    void testGetAngleWithZeroVectors() {
        Assertions.assertEquals(0f, DuneTDMath.getAngle(Vector2.Zero, Vector2.Zero), 0f);
    }

    @Test
    void testGetAngleWithSomeAngles() {
        Assertions.assertEquals(0f, DuneTDMath.getAngle(Vector2.Zero, new Vector2(0f, 1f)), 0f);
        Assertions.assertEquals(45f, DuneTDMath.getAngle(Vector2.Zero, new Vector2(1f, 1f)), 0f);
        Assertions.assertEquals(90f, DuneTDMath.getAngle(Vector2.Zero, new Vector2(1f, 0f)), 0f);
        Assertions.assertEquals(135f, DuneTDMath.getAngle(Vector2.Zero, new Vector2(1f, -1f)), 0f);
        Assertions.assertEquals(180f, DuneTDMath.getAngle(Vector2.Zero, new Vector2(0f, -1f)), 0f);
        Assertions.assertEquals(225f, DuneTDMath.getAngle(Vector2.Zero, new Vector2(-1f, -1f)), 0f);
        Assertions.assertEquals(270f, DuneTDMath.getAngle(Vector2.Zero, new Vector2(-1f, 0f)), 0f);
        Assertions.assertEquals(315f, DuneTDMath.getAngle(Vector2.Zero, new Vector2(-1f, 1f)), 0f);
        Assertions.assertEquals(315f, DuneTDMath.getAngle(Vector2.Zero, new Vector2(-1f, 1f)), 0f);
    }

    @Test
    void testSameAngleButMultipliedVectors() {
        var from = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
        var to = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));

        var angle1 = DuneTDMath.getAngle(from.cpy(), to.cpy());

        var scalar = new Random().nextFloat();

        from.scl(scalar);
        to.scl(scalar);

        var angle2 = DuneTDMath.getAngle(from, to);

        Assertions.assertEquals(angle1, angle2, 0.001f);
    }

    @Test
    void testSameAngleButAddedVectors() {
        var from = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
        var to = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));

        var angle1 = DuneTDMath.getAngle(from.cpy(), to.cpy());

        var addVector = new Vector2(new Random().nextFloat(), new Random().nextFloat());

        from.add(addVector.cpy());
        to.add(addVector.cpy());

        var angle2 = DuneTDMath.getAngle(from, to);

        Assertions.assertEquals(angle1, angle2, 0.001f);
    }

    @Test
    void testIsPositionInsideGridWithValidArguments() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var grid = getEntityGrid(width, height);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        Assertions.assertTrue(DuneTDMath.isPositionInsideGrid(grid, x, y));
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
                Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, -1, -1));
                Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, 0, -1));
                Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, -1, 0));
                Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, width, -1));
                Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, width, 0));
                Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, -1, height));
                Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, 0, height));
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
                        Assertions.assertTrue(DuneTDMath.isPositionAvailable(grid, x, y));
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
                Assertions.assertFalse(DuneTDMath.isPositionAvailable(grid, -1, -1));
                Assertions.assertFalse(DuneTDMath.isPositionAvailable(grid, 0, -1));
                Assertions.assertFalse(DuneTDMath.isPositionAvailable(grid, -1, 0));
                Assertions.assertFalse(DuneTDMath.isPositionAvailable(grid, width, -1));
                Assertions.assertFalse(DuneTDMath.isPositionAvailable(grid, width, 0));
                Assertions.assertFalse(DuneTDMath.isPositionAvailable(grid, -1, height));
                Assertions.assertFalse(DuneTDMath.isPositionAvailable(grid, 0, height));
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        Assertions.assertFalse(DuneTDMath.isPositionAvailable(grid, x, y));
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

            grid[x][y] = new GuardTower(x, y);
        }
        return grid;
    }
}
