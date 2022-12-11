package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.MathUtils;

import io.swapastack.dunetd.assets.controller.HostileUnitController;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.vectors.Vector2;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import lombok.NonNull;

/**
 * A harvester representing an enemy in this game.
 * It is a low air unit, which has a resistance against the sound towers slowing effect.
 */
public final class Harvester extends HostileUnit {

    /**
     * @see HostileUnit#speed
     */
    private static final float HARVESTER_SPEED = Configuration.getInstance().getFloatProperty("HARVESTER_SPEED");

    /**
     * @see HostileUnit#health
     */
    private static final int HARVESTER_INITIAL_HEALTH = Configuration.getInstance()
            .getIntProperty("HARVESTER_INITIAL_HEALTH");

    /**
     * @see HostileUnit#getSpiceReward()
     */
    private static final int HARVESTER_SPICE_REWARD = Configuration.getInstance()
            .getIntProperty("HARVESTER_SPICE_REWARD");

    /**
     * Multiplier to resist the slowing effect of the sound tower.
     */
    private static final float SLOWING_EFFECT_RESISTANCE_MULTIPLIER = Configuration.getInstance()
            .getFloatProperty("HARVESTER_SLOWING_EFFECT_RESISTANCE_MULTIPLIER");

    /**
     * Creates a new harvester with a specified position.
     *
     * @param position Position of this harvester
     */
    @TestOnly
    public Harvester(@NonNull Vector2 position) {
        this(position, null);
    }

    /**
     * Creates a new harvester with a specified position.
     *
     * @param position              Position of this harvester
     * @param hostileUnitController Controller for hostile units
     */
    public Harvester(@NonNull Vector2 position, @Nullable HostileUnitController hostileUnitController) {
        super(position, HARVESTER_SPEED, HARVESTER_INITIAL_HEALTH, hostileUnitController);
    }

    /**
     * Slows down the harvester by decreasing the speed to the value of <code>speed * slowingEffectMultiplier *
     * SLOWING_EFFECT_RESISTANCE_MULTIPLIER</code>. The effect lasts as long as the specified
     * slowingEffectDurationInMilliseconds. The slowing effect can't exceed the range [0, 1].
     *
     * @param slowingEffectMultiplier                    Value to multiply with speed to set the new speed
     * @param appliedSlowingEffectDurationInMilliseconds Duration of slowing effect in milliseconds.
     */
    @Override
    public void slowDown(float slowingEffectMultiplier, int appliedSlowingEffectDurationInMilliseconds) {
        var slowingEffect = MathUtils.clamp(slowingEffectMultiplier * SLOWING_EFFECT_RESISTANCE_MULTIPLIER, 0f, 1f);
        currentSpeed = speed * slowingEffect;
        this.slowingEffectDurationInMilliseconds = appliedSlowingEffectDurationInMilliseconds;
    }

    /**
     * Returns the spice reward for killing this harvester.
     *
     * @return Spice reward for killing this harvester
     */
    @Override
    public int getSpiceReward() {
        return HARVESTER_SPICE_REWARD;
    }
}
