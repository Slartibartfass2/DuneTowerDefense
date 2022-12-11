package io.swapastack.dunetd.vectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Vector2Test {

    @Test
    void testGetLength() {
        var vector = new Vector2(3, 4);

        float actual = vector.getLength();
        Assertions.assertEquals(5, actual);
    }

    @Test
    void testGetDistance() {
        var vector1 = new Vector2(1, 0);
        var vector2 = new Vector2(4, 4);

        float actual = vector1.getDistance(vector2);
        Assertions.assertEquals(5, actual);
    }

    @Test
    void testGetDistanceSquared() {
        var vector1 = new Vector2(1, 0);
        var vector2 = new Vector2(3, 4);

        float actual = vector1.getDistanceSquared(vector2);
        Assertions.assertEquals(20, actual);
    }

    @Test
    void testIsZero() {
        var vector1 = new Vector2(0, 0);
        var vector2 = new Vector2(1, 2);

        Assertions.assertTrue(vector1.isZero());
        Assertions.assertFalse(vector2.isZero());
    }

    @Test
    void testNormalize() {
        var vector = new Vector2(3, 4);

        var actual = vector.normalize();
        Assertions.assertEquals(new Vector2(0.6f, 0.8f), actual);
        Assertions.assertEquals(1, actual.getLength());
    }

    @Test
    void testMultiply() {
        var vector = new Vector2(3, 4);
        float scalar = 3;

        var actual = Vector2.multiply(vector, scalar);
        Assertions.assertEquals(new Vector2(9, 12), actual);
    }

    @Test
    void testAddWithVector() {
        var vector1 = new Vector2(1, 0);
        var vector2 = new Vector2(3, 4);

        var actual = Vector2.add(vector1, vector2);
        Assertions.assertEquals(new Vector2(4, 4), actual);
    }

    @Test
    void testAddWithCoordinates() {
        var vector = new Vector2(1, 3);
        float x = 1.4f;
        float y = 2.7f;

        var actual = Vector2.add(vector, x, y);
        Assertions.assertEquals(new Vector2(2.4f, 5.7f), actual);
    }

    @Test
    void testSubtract() {
        var vector1 = new Vector2(1, 0);
        var vector2 = new Vector2(3, 4);

        var actual = Vector2.subtract(vector1, vector2);
        Assertions.assertEquals(new Vector2(-2, -4), actual);
    }
}
