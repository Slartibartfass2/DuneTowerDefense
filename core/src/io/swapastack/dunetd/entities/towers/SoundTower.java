package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A sound tower representing a part of the game grid. A sound tower slows down hostile units in range. <br>
 * It consists of the same properties as a tower plus a slowing effect multiplier and a slowing effect
 * duration, which specify the slowing effect.
 */
public final class SoundTower extends Tower {

    /**
     * @see Tower#range
     */
    private static final float SOUND_TOWER_RANGE = Configuration.getInstance().getFloatProperty("SOUND_TOWER_RANGE");

    /**
     * @see Tower#buildCost
     */
    private static final int SOUND_TOWER_BUILD_COST = Configuration.getInstance().getIntProperty("SOUND_TOWER_BUILD_COST");

    /**
     * @see Tower#reloadTimeInMs
     */
    private static final int SOUND_TOWER_RELOAD_TIME_IN_MS = Configuration.getInstance().getIntProperty("SOUND_TOWER_RELOAD_TIME_IN_MS");

    /**
     * Value between zero and one, multiplied with the speed result in the slowed down speed
     */
    private static final float SLOWING_EFFECT_MULTIPLIER = Configuration.getInstance().getFloatProperty("SOUND_TOWER_SLOWING_EFFECT_MULTIPLIER");

    /**
     * Duration of the slowing effect in milliseconds
     */
    private static final int SLOWING_EFFECT_DURATION_IN_MS = Configuration.getInstance().getIntProperty("SOUND_TOWER_SLOWING_EFFECT_DURATION_IN_MS");

    /**
     * Creates a new sound tower with a specified position.
     *
     * @param x X coordinate of position
     * @param y Y coordinate of position
     */
    public SoundTower(int x, int y) {
        this(x, y, null);
    }

    /**
     * Creates a new sound tower with a specified position.
     *
     * @param x                X coordinate of position
     * @param y                Y coordinate of position
     * @param entityController Controller for towers
     */
    public SoundTower(int x, int y, @Nullable EntityController entityController) {
        super(x, y, SOUND_TOWER_RANGE, SOUND_TOWER_BUILD_COST, SOUND_TOWER_RELOAD_TIME_IN_MS, entityController, 0f);
    }

    /**
     * Searches for every hostile unit in range of this sound tower and applies a slowing effect to each one.
     *
     * @param hostileUnits Hostile units available to target
     * @param killOrder    If this sound tower should also slow down the target
     * @return True if attack was successful (tower attacked at least one hostile unit)
     */
    @Override
    protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
        var hostileUnitsInRange = getHostileUnitsInRange(hostileUnits);

        // If there are no hostile units in range the attack wasn't successful
        if (hostileUnitsInRange.isEmpty() || !killOrder) {
            return false;
        }

        // Slow down every hostile unit in range
        for (var hostileUnit : hostileUnitsInRange) {
            hostileUnit.slowDown(SLOWING_EFFECT_MULTIPLIER, SLOWING_EFFECT_DURATION_IN_MS);
        }

        return true;
    }

    /**
     * Randomly rotates to simulate searching for hostile units, when none are in range.
     *
     * @param deltaTime The time in seconds since the last update
     */
    @Override
    protected void idle(float deltaTime) {
        // The sound tower doesn't rotate, so it won't do anything while idling.
    }
}
