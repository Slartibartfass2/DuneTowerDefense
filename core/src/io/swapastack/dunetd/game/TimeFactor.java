package io.swapastack.dunetd.game;

import lombok.Getter;

public enum TimeFactor {
    QUARTER(0.25f),
    THIRD(0.33f),
    HALF(0.5f),
    NORMAL(1f),
    DOUBLE(2f),
    TRIPLE(3f),
    QUADRUPLE(4f);

    /**
     * Factor to multiply with delta time in the render methods to speed up or slow down time
     */
    @Getter
    private final float factor;

    TimeFactor(float factor) {
        this.factor = factor;
    }

    public TimeFactor speedUp() {
        return switch (this) {
            case QUARTER -> THIRD;
            case THIRD -> HALF;
            case HALF -> NORMAL;
            case NORMAL -> DOUBLE;
            case DOUBLE -> TRIPLE;
            default -> QUADRUPLE;
        };
    }

    public TimeFactor slowDown() {
        return switch (this) {
            case QUADRUPLE -> TRIPLE;
            case TRIPLE -> DOUBLE;
            case DOUBLE -> NORMAL;
            case NORMAL -> HALF;
            case HALF -> THIRD;
            default -> QUARTER;
        };
    }
}