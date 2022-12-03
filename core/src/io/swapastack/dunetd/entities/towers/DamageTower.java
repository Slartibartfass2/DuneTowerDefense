package io.swapastack.dunetd.entities.towers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.game.GameModelData;
import org.jetbrains.annotations.Nullable;

import static io.swapastack.dunetd.assets.controller.EntityController.UPDATE_EVENT_NAME;

/**
 * A damage tower representing a part of the game grid. A damage tower can be: <br>
 * - a guard tower: a tower which deals damage to exactly one hostile unit in range. <br>
 * - a bomb tower: a tower which deals area damage to at least one hostile unit in range. <br>
 * It consists of the same properties as a tower plus the amount of damage it deals to hostile units.
 */
@SuppressWarnings("squid:S2160") // equals only takes uuid
public abstract class DamageTower extends Tower {

    /**
     * Amount of health decreased when this tower attacks a hostile unit
     */
    protected final int damage;

    /**
     * Rotation to rotate to while idling
     */
    private float idleGoalRotation;

    /**
     * Rotation speed for rotating while idling
     */
    private static final float IDLE_ANGULAR_VELOCITY = 20f;

    /**
     * Creates a new damage tower with a specified position, range, build cost, damage and reload time.
     *
     * @param x              X coordinate of position
     * @param y              Y coordinate of position
     * @param range          Range of this damage tower, in which it attacks hostile units
     * @param buildCost      Costs to build this damage tower
     * @param damage         Damage of this damage tower
     * @param reloadTimeInMs Time in milliseconds needed to reload
     */
    protected DamageTower(int x, int y, float range, int buildCost, int damage, int reloadTimeInMs) {
        this(x, y, range, buildCost, damage, reloadTimeInMs, null);
    }

    /**
     * Creates a new damage tower with a specified position, range, build cost, damage and reload time.
     *
     * @param x                X coordinate of position
     * @param y                Y coordinate of position
     * @param range            Range of this damage tower, in which it attacks hostile units
     * @param buildCost        Costs to build this damage tower
     * @param damage           Damage of this damage tower
     * @param reloadTimeInMs   Time in milliseconds needed to reload
     * @param entityController Controller for towers
     */
    protected DamageTower(int x, int y, float range, int buildCost, int damage, int reloadTimeInMs,
                          @Nullable EntityController entityController) {
        super(x, y, range, buildCost, reloadTimeInMs, entityController, MathUtils.random(0, 359));
        this.damage = damage;
        idleGoalRotation = MathUtils.random(0, 359);
    }

    /**
     * Randomly rotates to simulate searching for hostile units, when none are in range.
     *
     * @param deltaTime The time in seconds since the last update
     */
    @Override
    protected void idle(float deltaTime) {
        if (support == null) return;

        // Get rotation by calling tower controller
        var towerController = (EntityController) support.getPropertyChangeListeners()[0];
        var rotation = towerController.getRotation(this);

        // TODO: wirkt noch bisschen seltsam, vllt mit delays dazwischen

        var rotationChange = IDLE_ANGULAR_VELOCITY * deltaTime;

        var oppositeGoal = (idleGoalRotation + 180) % 360;

        if (oppositeGoal >= 180) {
            if (rotation >= oppositeGoal || rotation < idleGoalRotation) {
                rotationChange *= 1f;
            } else {
                rotationChange *= -1f;
            }
        } else {
            if (rotation <= oppositeGoal || rotation > idleGoalRotation) {
                rotationChange *= -1f;
            } else {
                rotationChange *= 1f;
            }
        }

        var newRotation = (rotationChange + rotation + 360) % 360;

        var difference = Math.abs(newRotation - idleGoalRotation);

        if (difference <= 1f || difference >= 359f) {
            rotation = idleGoalRotation;
            idleGoalRotation = MathUtils.random(0, 359);
        } else {
            rotation = newRotation;
        }

        // Update rotation
        support.firePropertyChange(UPDATE_EVENT_NAME, null, new GameModelData(rotation, new Vector2(x, y)));
    }
}