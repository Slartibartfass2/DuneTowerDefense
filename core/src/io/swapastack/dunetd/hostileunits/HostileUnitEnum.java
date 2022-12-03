package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.assets.controller.HostileUnitController;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        switch (this) {
            case INFANTRY -> {
                if (hostileUnitController == null) {
                    return new Infantry(spawnPoint);
                }
                return new Infantry(spawnPoint, hostileUnitController);
            }
            case BOSS_UNIT -> {
                if (hostileUnitController == null) {
                    return new BossUnit(spawnPoint);
                }
                return new BossUnit(spawnPoint, hostileUnitController);
            }
            case HARVESTER -> {
                if (hostileUnitController == null) {
                    return new Harvester(spawnPoint);
                }
                return new Harvester(spawnPoint, hostileUnitController);
            }
            default -> throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}