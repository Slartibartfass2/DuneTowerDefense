package io.swapastack.dunetd.vectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Vector3Test {

    @Test
    void testGetLength() {
        var vector = new Vector3(3, 4, 7);

        float actual = vector.getLength();
        Assertions.assertEquals(Math.sqrt(74), actual, 1E-6);
    }

    @Test
    void testNormalize() {
        var vector = new Vector3(3, 4, 2);

        float length = (float) Math.sqrt(29);

        var actual = vector.normalize();
        Assertions.assertEquals(new Vector3(3 / length, 4 / length, 2 / length), actual);
        Assertions.assertEquals(1, actual.getLength());
    }

    @Test
    void testAddWithVector() {
        var vector1 = new Vector3(1, 0, 4);
        var vector2 = new Vector3(3, 4, 9);

        var actual = Vector3.add(vector1, vector2);
        Assertions.assertEquals(new Vector3(4, 4, 13), actual);
    }

    @Test
    void testSubtract() {
        var vector1 = new Vector3(1, 0, 4);
        var vector2 = new Vector3(3, 4, 9);

        var actual = Vector3.subtract(vector1, vector2);
        Assertions.assertEquals(new Vector3(-2, -4, -5), actual);
    }

    @Test
    void testMultiply() {
        var vector = new Vector3(3, 4, 6);
        float scalar = 3;

        var actual = Vector3.multiply(vector, scalar);
        Assertions.assertEquals(new Vector3(9, 12, 18), actual);
    }
}
