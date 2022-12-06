package io.swapastack.dunetd.math;

import com.badlogic.gdx.Gdx;

/**
 * Class to calculate the amount of pixels matching the percentage of the screen dimensions.
 */
public final class PixelsConverter {

    private PixelsConverter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Returns the amount of pixels matching the specified percentage of the screen width.
     *
     * @param percentage Percentage of the screen width
     * @return Pixels matching the percentage of the screen width
     */
    public static float getX(float percentage) {
        return Gdx.graphics.getWidth() * percentage;
    }

    /**
     * Returns the amount of pixels matching the specified percentage of the screen height.
     *
     * @param percentage Percentage of the screen height
     * @return Pixels matching the percentage of the screen height
     */
    public static float getY(float percentage) {
        return Gdx.graphics.getHeight() * percentage;
    }
}
