package io.swapastack.dunetd.math;

import com.badlogic.gdx.math.MathUtils;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.GuardTower;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DuneTDMathTest {

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
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
        var grid = getEntityGrid(5, 5);
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                Assertions.assertTrue(DuneTDMath.isPositionInsideGrid(grid, x, y));
            }
        }
    }

    @Test
    void testIsPositionInsideGridWithInvalidArguments() {
        var grid = getEntityGrid(10, 10);
        Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, -1, -1));
        Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, 0, -1));
        Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, -1, 0));
        Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, 10, -1));
        Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, 10, 0));
        Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, -1, 10));
        Assertions.assertFalse(DuneTDMath.isPositionInsideGrid(grid, 0, 10));
    }

    @Test
    void testIsPositionAvailableWithPositionInsideTheGridAndNullEntities() {
        var grid = getEntityGrid(10, 10);
        Assertions.assertTrue(DuneTDMath.isPositionAvailable(grid, 0, 0));
    }

    @Test
    void testIsPositionAvailableWithPositionOutsideTheGrid() {
        var grid = getEntityGrid(10, 10);
        Assertions.assertFalse(DuneTDMath.isPositionAvailable(grid, -1, -1));
    }

    @Test
    void testIsPositionAvailableWithPositionInsideTheGridAndNonNullEntities() {
        var grid = getEntityGrid(10, 10);
        grid[0][0] = new Entity(Vector2.ZERO) { };
        Assertions.assertFalse(DuneTDMath.isPositionAvailable(grid, 0, 0));
    }

    Entity[][] getEntityGrid(int width, int height, Vector2... towerPositions) {
        var grid = new Entity[width][height];
        for (var towerPosition : towerPositions) {
            grid[(int) towerPosition.x()][(int) towerPosition.y()] = new GuardTower(towerPosition, null);
        }
        return grid;
    }
}
