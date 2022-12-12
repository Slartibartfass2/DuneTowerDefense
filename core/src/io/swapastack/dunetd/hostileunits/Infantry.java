package io.swapastack.dunetd.hostileunits;

import io.swapastack.dunetd.assets.controller.HostileUnitController;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.vectors.Vector2;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import lombok.NonNull;

/**
 * An infantry representing an enemy in this game.
 * It is a fast but weak ground unit.
 */
public final class Infantry extends HostileUnit {

    /**
     * @see HostileUnit#getSpeed()
     */
    private static final float INFANTRY_SPEED = Configuration.getInstance().getFloatProperty("INFANTRY_SPEED");

    /**
     * @see HostileUnit#getHealth()
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
    @TestOnly
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
     * @return Spice reward for killing the infantry
     */
    @Override
    public int getSpiceReward() {
        return INFANTRY_SPICE_REWARD;
    }
}
