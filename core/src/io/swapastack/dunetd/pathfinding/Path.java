package io.swapastack.dunetd.pathfinding;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.entities.Entity;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Class to store and calculate paths on an entity grid. Waypoints are stored in an array. If path is blocked the
 * array will be empty.
 */
public final class Path {

    /**
     * Waypoints which represent the path
     */
    @Getter
    private final Vector2[] waypoints;

    /**
     * Creates a new path with an array of waypoints.
     *
     * @param waypoints Waypoints of path
     */
    private Path(@NonNull Vector2[] waypoints) {
        this.waypoints = waypoints;
    }

    /**
     * Calculates the path in the specified grid from start to end.
     *
     * @param grid  Grid on which the path should be found
     * @param start Start position of the path
     * @param end   End position of the path
     * @return A path with either zero waypoints, which means that the path is blocked by something or greater than
     * zero waypoints, which means that a path was found
     */
    public static @NotNull Path calculatePath(@NonNull Entity[][] grid, @NonNull Vector2 start, @NonNull Vector2 end) {
        if (grid.length == 0 || grid[0].length == 0) {
            throw new IllegalArgumentException("grid must consist of at least one entity");
        }

        // Find path
        var entityGraph = new EntityGraph(grid);
        var graphPath = entityGraph.findPath(start, end);

        // Convert path to waypoints
        var waypoints = new Vector2[graphPath.getCount()];
        for (int i = 0; i < graphPath.getCount(); i++) {
            var entityNode = graphPath.get(i);
            waypoints[i] = new Vector2(entityNode.getX(), entityNode.getY());
        }

        return new Path(waypoints);
    }

    /**
     * Searches for next waypoint on path after specified position.
     *
     * @param position Position from which the next waypoint is searched
     * @return The next waypoint on the path (could be same position -> end of path) or null if the position is not
     * on the path
     */
    public @Nullable Vector2 getNextWaypoint(@NonNull Vector2 position) {
        for (int i = 0; i < waypoints.length - 1; i++) {
            var firstPoint = waypoints[i];
            var secondPoint = waypoints[i + 1];

            // Check if position is first waypoint and return the second waypoint if true
            // Otherwise check if position is second waypoint and return waypoint after that if there's one
            if (position.equals(firstPoint)) {
                return secondPoint;
            } else if (position.equals(secondPoint)) {
                if (i < waypoints.length - 2) {
                    return waypoints[i + 2];  // just get 'third point' (point after secondPoint)
                } else {
                    return secondPoint; // reached end
                }
            }

            if (firstPoint.x == secondPoint.x && firstPoint.x == position.x // Vertical connection
                    && isValueInBetween(firstPoint.y, secondPoint.y, position.y)) {
                return secondPoint;

            } else if (firstPoint.y == secondPoint.y && firstPoint.y == position.y // Horizontal connection
                    && isValueInBetween(firstPoint.x, secondPoint.x, position.x)) {
                return secondPoint;

            } else if (firstPoint.x != secondPoint.x && firstPoint.y != secondPoint.y) { // Diagonal connection
                throw new IllegalStateException("Waypoints can't have a diagonal connection");
            }

            // The position is not on the connection between the two points -> check other connections
        }

        // Couldn't find next waypoint -> position is not on the path
        return null;
    }

    /**
     * Checks if testValue is in between the two limit values.
     *
     * @param value1    First limit value
     * @param value2    Second limit value
     * @param testValue Value to test
     * @return True, if testValue is in between the two limit values, otherwise false
     */
    private boolean isValueInBetween(float value1, float value2, float testValue) {
        return (value1 > testValue && testValue > value2) || (value1 < testValue && testValue < value2);
    }

    /**
     * Returns true if the path is blocked.
     *
     * @return True if the path is blocked
     */
    public boolean isBlocked() {
        return waypoints.length == 0;
    }

    /**
     * Returns the amount of waypoints, start and end position excluded
     *
     * @return Length of path
     */
    public int getLength() {
        return isBlocked() ? 0 : waypoints.length - 2;
    }

    /**
     * Returns copy of waypoint at specified index.
     *
     * @param index Index of waypoint in path
     * @return copy of waypoint at specified index
     * @throws IndexOutOfBoundsException If specified index is out of bounds
     */
    public Vector2 getWaypoint(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= waypoints.length) {
            throw new IndexOutOfBoundsException("Index " + index + " is out bounds of waypoints");
        }

        return waypoints[index].cpy();
    }

    /**
     * Creates a new path with a copied array of the same waypoints. The copy will be equal to the original (original
     * .equals(copy) = true) but won't have the same reference (original != copy).
     *
     * @return Copy of this path
     */
    public Path copy() {
        var newWaypoints = new Vector2[waypoints.length];
        for (int i = 0; i < newWaypoints.length; i++) {
            newWaypoints[i] = waypoints[i].cpy();
        }
        return new Path(newWaypoints);
    }
}