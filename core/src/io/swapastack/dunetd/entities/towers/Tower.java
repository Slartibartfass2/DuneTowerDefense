package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.game.GameModelData;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.math.DuneTDMath;
import io.swapastack.dunetd.vectors.Vector2;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * A tower representing a part of the game grid. A tower can be: <br>
 * - a sound tower: a tower which slows down hostile units in range. <br>
 * - a damage tower: a tower which deals damage to hostile units in range. <br>
 * It consists of a position, range, build cost and reload time.
 */
@EqualsAndHashCode(callSuper = true)
public abstract class Tower extends Entity {

    /**
     * Range of this tower, in which it attacks hostile units
     */
    @Getter
    private final float range;

    /**
     * Range of this tower squared for faster calculation purposes
     */
    private final float rangeSquared;

    /**
     * Costs to build this tower
     */
    @Getter
    private final int buildCost;

    /**
     * Time in milliseconds needed to reload
     */
    @Getter
    private final int reloadTimeInMilliseconds;

    /**
     * Current reload time in milliseconds
     */
    @Getter(AccessLevel.PROTECTED)
    private int currentReloadTimeInMilliseconds;

    /**
     * True if this tower is a debris (if getting destroyed by the shai hulud)
     */
    @Getter
    private boolean isDebris;

    /**
     * Creates a new tower with a specified position, range, build cost and reload time.
     *
     * @param position                 Position of this tower
     * @param range                    Range of the tower, in which it attacks hostile units
     * @param buildCost                Costs to build this tower
     * @param reloadTimeInMilliseconds Time in milliseconds needed to reload
     * @param entityController         Controller for towers
     * @param startRotation            Start rotation of game model
     */
    protected Tower(@NonNull Vector2 position, float range, int buildCost, int reloadTimeInMilliseconds,
                    @Nullable EntityController entityController, float startRotation) {
        super(position, entityController, startRotation);

        this.range = range;
        rangeSquared = range * range;
        this.buildCost = buildCost;
        this.reloadTimeInMilliseconds = reloadTimeInMilliseconds;
        currentReloadTimeInMilliseconds = 0;
        isDebris = false;
    }

    /**
     * Adds game model of this tower to the scene manager.
     */
    public void show() {
        firePropertyChange(EntityController.SHOW_EVENT_NAME, null);
    }

    /**
     * Updates the logic of this tower (waiting for reload, attacking when reload finished, doing nothing when this
     * tower is a debris).
     *
     * @param hostileUnits            Hostile units available on the grid
     * @param deltaTimeInMilliseconds The time in milliseconds since the last update
     */
    public final void update(float deltaTimeInMilliseconds, @NonNull HostileUnit... hostileUnits) {
        if (isDebris) {
            return;
        }

        // Subtract time passed from reload time
        if (currentReloadTimeInMilliseconds > 0) {
            currentReloadTimeInMilliseconds -= deltaTimeInMilliseconds;
        }

        // Attack as long as current reload time is less than or equal to zero (if delta time > reload time)
        while (target(currentReloadTimeInMilliseconds <= 0, hostileUnits)) {
            currentReloadTimeInMilliseconds += reloadTimeInMilliseconds;
        }
    }

    /**
     * Rotates to a hostile unit in range and attacks one or more hostile units in {@code hostileUnits} if possible
     * and allowed by {@code killOrder}.
     *
     * @param killOrder    If this tower should also shoot the target
     * @param hostileUnits Hostile units available to target
     * @return True if attack was successful (tower attacked at least one hostile unit)
     */
    protected abstract boolean target(boolean killOrder, @NonNull HostileUnit... hostileUnits);

    /**
     * Checks if there are hostile units in {@code hostileUnits}, which are in range of this tower and returns them.
     *
     * @param hostileUnits Hostile units on the grid
     * @return Hostile units, which are in range of the tower
     */
    @NotNull
    protected final ArrayList<HostileUnit> getHostileUnitsInRange(@NonNull HostileUnit... hostileUnits) {
        return getHostileUnitsInRange(getPosition(), rangeSquared, hostileUnits);
    }

    /**
     * Checks if there are hostile units in <code>hostileUnits</code>, which are in range of the specified position and
     * returns them.
     *
     * @param hostileUnits Hostile units on the grid
     * @param position     Position to search around
     * @param rangeSquared Range around the position squared, where hostile units can be found
     * @return Hostile units, which are in range of the position
     */
    @NotNull
    protected static ArrayList<HostileUnit> getHostileUnitsInRange(@NonNull Vector2 position, float rangeSquared,
                                                                   @NonNull HostileUnit... hostileUnits) {
        var hostileUnitsInRange = new ArrayList<HostileUnit>();

        // Check if hostile unit is in range, if so add it to list
        for (var hostileUnit : hostileUnits) {
            if (hostileUnit.isDead()) {
                continue;
            }

            // Calculate distance squared
            var hostileUnitPosition = hostileUnit.getPosition();
            var distanceSquared = position.getDistanceSquared(hostileUnitPosition);

            if (distanceSquared <= rangeSquared) {
                hostileUnitsInRange.add(hostileUnit);
            }
        }

        return hostileUnitsInRange;
    }

    /**
     * Rotates the game model of this tower to face {@code hostileUnit}.
     *
     * @param hostileUnit Hostile unit to rotate to
     */
    protected final void rotateToHostileUnit(@NonNull HostileUnit hostileUnit) {
        float rotation = DuneTDMath.getAngle(hostileUnit.getPosition(), getPosition());
        var gameModelData = new GameModelData(rotation, getPosition());
        firePropertyChange(EntityController.UPDATE_EVENT_NAME, gameModelData);
    }

    /**
     * Turns this tower into a debris.
     */
    public final void setToDebris() {
        isDebris = true;
        firePropertyChange(EntityController.TO_DEBRIS_EVENT_NAME, true);
    }

    /**
     * Removes game model of this tower from scene manager.
     */
    public void destroy() {
        firePropertyChange(EntityController.DESTROY_EVENT_NAME, null);
    }
}
