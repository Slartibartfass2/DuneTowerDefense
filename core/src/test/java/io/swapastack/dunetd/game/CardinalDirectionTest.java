package io.swapastack.dunetd.game;

import com.badlogic.gdx.math.Vector2;
import org.junit.Test;

import static io.swapastack.dunetd.game.CardinalDirection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CardinalDirectionTest {

    @Test
    public void testFromDirectionWithValidArguments() {
        var northVector = new Vector2(0f, 1f);
        assertEquals(NORTH, fromDirection(northVector));

        var eastVector = new Vector2(1f, 0f);
        assertEquals(EAST, fromDirection(eastVector));

        var southVector = new Vector2(0f, -1f);
        assertEquals(SOUTH, fromDirection(southVector));

        var westVector = new Vector2(-1f, 0f);
        assertEquals(WEST, fromDirection(westVector));
    }

    @Test
    public void testFromDirectionWithInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> fromDirection(null));
    }

    @Test
    public void testGetDirection() {
        var northVector = new Vector2(0f, 1f);
        assertEquals(NORTH.getDirection(), northVector);

        var eastVector = new Vector2(1f, 0f);
        assertEquals(EAST.getDirection(), eastVector);

        var southVector = new Vector2(0f, -1f);
        assertEquals(SOUTH.getDirection(), southVector);

        var westVector = new Vector2(-1f, 0f);
        assertEquals(WEST.getDirection(), westVector);
    }

    @Test
    public void testGetDegrees() {
        assertEquals(0f, NORTH.getDegrees(), 0f);
        assertEquals(90f, EAST.getDegrees(), 0f);
        assertEquals(180f, SOUTH.getDegrees(), 0f);
        assertEquals(270f, WEST.getDegrees(), 0f);
    }
}