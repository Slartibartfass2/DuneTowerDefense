package io.swapastack.dunetd.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.entities.Entity;
import lombok.NonNull;

/**
 * Math class to create own math methods.
 */
public final class DuneTDMath {

    private DuneTDMath() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Calculates angle between two vectors.
     *
     * @param from from vector
     * @param to   to vector
     * @return Angle between 0 and 360 degrees
     */
    public static float getAngle(@NonNull Vector2 from, @NonNull Vector2 to) {
        // Subtracting vectors to get direction vector
        var direction = to.cpy().sub(from.cpy());

        // If both vectors are the same, angle is 0
        if (direction.isZero()) {
            return 0f;
        }

        // Prevent division by zero
        if (direction.x == 0f) {
            return direction.y >= 0 ? 0f : 180f;
        }

        // Calculate degree range: [-90, 90]
        var degree = (float) Math.atan(direction.y / direction.x) * MathUtils.radiansToDegrees;

        // Adjust degree to range [0, 360]
        if (direction.x < 0) {
            return (270 - degree) % 360;
        }
        // direction.x > 0
        return 90 - degree;
    }

    /**
     * Checks if the coordinates of the specified position are inside the boundaries of grid.
     *
     * @param grid Grid where the coordinates must be inside
     * @param x    X coordinate to check
     * @param y    Y coordinate to check
     * @return true if the coordinates of gridPosition are inside the boundaries of grid, otherwise false
     */
    public static boolean isPositionInsideGrid(@NonNull Entity[][] grid, float x, float y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[(int) x].length;
    }

    /**
     * Checks if the entity at the specified position on grid is null.
     *
     * @param grid Grid to check the position
     * @param x    X coordinate to check
     * @param y    Y coordinate to check
     * @return true if the Entity at gridPosition on grid is null
     */
    public static boolean isPositionAvailable(@NonNull Entity[][] grid, int x, int y) {
        // The coordinates of the specified position must be on the grid
        if (!isPositionInsideGrid(grid, x, y)) {
            return false;
        }

        return grid[x][y] == null;
    }
}
