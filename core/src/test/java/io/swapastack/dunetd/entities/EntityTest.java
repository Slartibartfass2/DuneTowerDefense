package io.swapastack.dunetd.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

class EntityTest {

    @Test
    void testValidConstructor1() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var entity = new Entity(x, y) {
        };

        Assertions.assertEquals(x, entity.x);
        Assertions.assertEquals(x, entity.getX());

        Assertions.assertEquals(y, entity.y);
        Assertions.assertEquals(y, entity.getY());
    }

    @Test
    void testGetGridPosition3d() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var entity = new Entity(x, y) {
        };
        var gridPosition3d = entity.getGridPosition3d();

        Assertions.assertEquals(x, gridPosition3d.x, 0f);
        Assertions.assertEquals(0f, gridPosition3d.y, 0f);
        Assertions.assertEquals(y, gridPosition3d.z, 0f);
    }

    @Test
    void testGetGridPosition2d() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var entity = new Entity(x, y) {
        };
        var gridPosition3d = entity.getGridPosition2d();

        Assertions.assertEquals(x, gridPosition3d.x, 0f);
        Assertions.assertEquals(y, gridPosition3d.y, 0f);
    }
}
