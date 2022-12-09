package io.swapastack.dunetd.entities.portals;

import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.assets.controller.EntityController;
import org.jetbrains.annotations.Nullable;

/**
 * A portal representing a part of the game grid. A portal can be: <br>
 * - a start portal: where hostile units appear <br>
 * - an end portal: where hostile units disappear
 */
public abstract class Portal extends Entity {

    /**
     * Creates a new entity with a specified position.
     *
     * @param x                X coordinate of position
     * @param y                Y coordinate of position
     * @param entityController Controller for entities
     */
    protected Portal(int x, int y, @Nullable EntityController entityController) {
        super(x, y, entityController, 0f);
        if (entityController != null)
            support.firePropertyChange(EntityController.SHOW_EVENT_NAME, null, null);
    }
}
