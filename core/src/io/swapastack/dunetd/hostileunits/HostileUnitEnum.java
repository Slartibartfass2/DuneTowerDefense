package io.swapastack.dunetd.hostileunits;

import io.swapastack.dunetd.assets.controller.HostileUnitController;
import io.swapastack.dunetd.vectors.Vector2;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.NonNull;

/**
 * Represents a selection of hostile units.
 */
public enum HostileUnitEnum {
    INFANTRY,
    BOSS_UNIT,
    HARVESTER;

    /**
     * Returns the corresponding hostile unit enum to the specified hostile unit.
     *
     * @param hostileUnit Hostile unit from which the corresponding hostile unit enum is returned
     * @return Corresponding hostile unit enum to the specified hostile unit
     * @throws IllegalStateException If the hostile unit is not an infantry, a harvester or a boss unit
     */
    public static HostileUnitEnum fromHostileUnit(@NonNull HostileUnit hostileUnit) throws IllegalStateException {
        if (hostileUnit instanceof Infantry) {
            return INFANTRY;
        } else if (hostileUnit instanceof Harvester) {
            return HARVESTER;
        } else if (hostileUnit instanceof BossUnit) {
            return BOSS_UNIT;
        }
        throw new IllegalStateException("Unexpected hostile unit: " + hostileUnit);
    }

    /**
     * Creates a hostile unit from hostile unit enum.
     *
     * @param spawnPoint            Spawn point of hostile unit
     * @param hostileUnitController Controller for hostile units
     * @return The hostile unit object created from the hostile unit enum
     */
    @NotNull
    public HostileUnit toHostileUnit(@NonNull Vector2 spawnPoint,
                                     @Nullable HostileUnitController hostileUnitController) {
        return switch (this) {
            case INFANTRY -> new Infantry(spawnPoint, hostileUnitController);
            case BOSS_UNIT -> new BossUnit(spawnPoint, hostileUnitController);
            case HARVESTER -> new Harvester(spawnPoint, hostileUnitController);
        };
    }
}
