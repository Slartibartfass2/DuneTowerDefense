package io.swapastack.dunetd.game;

import io.swapastack.dunetd.vectors.Vector2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CardinalDirectionTest {

    @Test
    void whenFromDirectionIsCalledWithValidDirections_thenCorrectEnumIsReturned() {
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
    void whenFromDirectionIsCalledWithInvalidDirection_thenExceptionIsThrown() {
        var direction = new Vector2(3, 4);

        Assertions.assertThrows(IllegalStateException.class, () -> CardinalDirection.fromDirection(direction));
    }
}
