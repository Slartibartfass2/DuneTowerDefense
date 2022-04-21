package io.swapastack.dunetd.entities.towers;

import io.swapastack.dunetd.game.EntityController;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a selection of the towers the user can build in the game.
 * @see GuardTower
 * @see BombTower
 * @see SoundTower
 */
public enum TowerEnum {
    GUARD_TOWER,
    BOMB_TOWER,
    SOUND_TOWER;
    
    /**
     * Returns the corresponding tower enum to the specified tower.
     *
     * @param tower Tower from which the corresponding tower enum is returned
     * @return Corresponding tower enum to the specified tower
     */
    public static TowerEnum fromTower(@NonNull Tower tower) {
        if (tower instanceof GuardTower) return GUARD_TOWER;
        if (tower instanceof BombTower) return BOMB_TOWER;
        if (tower instanceof SoundTower) return SOUND_TOWER;
        throw new IllegalStateException("Unexpected tower: " + tower);
    }
    
    /**
     * Creates a tower from tower enum.
     *
     * @param x                X position of tower in grid
     * @param y                Y position of tower in grid
     * @param entityController Controller for towers
     * @return The tower object created from the tower enum
     */
    @NotNull
    public Tower toTower(int x, int y, EntityController entityController) {
        switch (this) {
            case GUARD_TOWER -> {
                if (entityController == null) return new GuardTower(x, y);
                return new GuardTower(x, y, entityController);
            }
            case BOMB_TOWER -> {
                if (entityController == null) return new BombTower(x, y);
                return new BombTower(x, y, entityController);
            }
            case SOUND_TOWER -> {
                if (entityController == null) return new SoundTower(x, y);
                return new SoundTower(x, y, entityController);
            }
            default -> throw new IllegalStateException("Unexpected value: " + this);
        }
    }
    
    /**
     * Returns next tower enum.
     *
     * @return Next tower enum
     */
    public TowerEnum next() {
        return switch (this) {
            case GUARD_TOWER -> BOMB_TOWER;
            case BOMB_TOWER -> SOUND_TOWER;
            case SOUND_TOWER -> GUARD_TOWER;
        };
    }
    
    /**
     * Returns previous tower enum.
     *
     * @return Previous tower enum
     */
    public TowerEnum previous() {
        return switch (this) {
            case GUARD_TOWER -> SOUND_TOWER;
            case BOMB_TOWER -> GUARD_TOWER;
            case SOUND_TOWER -> BOMB_TOWER;
        };
    }
}
