package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A bomb tower representing a part of the game grid. A bomb tower attacks all hostile units in a specified range
 * around the first hostile unit it can find.<br>
 * It consists of the same properties as a damage tower plus an area damage range, in which all hostile units are
 * attacked.
 */
public final class BombTower extends DamageTower {

    /**
     * @see Tower#range
     */
    private static final float BOMB_TOWER_RANGE = Configuration.getInstance().getFloatProperty("BOMB_TOWER_RANGE");

    /**
     * @see Tower#buildCost
     */
    private static final int BOMB_TOWER_BUILD_COST = Configuration.getInstance().getIntProperty("BOMB_TOWER_BUILD_COST");

    /**
     * @see Tower#reloadTimeInMs
     */
    private static final int BOMB_TOWER_DAMAGE = Configuration.getInstance().getIntProperty("BOMB_TOWER_DAMAGE");

    /**
     * @see DamageTower#damage
     */
    private static final int BOMB_TOWER_RELOAD_TIME_IN_MS = Configuration.getInstance().getIntProperty("BOMB_TOWER_RELOAD_TIME_IN_MS");

    /**
     * Range around first found hostile unit, in which any other hostile units are attacked
     */
    private static final float AREA_DAMAGE_RANGE = Configuration.getInstance().getFloatProperty("BOMB_TOWER_AREA_DAMAGE_RANGE");

    /**
     * Creates a new bomb tower with a specified position.
     *
     * @param x X coordinate of position
     * @param y Y coordinate of position
     */
    public BombTower(int x, int y) {
        this(x, y, null);
    }

    /**
     * Creates a new bomb tower with a specified position.
     *
     * @param x                X coordinate of position
     * @param y                Y coordinate of position
     * @param entityController Controller for towers
     */
    public BombTower(int x, int y, @Nullable EntityController entityController) {
        super(x, y, BOMB_TOWER_RANGE, BOMB_TOWER_BUILD_COST, BOMB_TOWER_DAMAGE, BOMB_TOWER_RELOAD_TIME_IN_MS, entityController);
    }

    /**
     * Searches for at least one hostile unit in range of this bomb tower and searches for every other hostile unit in a
     * range around its position and applies its damage to every hostile unit found in this area.
     *
     * @param hostileUnits Hostile units available to target
     * @param killOrder    If this bomb tower should also shoot the target
     * @return True if attack was successful (tower attacked at least one hostile unit)
     */
    @Override
    protected boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder) {
        var hostileUnitsInRange = getHostileUnitsInRange(hostileUnits);

        // If there's at least one hostile unit, search for more in the area damage range around the one in normal range
        if (!hostileUnitsInRange.isEmpty()) {
            var hostileUnitInRange = hostileUnitsInRange.get(0);

            rotateToHostileUnit(hostileUnitInRange);

            if (killOrder) {
                // Get hostile units in area damage range around found hostile unit
                var hostileUnitsInAreaDamageRange = getHostileUnitsInRange(hostileUnits, hostileUnitInRange.getPosition(), AREA_DAMAGE_RANGE);

                // Deal damage to each hostile unit
                for (var hostileUnit : hostileUnitsInAreaDamageRange) {
                    hostileUnit.dealDamage(damage);
                }

                return true;
            }
        }

        return false;
    }
}