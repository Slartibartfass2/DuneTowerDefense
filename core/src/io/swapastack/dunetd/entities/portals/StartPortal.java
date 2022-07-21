package io.swapastack.dunetd.entities.portals;

import io.swapastack.dunetd.game.EntityController;
import lombok.NonNull;

/**
 * A start portal representing a part of the game grid.
 */
public final class StartPortal extends Portal {

    /**
     * Creates a new start portal with a specified position.
     *
     * @param x X coordinate of position
     * @param y Y coordinate of position
     */
    public StartPortal(int x, int y) {
        super(x, y);
    }

    /**
     * Creates a new start portal with a specified position.
     *
     * @param x                X coordinate of position
     * @param y                Y coordinate of position
     * @param entityController Controller for entities
     */
    public StartPortal(int x, int y, @NonNull EntityController entityController) {
        super(x, y, entityController);
    }
}