package io.swapastack.dunetd.entities;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class EntityTest {

    @Test
    public void testValidConstructor1() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var entity = new Entity(x, y) {
        };

        assertEquals(x, entity.x);
        assertEquals(x, entity.getX());

        assertEquals(y, entity.y);
        assertEquals(y, entity.getY());
    }

    @Test
    public void testGetGridPosition3d() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var entity = new Entity(x, y) {
        };
        var gridPosition3d = entity.getGridPosition3d();

        assertEquals(x, gridPosition3d.x, 0f);
        assertEquals(0f, gridPosition3d.y, 0f);
        assertEquals(y, gridPosition3d.z, 0f);
    }

    @Test
    public void testGetGridPosition2d() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var entity = new Entity(x, y) {
        };
        var gridPosition3d = entity.getGridPosition2d();

        assertEquals(x, gridPosition3d.x, 0f);
        assertEquals(y, gridPosition3d.y, 0f);
    }
}