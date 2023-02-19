package io.swapastack.dunetd.hostileunits;

import io.swapastack.dunetd.assets.controller.HostileUnitController;
import io.swapastack.dunetd.game.CardinalDirection;
import io.swapastack.dunetd.game.GameModelData;
import io.swapastack.dunetd.pathfinding.Path;
import io.swapastack.dunetd.vectors.Vector2;

import java.beans.PropertyChangeSupport;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * A hostile unit representing the enemy in this game. It moves along a path on the grid until it reaches the end
 * portal to decrease the health of the player. A hostile unit can be:<br>
 * - an infantry: weak but fast ground unit <br>
 * - a harvester: slow air unit, which has a resistance against the sound towers slowing effect <br>
 * - a boss unit: strong but slow ground unit. <br>
 * A hostile unit consists of a position, speed, health, slowing effect variables and a cardinal direction.
 */
@EqualsAndHashCode
public abstract class HostileUnit {

    /**
     * Position of this hostile unit
     */
    @Getter
    private Vector2 position;

    /**
     * Distance that this hostile unit can cover in one second
     */
    @Getter
    private final float speed;

    /**
     * Health of this hostile unit (if <= 0, the hostile unit is dead)
     */
    @Getter
    private int health;

    /**
     * Current speed of hostile unit (can be influenced by a sound tower)
     */
    @Getter
    private float currentSpeed;

    /**
     * Duration how long the slowing effect will last
     */
    private int slowingEffectDurationInMilliseconds;

    /**
     * Unique identifier for storing this hostile unit in a map
     */
    @Getter
    private final UUID uuid;

    /**
     * Cardinal direction of this hostile unit
     */
    private CardinalDirection cardinalDirection;

    /**
     * Property change support to update game model of this hostile unit
     */
    private final PropertyChangeSupport support;

    /**
     * Creates a new hostile unit with a specified position, speed and health.
     *
     * @param position              Position of this hostile unit
     * @param speed                 Distance that this hostile unit can cover in one second
     * @param health                Health of this hostile unit (if <= 0, the hostile unit is dead)
     * @param hostileUnitController Controller for hostile units
     */
    protected HostileUnit(@NonNull Vector2 position, float speed, int health,
                          @Nullable HostileUnitController hostileUnitController) {
        uuid = UUID.randomUUID();
        this.position = position;
        this.speed = speed;
        this.health = health;
        currentSpeed = speed;
        slowingEffectDurationInMilliseconds = 0;
        cardinalDirection = CardinalDirection.NORTH;

        if (hostileUnitController != null) {
            support = new PropertyChangeSupport(this);

            // Add hostile unit controller as observer and call create event
            support.addPropertyChangeListener(hostileUnitController);
            support.firePropertyChange(HostileUnitController.CREATE_EVENT_NAME, null,
                    new GameModelData(cardinalDirection.getDegrees(), position));
        } else {
            support = null;
        }
    }

    /**
     * Adds game model of this hostile unit to the scene manager.
     */
    public final void show() {
        if (support != null) {
            support.firePropertyChange(HostileUnitController.SHOW_EVENT_NAME, null, null);
        }
    }

    /**
     * Moves this hostile unit on the grid along the specified path and updates its game model accordingly. Also
     * updates the slowing effect, if there is one.
     *
     * @param path                    Path on which the hostile unit is moving
     * @param deltaTimeInMilliseconds The time in milliseconds since the last update
     * @throws IllegalStateException If the hostile unit is not on the path
     */
    public final void move(@NonNull Path path, float deltaTimeInMilliseconds) throws IllegalStateException {
        // If hostile unit is already slowed down, decrease duration, otherwise reset speed to maximum
        if (slowingEffectDurationInMilliseconds > 0) {
            slowingEffectDurationInMilliseconds -= deltaTimeInMilliseconds;
        } else {
            slowingEffectDurationInMilliseconds = 0;
            currentSpeed = speed;
        }

        var positionTmp = position;

        // Distance which the hostile unit can go in this iteration
        var maxMoveDistance = currentSpeed * deltaTimeInMilliseconds;

        // Move hostile unit as long as moveDistance > 0
        do {
            var nextWaypoint = path.getNextWaypoint(positionTmp);

            // If next waypoint is null, the position of the hostile unit is not on the path
            if (nextWaypoint == null) {
                throw new IllegalStateException("Hostile unit must be on the path");
            }

            // If hostile unit reached the end of the path stop moving
            if (nextWaypoint.equals(positionTmp)) {
                position = positionTmp;
                return;
            }

            var distanceToNextWaypoint = positionTmp.getDistance(nextWaypoint);

            // Get normalized direction vector to set orientation of game model
            var direction = Vector2.subtract(nextWaypoint, positionTmp).normalize();
            cardinalDirection = CardinalDirection.fromDirection(direction);

            // Move either to next waypoint or as long as possible
            var moveDistance = Math.min(maxMoveDistance, distanceToNextWaypoint);
            var moveVector = Vector2.multiply(direction, moveDistance);
            positionTmp = Vector2.add(positionTmp, moveVector);
            maxMoveDistance -= moveDistance;
        } while (maxMoveDistance > 0f);

        // Set position
        position = positionTmp;

        // Update game model if existing
        if (support != null) {
            support.firePropertyChange(HostileUnitController.UPDATE_EVENT_NAME, deltaTimeInMilliseconds,
                    new GameModelData(cardinalDirection.getDegrees(), positionTmp));
        }
    }

    /**
     * Decreases the <code>health</code> of this hostile unit by the specified <code>damage</code>.
     *
     * @param damage Amount of health to subtract from this hostile units <code>health</code>
     * @throws IllegalArgumentException If the damage is less than zero
     */
    public final void dealDamage(int damage) throws IllegalArgumentException {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage must be greater than or equal to zero");
        }

        if (isDead()) {
            return;
        }

        health -= damage;

        if (health <= 0) {
            kill();
        }
    }

    /**
     * Slows down this hostile unit by decreasing the speed to the value of speed * slowingEffectMultiplier. The effect
     * lasts as long as the specified appliedSlowingEffectDurationInMilliseconds.
     *
     * @param slowingEffectMultiplier                    Value to multiply with speed to set the new speed
     * @param appliedSlowingEffectDurationInMilliseconds Duration of slowing effect in milliseconds.
     */
    public final void slowDown(float slowingEffectMultiplier, int appliedSlowingEffectDurationInMilliseconds) {
        currentSpeed = speed * getSlowingEffect(slowingEffectMultiplier);
        slowingEffectDurationInMilliseconds = appliedSlowingEffectDurationInMilliseconds;
    }

    /**
     * Sets <code>health</code> of this hostile unit to zero and removes its game model.
     */
    public final void kill() {
        health = 0;
        // Remove HostileUnit from grid
        if (support != null) {
            support.firePropertyChange(HostileUnitController.DESTROY_EVENT_NAME, null, null);
        }
    }

    /**
     * Returns if this hostile unit is dead (<code>health</code> <= 0).
     *
     * @return True if hostile unit is dead, false otherwise.
     */
    public final boolean isDead() {
        return health <= 0;
    }

    /**
     * Returns the spice reward for killing this hostile unit.
     *
     * @return Spice reward for killing this hostile unit
     */
    public abstract int getSpiceReward();

    protected float getSlowingEffect(float slowingEffectMultiplier) {
        return slowingEffectMultiplier;
    }
}
