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
     * @see HostileUnit#getSpeed()
     */
    private static final float HARVESTER_SPEED = Configuration.getInstance().getFloatProperty("HARVESTER_SPEED");

    /**
     * @see HostileUnit#getHealth()
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
     * Returns the spice reward for killing this harvester.
     *
     * @return Spice reward for killing this harvester
     */
    @Override
    public int getSpiceReward() {
        return HARVESTER_SPICE_REWARD;
    }

    @Override
    protected float getSlowingEffect(float slowingEffectMultiplier) {
        return MathUtils.clamp(slowingEffectMultiplier * SLOWING_EFFECT_RESISTANCE_MULTIPLIER, 0f, 1f);
    }
}
