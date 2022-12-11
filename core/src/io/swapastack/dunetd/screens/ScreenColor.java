package io.swapastack.dunetd.screens;

import lombok.Getter;

public enum ScreenColor {
    MAIN_BACKGROUND(0.2353f, 0.2471f, 0.2549f, 1f),
    BLACK(0f, 0f, 0f, 1f);

    @Getter
    private final float red;
    @Getter
    private final float green;
    @Getter
    private final float blue;
    @Getter
    private final float alpha;

    ScreenColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
}
