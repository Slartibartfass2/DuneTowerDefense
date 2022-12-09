package io.swapastack.dunetd.hostileunits;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.assets.controller.HostileUnitController;
import io.swapastack.dunetd.game.CardinalDirection;
import io.swapastack.dunetd.game.GameModelData;
import io.swapastack.dunetd.pathfinding.Path;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeSupport;
import java.util.UUID;

import static io.swapastack.dunetd.assets.controller.HostileUnitController.CREATE_EVENT_NAME;
import static io.swapastack.dunetd.assets.controller.HostileUnitController.DESTROY_EVENT_NAME;
import static io.swapastack.dunetd.assets.controller.HostileUnitController.SHOW_EVENT_NAME;
import static io.swapastack.dunetd.assets.controller.HostileUnitController.UPDATE_EVENT_NAME;
import static io.swapastack.dunetd.game.CardinalDirection.NORTH;
import static io.swapastack.dunetd.game.CardinalDirection.fromDirection;

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
     * Unique identifier for storing this hostile unit in a map
     */
    @Getter
    private final UUID uuid;

    /**
     * Position of this hostile unit
     */
    protected Vector2 position;

    /**
     * Distance that this hostile unit can cover in one second
     */
    protected final float speed;

    /**
     * Health of this hostile unit (if <= 0, the hostile unit is dead)
     */
    @Getter
    protected int health;

    /**
     * Current speed of hostile unit (can be influenced by a sound tower)
     */
    protected float currentSpeed;

    /**
     * Duration how long the slowing effect will last
     */
    protected int slowingEffectDurationInMs;

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
        this.position = position.cpy();
        this.speed = speed;
        this.health = health;
        currentSpeed = speed;
        slowingEffectDurationInMs = 0;
        cardinalDirection = NORTH;

        if (hostileUnitController != null) {
            support = new PropertyChangeSupport(this);

            // Add hostile unit controller as observer and call create event
            support.addPropertyChangeListener(hostileUnitController);
            support.firePropertyChange(CREATE_EVENT_NAME, null,
                    new GameModelData(cardinalDirection.getDegrees(), position.cpy()));
        } else {
            support = null;
        }
    }

    /**
     * Adds game model of this hostile unit to the scene manager.
     */
    public final void show() {
        if (support != null) {
            support.firePropertyChange(SHOW_EVENT_NAME, null, null);
        }
    }

    /**
     * Moves this hostile unit on the grid along the specified path and updates its game model accordingly. Also
     * updates the slowing effect, if there is one.
     *
     * @param path      Path on which the hostile unit is moving
     * @param deltaTime The time in seconds since the last update
     * @throws IllegalStateException If the hostile unit is not on the path
     */
    public final void move(@NonNull Path path, float deltaTime) throws IllegalStateException {
        // If hostile unit is already slowed down, decrease duration, otherwise reset speed to maximum
        if (slowingEffectDurationInMs > 0) {
            slowingEffectDurationInMs -= deltaTime * 1000;
        } else {
            slowingEffectDurationInMs = 0;
            currentSpeed = speed;
        }

        var positionTmp = position.cpy();

        // Distance which the hostile unit can go in this iteration
        var maxMoveDistance = currentSpeed * deltaTime;

        // Move hostile unit as long as moveDistance > 0
        do {
            var nextWaypoint = path.getNextWaypoint(positionTmp);

            // If next waypoint is null, the position of the hostile unit is not on the path
            if (nextWaypoint == null) {
                throw new IllegalStateException("Hostile unit must be on the path");
            }

            // If hostile unit reached the end of the path
            if (nextWaypoint.equals(positionTmp)) {
                // Set new position and stop moving
                position = positionTmp;
                return;
            }

            var distanceToNextWaypoint = positionTmp.dst(nextWaypoint);

            // Get normalized direction vector to set orientation of game model
            var direction = nextWaypoint.cpy().sub(positionTmp).nor();
            cardinalDirection = fromDirection(direction);

            // Move either to next waypoint or as long as possible
            var moveDistance = Math.min(maxMoveDistance, distanceToNextWaypoint);
            positionTmp.add(direction.scl(moveDistance));
            maxMoveDistance -= moveDistance;
        } while (maxMoveDistance > 0f);

        // Set new position
        position = positionTmp;

        // Update game model if existing
        if (support != null) {
            support.firePropertyChange(UPDATE_EVENT_NAME, deltaTime,
                    new GameModelData(cardinalDirection.getDegrees(), positionTmp.cpy()));
        }
    }

    /**
     * Decreases the <code>health</code> of this hostile unit by the specified <code>damage</code>.
     *
     * @param damage Amount of health to subtract from this hostile units <code>health</code>
     * @throws IllegalArgumentException If the damage is less than or equal to zero
     */
    public final void dealDamage(int damage) throws IllegalArgumentException {
        if (damage <= 0) {
            throw new IllegalArgumentException("Damage must be greater than zero");
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
     * lasts as long as the specified slowingEffectDurationInMs.
     *
     * @param slowingEffectMultiplier   Value to multiply with speed to set the new speed
     * @param slowingEffectDurationInMs Duration of slowing effect.
     */
    public abstract void slowDown(float slowingEffectMultiplier, int slowingEffectDurationInMs);

    /**
     * Sets <code>health</code> of this hostile unit to zero and removes its game model.
     */
    public final void kill() {
        health = 0;
        // Remove HostileUnit from grid
        if (support != null) {
            support.firePropertyChange(DESTROY_EVENT_NAME, null, null);
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
     * Returns a copy of the position of this hostile unit.
     *
     * @return Copy of the position of this hostile unit
     */
    public final Vector2 getPosition() {
        return position.cpy();
    }

    /**
     * Returns the spice reward for killing this hostile unit.
     *
     * @return Spice reward for killing this hostile unit
     */
    public abstract int getSpiceReward();
}
