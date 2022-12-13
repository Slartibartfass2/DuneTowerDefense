package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.vectors.Vector2;

import org.jetbrains.annotations.Nullable;

import lombok.NonNull;

/**
 * A guard tower representing a part of the game grid. A guard tower attacks only one hostile unit in range at once.<br>
 * It consists of the same properties as a damage tower.
 */
public final class GuardTower extends DamageTower {

    /**
     * @see Tower#getRange()
     */
    private static final float GUARD_TOWER_RANGE = Configuration.getInstance().getFloatProperty("GUARD_TOWER_RANGE");

    /**
     * @see Tower#getBuildCost()
     */
    private static final int GUARD_TOWER_BUILD_COST = Configuration.getInstance()
            .getIntProperty("GUARD_TOWER_BUILD_COST");

    /**
     * @see DamageTower#getDamage()
     */
    private static final int GUARD_TOWER_DAMAGE = Configuration.getInstance().getIntProperty("GUARD_TOWER_DAMAGE");

    /**
     * @see Tower#getReloadTimeInMilliseconds()
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
    protected boolean target(boolean killOrder, @NonNull HostileUnit... hostileUnits) {
        var hostileUnitsInRange = getHostileUnitsInRange(hostileUnits);

        // TODO: use guard case
        // If there's at least one hostile unit, target the first
        if (!hostileUnitsInRange.isEmpty()) {
            var hostileUnitInRange = hostileUnitsInRange.get(0);

            rotateToHostileUnit(hostileUnitInRange);

            if (killOrder) {
                hostileUnitInRange.dealDamage(getDamage());
                return true;
            }
        }

        return false;
    }
}
