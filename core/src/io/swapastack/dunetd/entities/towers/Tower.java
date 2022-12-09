package io.swapastack.dunetd.entities.towers;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.game.GameModelData;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.math.DuneTDMath;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
    protected final float range;

    /**
     * Range of this tower squared for faster calculation purposes
     */
    protected final float rangeSquared;

    /**
     * Costs to build this tower
     */
    @Getter
    protected final int buildCost;

    /**
     * Time in milliseconds needed to reload
     */
    protected final int reloadTimeInMs;

    /**
     * Current reload time in milliseconds
     */
    protected int currentReloadTimeInMs;

    /**
     * True if this tower is a debris (if getting destroyed by the shai hulud)
     */
    @Getter
    protected boolean isDebris;

    /**
     * Creates a new tower with a specified position, range, build cost and reload time.
     *
     * @param x                X coordinate of position
     * @param y                Y coordinate of position
     * @param range            Range of the tower, in which it attacks hostile units
     * @param buildCost        Costs to build this tower
     * @param reloadTimeInMs   Time in milliseconds needed to reload
     * @param entityController Controller for towers
     * @param startRotation    Start rotation of game model
     */
    protected Tower(int x, int y, float range, int buildCost, int reloadTimeInMs,
                    @Nullable EntityController entityController, float startRotation) {
        super(x, y, entityController, startRotation);

        this.range = range;
        rangeSquared = range * range;
        this.buildCost = buildCost;
        this.reloadTimeInMs = reloadTimeInMs;
        currentReloadTimeInMs = 0;
        isDebris = false;
    }

    /**
     * Adds game model of this tower to the scene manager
     */
    public void show() {
        if (support != null) {
            support.firePropertyChange(EntityController.SHOW_EVENT_NAME, null, null);
        }
    }

    /**
     * Updates the logic of this tower (waiting for reload, attacking when reload finished, doing nothing when this
     * tower is a debris).
     *
     * @param hostileUnits Hostile units available on the grid
     * @param deltaTime    The time in seconds since the last update
     */
    public final void update(@NonNull List<HostileUnit> hostileUnits, float deltaTime) {
        if (isDebris) {
            return;
        }

        // Subtract time passed from reload time
        if (currentReloadTimeInMs > 0) {
            currentReloadTimeInMs -= deltaTime * 1000;
        }

        // Attack as long as current reload time is less than or equal to zero (if delta time > reload time)
        while (target(hostileUnits, currentReloadTimeInMs <= 0)) {
            currentReloadTimeInMs += reloadTimeInMs;
        }
    }

    /**
     * Rotates to a hostile unit in range and attacks one or more hostile units in <code>hostileUnits</code> if possible
     * and allowed by <code>killOrder</code>.
     *
     * @param hostileUnits Hostile units available to target
     * @param killOrder    If this tower should also shoot the target
     * @return True if attack was successful (tower attacked at least one hostile unit)
     */
    protected abstract boolean target(@NonNull List<HostileUnit> hostileUnits, boolean killOrder);

    /**
     * Checks if there are hostile units in <code>hostileUnits</code>, which are in range of this tower and returns
     * them.
     *
     * @param hostileUnits Hostile units on the grid
     * @return Hostile units, which are in range of the tower
     */
    @NotNull
    protected final ArrayList<HostileUnit> getHostileUnitsInRange(@NonNull List<HostileUnit> hostileUnits) {
        return getHostileUnitsInRange(hostileUnits, new Vector2(x, y), rangeSquared);
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
    protected static ArrayList<HostileUnit> getHostileUnitsInRange(@NonNull List<HostileUnit> hostileUnits,
                                                                   @NonNull Vector2 position, float rangeSquared) {
        var hostileUnitsInRange = new ArrayList<HostileUnit>();

        // Check if hostile unit is in range, if so add it to list
        for (var hostileUnit : hostileUnits) {
            if (hostileUnit.isDead()) {
                continue;
            }

            // Calculate distance squared
            var hostileUnitPosition = hostileUnit.getPosition();
            var distanceSquared = position.dst2(hostileUnitPosition);

            if (distanceSquared <= rangeSquared) {
                hostileUnitsInRange.add(hostileUnit);
            }
        }

        return hostileUnitsInRange;
    }

    /**
     * Changes position of this tower and updates its game model
     *
     * @param x X coordinate of new position on the grid
     * @param y Y coordinate of new position on the grid
     */
    public final void rePosition(int x, int y) {
        this.x = x;
        this.y = y;

        if (support != null) {
            var gameModelData = new GameModelData(-1f, new Vector2(x, y));
            support.firePropertyChange(EntityController.UPDATE_EVENT_NAME, null, gameModelData);
        }
    }

    /**
     * Rotates the game model of this tower to face <code>hostileUnit</code>.
     *
     * @param hostileUnit Hostile unit to rotate to
     */
    protected final void rotateToHostileUnit(@NonNull HostileUnit hostileUnit) {
        if (support == null) {
            return;
        }

        float rotation = DuneTDMath.getAngle(hostileUnit.getPosition(), new Vector2(x, y));
        var gameModelData = new GameModelData(rotation, new Vector2(x, y));
        support.firePropertyChange(EntityController.UPDATE_EVENT_NAME, null, gameModelData);
    }

    /**
     * Turns this tower into a debris.
     */
    public final void setToDebris() {
        isDebris = true;
        if (support != null) {
            support.firePropertyChange(EntityController.TO_DEBRIS_EVENT_NAME, null, true);
        }
    }

    /**
     * Removes game model of this tower from scene manager
     */
    public void destroy() {
        if (support != null) {
            support.firePropertyChange(EntityController.DESTROY_EVENT_NAME, null, null);
        }
    }
}
