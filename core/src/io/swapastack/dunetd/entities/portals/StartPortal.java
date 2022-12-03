package io.swapastack.dunetd.entities.portals;

import io.swapastack.dunetd.assets.controller.EntityController;

/**
 * A start portal representing a part of the game grid.
 */
public final class StartPortal extends Portal {

    /**
     * Creates a new start portal with a specified position.
     *
     * @param x                X coordinate of position
     * @param y                Y coordinate of position
     * @param entityController Controller for entities
     */
    public StartPortal(int x, int y, EntityController entityController) {
        super(x, y, entityController);
    }
}