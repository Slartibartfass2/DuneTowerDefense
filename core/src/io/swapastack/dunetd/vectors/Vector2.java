package io.swapastack.dunetd.vectors;

import com.badlogic.gdx.math.MathUtils;

import lombok.NonNull;

/**
 * A two-dimensional vector.
 *
 * @param x X coordinate of vector
 * @param y Y coordinate of vector
 */
public record Vector2(float x, float y) {

    public static final Vector2 ZERO = new Vector2(0, 0);

    /**
     * Calculates the length of this vector.
     *
     * @return The length of this vector.
     */
    public float getLength() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculates the distance between this vector and the specified vector.
     *
     * @param vector Vector to calculate the distance to
     * @return distance between this vector and the specified vector
     */
    public float getDistance(@NonNull Vector2 vector) {
        float deltaX = vector.x - x;
        float deltaY = vector.y - y;
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Calculates the squared distance between this vector and the specified vector.
     *
     * @param vector Vector to calculate the squared distance to
     * @return squared distance between this vector and the specified vector
     */
    public float getDistanceSquared(@NonNull Vector2 vector) {
        float deltaX = vector.x - x;
        float deltaY = vector.y - y;
        return deltaX * deltaX + deltaY * deltaY;
    }

    /**
     * @return True, if both coordinates are zero; otherwise false
     */
    public boolean isZero() {
        return x == 0 && y == 0;
    }

    /**
     * Calculates the normalized vector of this vector. A normalized vector has the length 1.
     *
     * @return The normalized vector of this vector
     */
    public Vector2 normalize() {
        float length = getLength();
        return new Vector2(x / length, y / length);
    }

    /**
     * Creates a vector which is equal to the specified vector multiplied by the specified scalar.
     *
     * @param vector Vector to multiply with scalar
     * @param scalar Scalar to multiply with vector
     * @return Specified vector multiplied by the specified scalar
     */
    public static Vector2 multiply(@NonNull Vector2 vector, float scalar) {
        return new Vector2(vector.x * scalar, vector.y * scalar);
    }

    /**
     * Creates a vector which is equal to the first specified vector and the second specified vector added together.
     *
     * @param vector1 First vector to add to second vector
     * @param vector2 Second vector to add to first vector
     * @return The first specified vector and the second specified vector added together
     */
    public static Vector2 add(@NonNull Vector2 vector1, @NonNull Vector2 vector2) {
        return new Vector2(vector1.x + vector2.x, vector1.y + vector2.y);
    }

    /**
     * Creates a vector which is equal to the specified vector and the specified coordinates added together.
     *
     * @param vector Vector to add to coordinates
     * @param x      X Coordinate to add to vector
     * @param y      Y Coordinate to add to vector
     * @return The specified vector and the specified coordinates added together
     */
    public static Vector2 add(@NonNull Vector2 vector, float x, float y) {
        return new Vector2(vector.x + x, vector.y + y);
    }

    /**
     * Creates a vector which is equal to the first specified vector with the second specified vector subtracted.
     *
     * @param vector1 First vector to be subtracted by second vector
     * @param vector2 Second vector to subtract from first vector
     * @return The first specified vector with the second specified vector subtracted
     */
    public static Vector2 subtract(@NonNull Vector2 vector1, @NonNull Vector2 vector2) {
        return new Vector2(vector1.x - vector2.x, vector1.y - vector2.y);
    }

    public float getAngleInDegrees() {
        float angle = (float) Math.atan2(y, x) * MathUtils.radiansToDegrees;
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }
}
