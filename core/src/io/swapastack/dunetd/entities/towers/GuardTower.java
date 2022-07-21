package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.game.EntityController;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import lombok.NonNull;

import java.util.List;

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
    private static final int GUARD_TOWER_BUILD_COST = Configuration.getInstance().getIntProperty("GUARD_TOWER_BUILD_COST");

    /**
     * @see Tower#reloadTimeInMs
     */
    private static final int GUARD_TOWER_DAMAGE = Configuration.getInstance().getIntProperty("GUARD_TOWER_DAMAGE");

    /**
     * @see DamageTower#damage
     */
    private static final int GUARD_TOWER_RELOAD_TIME_IN_MS = Configuration.getInstance().getIntProperty("GUARD_TOWER_RELOAD_TIME_IN_MS");

    /**
     * Creates a new guard tower with a specified position.
     *
     * @param x X coordinate of position
     * @param y Y coordinate of position
     */
    public GuardTower(int x, int y) {
        super(x, y, GUARD_TOWER_RANGE, GUARD_TOWER_BUILD_COST, GUARD_TOWER_DAMAGE, GUARD_TOWER_RELOAD_TIME_IN_MS);
    }

    /**
     * Creates a new guard tower with a specified position.
     *
     * @param x                X coordinate of position
     * @param y                Y coordinate of position
     * @param entityController Controller for towers
     */
    public GuardTower(int x, int y, @NonNull EntityController entityController) {
        super(x, y, GUARD_TOWER_RANGE, GUARD_TOWER_BUILD_COST, GUARD_TOWER_DAMAGE, GUARD_TOWER_RELOAD_TIME_IN_MS, entityController);
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