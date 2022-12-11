package io.swapastack.dunetd.game;

import io.swapastack.dunetd.vectors.Vector2;

import lombok.Getter;
import lombok.NonNull;

/**
 * Represents cardinal directions, including the degrees and the normalized direction vector. The possible values are:
 * <br>
 * NORTH(0°, [0, 1])<br>
 * EAST(90°, [1, 0])<br>
 * SOUTH(180°, [0, -1])<br>
 * WEST(270°, [-1, 0])
 */
public enum CardinalDirection {
    NORTH(0f, new Vector2(0, 1)),
    EAST(90f, new Vector2(1, 0)),
    SOUTH(180f, new Vector2(0, -1)),
    WEST(270f, new Vector2(-1, 0));

    @Getter
    private final float degrees;

    @Getter
    private final Vector2 direction;

    CardinalDirection(float degrees, @NonNull Vector2 direction) {
        this.degrees = degrees;
        this.direction = direction;
    }

    /**
     * Creates cardinal direction from direction vector.
     *
     * @param direction normalized direction vector, which points in one of the cardinal directions
     * @return Cardinal direction according to specified direction vector
     * @throws IllegalStateException If the direction vector doesn't match any of the cardinal directions vectors
     */
    public static CardinalDirection fromDirection(@NonNull Vector2 direction) throws IllegalStateException {
        if (direction.equals(NORTH.direction)) {
            return NORTH;
        } else if (direction.equals(EAST.direction)) {
            return EAST;
        } else if (direction.equals(SOUTH.direction)) {
            return SOUTH;
        } else if (direction.equals(WEST.direction)) {
            return WEST;
        } else {
            throw new IllegalStateException("direction shouldn't be " + direction);
        }
    }
}
