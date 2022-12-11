package io.swapastack.dunetd.game;

import io.swapastack.dunetd.vectors.Vector2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CardinalDirectionTest {

    @Test
    void testFromDirectionWithValidArguments() {
        var northVector = new Vector2(0f, 1f);
        Assertions.assertEquals(CardinalDirection.NORTH, CardinalDirection.fromDirection(northVector));

        var eastVector = new Vector2(1f, 0f);
        Assertions.assertEquals(CardinalDirection.EAST, CardinalDirection.fromDirection(eastVector));

        var southVector = new Vector2(0f, -1f);
        Assertions.assertEquals(CardinalDirection.SOUTH, CardinalDirection.fromDirection(southVector));

        var westVector = new Vector2(-1f, 0f);
        Assertions.assertEquals(CardinalDirection.WEST, CardinalDirection.fromDirection(westVector));
    }

    @Test
    void testFromDirectionWithInvalidArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> CardinalDirection.fromDirection(null));
    }

    @Test
    void testGetDirection() {
        var northVector = new Vector2(0f, 1f);
        Assertions.assertEquals(CardinalDirection.NORTH.getDirection(), northVector);

        var eastVector = new Vector2(1f, 0f);
        Assertions.assertEquals(CardinalDirection.EAST.getDirection(), eastVector);

        var southVector = new Vector2(0f, -1f);
        Assertions.assertEquals(CardinalDirection.SOUTH.getDirection(), southVector);

        var westVector = new Vector2(-1f, 0f);
        Assertions.assertEquals(CardinalDirection.WEST.getDirection(), westVector);
    }

    @Test
    void testGetDegrees() {
        Assertions.assertEquals(0f, CardinalDirection.NORTH.getDegrees(), 0f);
        Assertions.assertEquals(90f, CardinalDirection.EAST.getDegrees(), 0f);
        Assertions.assertEquals(180f, CardinalDirection.SOUTH.getDegrees(), 0f);
        Assertions.assertEquals(270f, CardinalDirection.WEST.getDegrees(), 0f);
    }
}
