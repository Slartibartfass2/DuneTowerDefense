package io.swapastack.dunetd.entities.portals;

import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.vectors.Vector2;

import org.jetbrains.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * A portal representing a part of the game grid. A portal can be: <br>
 * - a start portal: where hostile units appear <br>
 * - an end portal: where hostile units disappear
 */
@EqualsAndHashCode(callSuper = true)
public final class Portal extends Entity {

    @Getter
    private final PortalType type;

    /**
     * Creates a new entity with a specified position.
     *
     * @param position         Position of this portal
     * @param type             Type of this portal
     * @param entityController Controller for entities
     */
    public Portal(@NonNull Vector2 position, PortalType type, @Nullable EntityController entityController) {
        super(position, entityController, 0f);
        this.type = type;
        if (entityController != null) {
            support.firePropertyChange(EntityController.SHOW_EVENT_NAME, null, null);
        }
    }
}
