package io.swapastack.dunetd.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.game.GameModelData;

import java.beans.PropertyChangeSupport;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * An entity representing a part of the game grid. An entity can be: <br>
 * - a portal: start or end portal, where hostile units appear or disappear <br>
 * - a tower: attacking hostile units in range <br>
 * An entity consists of a position.
 */
@EqualsAndHashCode
public abstract class Entity {

    @Getter
    protected int x;

    @Getter
    protected int y;

    /**
     * Property change support to update game model of this entity
     */
    protected final PropertyChangeSupport support;

    /**
     * Unique identifier for storing this tower in a map
     */
    @Getter
    private final UUID uuid;

    /**
     * Creates a new entity with a specified position.
     *
     * @param x X coordinate of position
     * @param y Y coordinate of position
     */
    protected Entity(int x, int y) {
        this(x, y, null, 0f);
    }

    /**
     * Creates a new entity with a specified position.
     *
     * @param x                X coordinate of position
     * @param y                Y coordinate of position
     * @param entityController Controller for entities
     * @param startRotation    Start rotation for game model of this entity
     */
    protected Entity(int x, int y, @Nullable EntityController entityController, float startRotation) {
        uuid = UUID.randomUUID();
        this.x = x;
        this.y = y;

        if (entityController != null) {
            support = new PropertyChangeSupport(this);

            // Add entity controller as observer and call create event
            support.addPropertyChangeListener(entityController);
            var gameModelData = new GameModelData(startRotation, new Vector2(x, y));
            support.firePropertyChange(EntityController.CREATE_EVENT_NAME, null, gameModelData);
        } else {
            support = null;
        }
    }

    /**
     * Returns the entities position as <code>Vector3</code> object with the coordinates (x, 0, y).
     *
     * @return The entities position as <code>Vector3</code> object
     */
    @NotNull
    public final Vector3 getGridPosition3d() {
        return new Vector3(x, 0f, y);
    }

    /**
     * Returns the entities position as <code>Vector2</code> object with the coordinates (x, y).
     *
     * @return The entities position as <code>Vector2</code> object
     */
    @NotNull
    public final Vector2 getGridPosition2d() {
        return new Vector2(x, y);
    }
}
