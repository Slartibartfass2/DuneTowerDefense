package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.game.HostileUnitController;
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
    private static final int BOSS_UNIT_INITIAL_HEALTH = Configuration.getInstance().getIntProperty("BOSS_UNIT_INITIAL_HEALTH");

    /**
     * @see HostileUnit#getSpiceReward()
     */
    private static final int BOSS_UNIT_SPICE_REWARD = Configuration.getInstance().getIntProperty("BOSS_UNIT_SPICE_REWARD");

    /**
     * Creates a new boss unit with a specified position.
     *
     * @param position Position of this boss unit
     */
    public BossUnit(@NonNull Vector2 position) {
        super(position, BOSS_UNIT_SPEED, BOSS_UNIT_INITIAL_HEALTH);
    }

    /**
     * Creates a new boss unit with a specified position.
     *
     * @param position              Position of this boss unit
     * @param hostileUnitController Controller for hostile units
     */
    public BossUnit(@NonNull Vector2 position, @NonNull HostileUnitController hostileUnitController) {
        super(position, BOSS_UNIT_SPEED, BOSS_UNIT_INITIAL_HEALTH, hostileUnitController);
    }

    /**
     * Slows down the boss unit by decreasing the speed to the value of <code>speed * slowingEffectMultiplier</code>.
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
     * Returns the spice reward for killing this boss unit.
     *
     * @return Spice reward for killing this boss unit
     */
    @Override
    public int getSpiceReward() {
        return BOSS_UNIT_SPICE_REWARD;
    }
}