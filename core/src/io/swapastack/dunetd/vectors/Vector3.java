package io.swapastack.dunetd.vectors;

import lombok.NonNull;

/**
 * A three-dimensional vector.
 *
 * @param x X coordinate of vector
 * @param y Y coordinate of vector
 * @param z Z coordinate of vector
 */
public record Vector3(float x, float y, float z) {

    public static final Vector3 ZERO = new Vector3(0, 0, 0);

    /**
     * Calculates the length of this vector.
     *
     * @return The length of this vector.
     */
    public float getLength() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Calculates the normalized vector of this vector. A normalized vector has the length 1.
     *
     * @return The normalized vector of this vector
     */
    public Vector3 normalize() {
        float length = getLength();
        return new Vector3(x / length, y / length, z / length);
    }

    /**
     * Creates a three-dimensional vector from the specified two-dimensional vector with a y value of the specified y.
     *
     * @param vector two-dimensional vector to convert
     * @return Three-dimensional vector with the coordinates (vector.x, y, vector.y)
     */
    public static Vector3 fromVector2(@NonNull Vector2 vector, float y) {
        return new Vector3(vector.x(), y, vector.y());
    }

    /**
     * Creates a vector which is equal to the first specified vector and the second specified vector added together.
     *
     * @param vector1 First vector to add to second vector
     * @param vector2 Second vector to add to first vector
     * @return The first specified vector and the second specified vector added together
     */
    public static Vector3 add(@NonNull Vector3 vector1, @NonNull Vector3 vector2) {
        return new Vector3(vector1.x + vector2.x, vector1.y + vector2.y, vector1.z + vector2.z);
    }

    /**
     * Creates a vector which is equal to the first specified vector with the second specified vector subtracted.
     *
     * @param vector1 First vector to be subtracted by second vector
     * @param vector2 Second vector to subtract from first vector
     * @return The first specified vector with the second specified vector subtracted
     */
    public static Vector3 subtract(@NonNull Vector3 vector1, @NonNull Vector3 vector2) {
        return new Vector3(vector1.x - vector2.x, vector1.y - vector2.y, vector1.z - vector2.z);
    }

    /**
     * Creates a vector which is equal to the specified vector multiplied by the specified scalar.
     *
     * @param vector Vector to multiply with scalar
     * @param scalar Scalar to multiply with vector
     * @return Specified vector multiplied by the specified scalar
     */
    public static Vector3 multiply(@NonNull Vector3 vector, float scalar) {
        return new Vector3(vector.x * scalar, vector.y * scalar, vector.z * scalar);
    }

    public com.badlogic.gdx.math.Vector3 toLibGdx() {
        return new com.badlogic.gdx.math.Vector3(x, y, z);
    }

    public static Vector3 fromLibGdx(@NonNull com.badlogic.gdx.math.Vector3 vector) {
        return new Vector3(vector.x, vector.y, vector.z);
    }
}
