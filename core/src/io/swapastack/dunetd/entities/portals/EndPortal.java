package io.swapastack.dunetd.entities.portals;

import io.swapastack.dunetd.assets.controller.EntityController;

/**
 * An end portal representing a part of the game grid.
 */
public final class EndPortal extends Portal {

    /**
     * Creates a new end portal with a specified position.
     *
     * @param x                X coordinate of position
     * @param y                Y coordinate of position
     * @param entityController Controller for entities
     */
    public EndPortal(int x, int y, EntityController entityController) {
        super(x, y, entityController);
    }
}