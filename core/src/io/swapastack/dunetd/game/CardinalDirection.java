package io.swapastack.dunetd.game;

import io.swapastack.dunetd.vectors.Vector2;

import java.util.Arrays;

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
    NORTH(0, new Vector2(0, 1)),
    EAST(90, new Vector2(1, 0)),
    SOUTH(180, new Vector2(0, -1)),
    WEST(270, new Vector2(-1, 0));

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
        return Arrays.stream(CardinalDirection.values())
                .filter(cardinalDirection -> cardinalDirection.direction.equals(direction))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Passed direction didn't match any cardinal direction"));
    }
}
