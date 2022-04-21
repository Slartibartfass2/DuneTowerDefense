package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.game.HostileUnitController;
import lombok.NonNull;

/**
 * A harvester representing an enemy in this game.
 * It is a low air unit, which has a resistance against the sound towers slowing effect.
 */
public final class Harvester extends HostileUnit {
    
    /** @see HostileUnit#speed */
    private static final float HARVESTER_SPEED = Configuration.getInstance().getFloatProperty("HARVESTER_SPEED");
    
    /** @see HostileUnit#health */
    private static final int HARVESTER_INITIAL_HEALTH = Configuration.getInstance().getIntProperty("HARVESTER_INITIAL_HEALTH");
    
    /** @see HostileUnit#getSpiceReward() */
    private static final int HARVESTER_SPICE_REWARD = Configuration.getInstance().getIntProperty("HARVESTER_SPICE_REWARD");
    
    /** Multiplier to resist the slowing effect of the sound tower. */
    private static final float SLOWING_EFFECT_RESISTANCE_MULTIPLIER = Configuration.getInstance().getFloatProperty("HARVESTER_SLOWING_EFFECT_RESISTANCE_MULTIPLIER");

    /**
     * Creates a new harvester with a specified position.
     *
     * @param position Position of this harvester
     */
    public Harvester(@NonNull Vector2 position) {
        super(position, HARVESTER_SPEED, HARVESTER_INITIAL_HEALTH);
    }

    /**
     * Creates a new harvester with a specified position.
     *
     * @param position Position of this harvester
     * @param hostileUnitController Controller for hostile units
     */
    public Harvester(@NonNull Vector2 position, @NonNull HostileUnitController hostileUnitController) {
        super(position, HARVESTER_SPEED, HARVESTER_INITIAL_HEALTH, hostileUnitController);
    }
    
    /**
     * Slows down the harvester by decreasing the speed to the value of <code>speed * slowingEffectMultiplier *
     * SLOWING_EFFECT_RESISTANCE_MULTIPLIER</code>. The effect lasts as long as the specified <code>slowingEffectDurationInMs</code>.
     * The slowing effect can't exceed the range [0, 1].
     *
     * @param slowingEffectMultiplier   Value to multiply with speed to set the new speed
     * @param slowingEffectDurationInMs Duration of slowing effect.
     */
    @Override
    public void slowDown(float slowingEffectMultiplier, int slowingEffectDurationInMs) {
        float slowingEffect = MathUtils.clamp(slowingEffectMultiplier * SLOWING_EFFECT_RESISTANCE_MULTIPLIER, 0f, 1f);
        currentSpeed = speed * slowingEffect;
        this.slowingEffectDurationInMs = slowingEffectDurationInMs;
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