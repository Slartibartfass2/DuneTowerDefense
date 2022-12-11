package io.swapastack.dunetd.hostileunits;

import io.swapastack.dunetd.assets.controller.HostileUnitController;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.vectors.Vector2;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import lombok.NonNull;

/**
 * A boss unit representing an enemy in this game.
 * It is a strong but slow ground unit.
 */
public final class BossUnit extends HostileUnit {

    /**
     * @see HostileUnit#speed
     */
    private static final float BOSS_UNIT_SPEED = Configuration.getInstance().getFloatProperty("BOSS_UNIT_SPEED");

    /**
     * @see HostileUnit#health
     */
    private static final int BOSS_UNIT_INITIAL_HEALTH = Configuration.getInstance()
            .getIntProperty("BOSS_UNIT_INITIAL_HEALTH");

    /**
     * @see HostileUnit#getSpiceReward()
     */
    private static final int BOSS_UNIT_SPICE_REWARD = Configuration.getInstance()
            .getIntProperty("BOSS_UNIT_SPICE_REWARD");

    /**
     * Creates a new boss unit with a specified position.
     *
     * @param position Position of this boss unit
     */
    @TestOnly
    public BossUnit(@NonNull Vector2 position) {
        this(position, null);
    }

    /**
     * Creates a new boss unit with a specified position.
     *
     * @param position              Position of this boss unit
     * @param hostileUnitController Controller for hostile units
     */
    public BossUnit(@NonNull Vector2 position, @Nullable HostileUnitController hostileUnitController) {
        super(position, BOSS_UNIT_SPEED, BOSS_UNIT_INITIAL_HEALTH, hostileUnitController);
    }

    /**
     * Slows down the boss unit by decreasing the speed to the value of <code>speed * slowingEffectMultiplier</code>.
     * The effect lasts as long as the specified slowingEffectDurationInMilliseconds.
     *
     * @param slowingEffectMultiplier                    Value to multiply with speed to set the new speed
     * @param appliedSlowingEffectDurationInMilliseconds Duration of slowing effect in milliseconds.
     */
    @Override
    public void slowDown(float slowingEffectMultiplier, int appliedSlowingEffectDurationInMilliseconds) {
        currentSpeed = speed * slowingEffectMultiplier;
        this.slowingEffectDurationInMilliseconds = appliedSlowingEffectDurationInMilliseconds;
    }

    /**
     * Returns the spice reward for killing this boss unit.
     *
     * @return Spice reward for killing this boss unit
     */
    @Override
    public int getSpiceReward() {
        return BOSS_UNIT_SPICE_REWARD;
    }
}
