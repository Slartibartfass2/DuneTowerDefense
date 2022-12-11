package io.swapastack.dunetd.game;

import io.swapastack.dunetd.vectors.Vector2;

import lombok.NonNull;

/**
 * Wrapper class for data used to update the game models of entities, hostile units or the shai hulud.
 */
public record GameModelData(float rotation, @NonNull Vector2 position) {
}
