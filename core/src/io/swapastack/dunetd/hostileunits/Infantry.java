package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.Vector2;

import io.swapastack.dunetd.assets.controller.HostileUnitController;
import io.swapastack.dunetd.config.Configuration;

import org.jetbrains.annotations.Nullable;

import lombok.NonNull;

/**
 * An infantry representing an enemy in this game.
 * It is a fast but weak ground unit.
 */
public final class Infantry extends HostileUnit {

    /**
     * @see HostileUnit#speed
     */
    private static final float INFANTRY_SPEED = Configuration.getInstance().getFloatProperty("INFANTRY_SPEED");

    /**
     * @see HostileUnit#health
     */
    private static final int INFANTRY_INITIAL_HEALTH = Configuration.getInstance()
            .getIntProperty("INFANTRY_INITIAL_HEALTH");

    /**
     * @see HostileUnit#getSpiceReward()
     */
    private static final int INFANTRY_SPICE_REWARD = Configuration.getInstance()
            .getIntProperty("INFANTRY_SPICE_REWARD");

    /**
     * Creates a new infantry with a specified position.
     *
     * @param position Position of this infantry
     */
    public Infantry(@NonNull Vector2 position) {
        this(position, null);
    }

    /**
     * Creates a new infantry with a specified position.
     *
     * @param position              Position of this infantry
     * @param hostileUnitController Controller for hostile units
     */
    public Infantry(@NonNull Vector2 position, @Nullable HostileUnitController hostileUnitController) {
        super(position, INFANTRY_SPEED, INFANTRY_INITIAL_HEALTH, hostileUnitController);
    }

    /**
     * Slows down the infantry by decreasing the speed to the value of <code>speed * slowingEffectMultiplier</code>.
     * The effect lasts as long as the specified <code>slowingEffectDurationInMs</code>.
     *
     * @param slowingEffectMultiplier   Value to multiply with speed to set the new speed
     * @param slowingEffectDurationInMs Duration of slowing effect.
     */
    @Override
    public void slowDown(float slowingEffectMultiplier, int slowingEffectDurationInMs) {
        currentSpeed = speed * slowingEffectMultiplier;
        this.slowingEffectDurationInMs = slowingEffectDurationInMs;
    }

    /**
     * @return Spice reward for killing the infantry
     */
    @Override
    public int getSpiceReward() {
        return INFANTRY_SPICE_REWARD;
    }
}
