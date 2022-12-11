package io.swapastack.dunetd.math;

import com.badlogic.gdx.math.MathUtils;

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
        Assertions.assertThrows(IllegalArgumentException.class, () -> DuneTDMath.getAngle(Vector2.ZERO, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> DuneTDMath.getAngle(null, Vector2.ZERO));
    }

    @Test
    void testGetAngleWithZeroVectors() {
        Assertions.assertEquals(0f, DuneTDMath.getAngle(Vector2.ZERO, Vector2.ZERO), 0f);
    }

    @Test
    void testGetAngleWithSomeAngles() {
        Assertions.assertEquals(0f, DuneTDMath.getAngle(Vector2.ZERO, new Vector2(0f, 1f)), 0f);
        Assertions.assertEquals(45f, DuneTDMath.getAngle(Vector2.ZERO, new Vector2(1f, 1f)), 0f);
        Assertions.assertEquals(90f, DuneTDMath.getAngle(Vector2.ZERO, new Vector2(1f, 0f)), 0f);
        Assertions.assertEquals(135f, DuneTDMath.getAngle(Vector2.ZERO, new Vector2(1f, -1f)), 0f);
        Assertions.assertEquals(180f, DuneTDMath.getAngle(Vector2.ZERO, new Vector2(0f, -1f)), 0f);
        Assertions.assertEquals(225f, DuneTDMath.getAngle(Vector2.ZERO, new Vector2(-1f, -1f)), 0f);
        Assertions.assertEquals(270f, DuneTDMath.getAngle(Vector2.ZERO, new Vector2(-1f, 0f)), 0f);
        Assertions.assertEquals(315f, DuneTDMath.getAngle(Vector2.ZERO, new Vector2(-1f, 1f)), 0f);
        Assertions.assertEquals(315f, DuneTDMath.getAngle(Vector2.ZERO, new Vector2(-1f, 1f)), 0f);
    }

    @Test
    void testSameAngleButMultipliedVectors() {
        var from = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
        var to = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));

        var angle1 = DuneTDMath.getAngle(from, to);

        var scalar = new Random().nextFloat();

        var fromMultiplied = Vector2.multiply(from, scalar);
        var toMultiplied = Vector2.multiply(to, scalar);

        var angle2 = DuneTDMath.getAngle(fromMultiplied, toMultiplied);

        Assertions.assertEquals(angle1, angle2, 0.001f);
    }

    @Test
    void testSameAngleButAddedVectors() {
        var from = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));
        var to = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));

        var angle1 = DuneTDMath.getAngle(from, to);

        var addVector = new Vector2(new Random().nextFloat(), new Random().nextFloat());

        var fromAdded = Vector2.add(from, addVector);
        var toAdded = Vector2.add(to, addVector);

        var angle2 = DuneTDMath.getAngle(fromAdded, toAdded);

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
            grid[(int) towerPosition.x()][(int) towerPosition.y()] = new GuardTower(towerPosition, null);
        }
        return grid;
    }
}
