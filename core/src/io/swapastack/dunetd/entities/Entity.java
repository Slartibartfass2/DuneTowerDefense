package io.swapastack.dunetd.entities;

import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.game.GameModelData;
import io.swapastack.dunetd.vectors.Vector2;

import java.beans.PropertyChangeSupport;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * An entity representing a part of the game grid. An entity can be: <br>
 * - a portal: start or end portal, where hostile units appear or disappear <br>
 * - a tower: attacking hostile units in range <br>
 * An entity consists of a position.
 */
@EqualsAndHashCode
public abstract class Entity {

    /**
     * Property change support to update game model of this entity
     */
    protected final PropertyChangeSupport support;

    /**
     * Position of this entity
     */
    @Getter
    private final Vector2 position;

    /**
     * Unique identifier for storing this entity in a map
     */
    @Getter
    private final UUID uuid;

    /**
     * Creates a new entity with a specified position.
     *
     * @param position position of this entity
     */
    protected Entity(@NonNull Vector2 position) {
        this(position, null, 0f);
    }

    /**
     * Creates a new entity with a specified position.
     *
     * @param position         position of this entity
     * @param entityController Controller for entities
     * @param startRotation    Start rotation for game model of this entity
     */
    protected Entity(@NonNull Vector2 position, @Nullable EntityController entityController, float startRotation) {
        uuid = UUID.randomUUID();
        this.position = position;

        if (entityController != null) {
            support = new PropertyChangeSupport(this);

            // Add entity controller as observer and call create event
            support.addPropertyChangeListener(entityController);
            var gameModelData = new GameModelData(startRotation, position);
            support.firePropertyChange(EntityController.CREATE_EVENT_NAME, null, gameModelData);
        } else {
            support = null;
        }
    }
}
