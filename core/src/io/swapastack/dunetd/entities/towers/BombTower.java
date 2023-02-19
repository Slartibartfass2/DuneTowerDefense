package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.vectors.Vector2;

import org.jetbrains.annotations.Nullable;

import lombok.NonNull;

/**
 * A bomb tower representing a part of the game grid. A bomb tower attacks all hostile units in a specified range
 * around the first hostile unit it can find.<br>
 * It consists of the same properties as a damage tower plus an area damage range, in which all hostile units are
 * attacked.
 */
public final class BombTower extends DamageTower {

    /**
     * @see Tower#getRange()
     */
    private static final float BOMB_TOWER_RANGE = Configuration.getInstance().getFloatProperty("BOMB_TOWER_RANGE");

    /**
     * @see Tower#getBuildCost()
     */
    private static final int BOMB_TOWER_BUILD_COST = Configuration.getInstance()
            .getIntProperty("BOMB_TOWER_BUILD_COST");

    /**
     * @see DamageTower#getDamage()
     */
    private static final int BOMB_TOWER_DAMAGE = Configuration.getInstance().getIntProperty("BOMB_TOWER_DAMAGE");

    /**
     * @see Tower#getReloadTimeInMilliseconds()
     */
    private static final int BOMB_TOWER_RELOAD_TIME_IN_MILLISECONDS = Configuration.getInstance()
            .getIntProperty("BOMB_TOWER_RELOAD_TIME_IN_MILLISECONDS");

    /**
     * Range around first found hostile unit, in which any other hostile units are attacked
     */
    private static final float AREA_DAMAGE_RANGE = Configuration.getInstance()
            .getFloatProperty("BOMB_TOWER_AREA_DAMAGE_RANGE");

    /**
     * Creates a new bomb tower with a specified position.
     *
     * @param position         Position of this bomb tower
     * @param entityController Controller for towers
     */
    public BombTower(@NonNull Vector2 position, @Nullable EntityController entityController) {
        super(position, BOMB_TOWER_RANGE, BOMB_TOWER_BUILD_COST, BOMB_TOWER_DAMAGE,
                BOMB_TOWER_RELOAD_TIME_IN_MILLISECONDS, entityController);
    }

    /**
     * Searches for at least one hostile unit in range of this bomb tower and searches for every other hostile unit in a
     * range around its position and applies its damage to every hostile unit found in this area.
     *
     * @param killOrder    If this bomb tower should also shoot the target
     * @param hostileUnits Hostile units available to target
     * @return True if attack was successful (tower attacked at least one hostile unit)
     */
    @Override
    protected boolean target(boolean killOrder, @NonNull HostileUnit... hostileUnits) {
        var hostileUnitsInRange = getHostileUnitsInRange(hostileUnits);

        // TODO: use guard case
        // If there's at least one hostile unit, search for more in the area damage range around the one in normal range
        if (!hostileUnitsInRange.isEmpty()) {
            var hostileUnitInRange = hostileUnitsInRange.get(0);

            rotateToHostileUnit(hostileUnitInRange);

            if (killOrder) {
                // Get hostile units in area damage range around found hostile unit
                var hostileUnitsInAreaDamageRange = getHostileUnitsInRange(hostileUnitInRange.getPosition(),
                        AREA_DAMAGE_RANGE, hostileUnits);

                // Deal damage to each hostile unit
                for (var hostileUnit : hostileUnitsInAreaDamageRange) {
                    hostileUnit.dealDamage(getDamage());
                }

                return true;
            }
        }

        return false;
    }
}
