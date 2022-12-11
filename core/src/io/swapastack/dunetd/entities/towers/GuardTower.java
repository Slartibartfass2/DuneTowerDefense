package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.vectors.Vector2;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import lombok.NonNull;

/**
 * A guard tower representing a part of the game grid. A guard tower attacks only one hostile unit in range at once.<br>
 * It consists of the same properties as a damage tower.
 */
public final class GuardTower extends DamageTower {

    /**
     * @see Tower#range
     */
    private static final float GUARD_TOWER_RANGE = Configuration.getInstance().getFloatProperty("GUARD_TOWER_RANGE");

    /**
     * @see Tower#buildCost
     */
    private static final int GUARD_TOWER_BUILD_COST = Configuration.getInstance()
            .getIntProperty("GUARD_TOWER_BUILD_COST");

    /**
     * @see DamageTower#damage
     */
    private static final int GUARD_TOWER_DAMAGE = Configuration.getInstance().getIntProperty("GUARD_TOWER_DAMAGE");

    /**
     * @see Tower#reloadTimeInMilliseconds
     */
    private static final int GUARD_TOWER_RELOAD_TIME_IN_MILLISECONDS = Configuration.getInstance()
            .getIntProperty("GUARD_TOWER_RELOAD_TIME_IN_MILLISECONDS");

    /**
     * Creates a new guard tower with a specified position.
     *
     * @param position         of this guard tower
     * @param entityController Controller for towers
     */
    public GuardTower(@NonNull Vector2 position, @Nullable EntityController entityController) {
        super(position, GUARD_TOWER_RANGE, GUARD_TOWER_BUILD_COST, GUARD_TOWER_DAMAGE,
                GUARD_TOWER_RELOAD_TIME_IN_MILLISECONDS, entityController);
    }

    /**
     * Searches for one hostile unit in range of this guard tower and applies its damage to it.
     *
     * @param hostileUnits Hostile units available to target
     * @param killOrder    If this guard tower should also shoot the target
     * @return True if attack was successful (tower attacked at least one hostile unit)
     */
    @Override
    protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
        var hostileUnitsInRange = getHostileUnitsInRange(hostileUnits);

        // If there's at least one hostile unit, target the first
        if (!hostileUnitsInRange.isEmpty()) {
            var hostileUnitInRange = hostileUnitsInRange.get(0);

            rotateToHostileUnit(hostileUnitInRange);

            if (killOrder) {
                hostileUnitInRange.dealDamage(damage);
                return true;
            }
        }

        return false;
    }
}
