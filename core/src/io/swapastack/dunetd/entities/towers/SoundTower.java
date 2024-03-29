package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.vectors.Vector2;

import org.jetbrains.annotations.Nullable;

import lombok.NonNull;

/**
 * A sound tower representing a part of the game grid. A sound tower slows down hostile units in range. <br>
 * It consists of the same properties as a tower plus a slowing effect multiplier and a slowing effect
 * duration, which specify the slowing effect.
 */
public final class SoundTower extends Tower {

    /**
     * @see Tower#getRange()
     */
    private static final float SOUND_TOWER_RANGE = Configuration.getInstance().getFloatProperty("SOUND_TOWER_RANGE");

    /**
     * @see Tower#getBuildCost()
     */
    private static final int SOUND_TOWER_BUILD_COST = Configuration.getInstance()
            .getIntProperty("SOUND_TOWER_BUILD_COST");

    /**
     * @see Tower#getReloadTimeInMilliseconds()
     */
    private static final int SOUND_TOWER_RELOAD_TIME_IN_MILLISECONDS = Configuration.getInstance()
            .getIntProperty("SOUND_TOWER_RELOAD_TIME_IN_MILLISECONDS");

    /**
     * Value between zero and one, multiplied with the speed result in the slowed down speed
     */
    private static final float SLOWING_EFFECT_MULTIPLIER = Configuration.getInstance()
            .getFloatProperty("SOUND_TOWER_SLOWING_EFFECT_MULTIPLIER");

    /**
     * Duration of the slowing effect in milliseconds
     */
    private static final int SLOWING_EFFECT_DURATION_IN_MILLISECONDS = Configuration.getInstance()
            .getIntProperty("SOUND_TOWER_SLOWING_EFFECT_DURATION_IN_MILLISECONDS");

    /**
     * Creates a new sound tower with a specified position.
     *
     * @param position         Position of this sound tower
     * @param entityController Controller for towers
     */
    public SoundTower(@NonNull Vector2 position, @Nullable EntityController entityController) {
        super(position, SOUND_TOWER_RANGE, SOUND_TOWER_BUILD_COST, SOUND_TOWER_RELOAD_TIME_IN_MILLISECONDS,
                entityController, 0f);
    }

    /**
     * Searches for every hostile unit in range of this sound tower and applies a slowing effect to each one.
     *
     * @param hostileUnits Hostile units available to target
     * @param killOrder    If this sound tower should also slow down the target
     * @return True if attack was successful (tower attacked at least one hostile unit)
     */
    @Override
    protected boolean target(boolean killOrder, @NonNull HostileUnit... hostileUnits) {
        var hostileUnitsInRange = getHostileUnitsInRange(hostileUnits);

        // If there are no hostile units in range the attack wasn't successful
        if (hostileUnitsInRange.isEmpty() || !killOrder) {
            return false;
        }

        // Slow down every hostile unit in range
        for (var hostileUnit : hostileUnitsInRange) {
            hostileUnit.slowDown(SLOWING_EFFECT_MULTIPLIER, SLOWING_EFFECT_DURATION_IN_MILLISECONDS);
        }

        return true;
    }
}
