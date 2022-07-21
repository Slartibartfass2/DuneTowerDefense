package io.swapastack.dunetd.game;

/**
 * Represents the different game phases:<br>
 * - BUILD_PHASE: The user can build/tear down/move towers.<br>
 * - WAVE_PHASE: The user can't build/tear down/move towers. Hostile units appear from the start portal and move
 * towards the end portal along a path, the towers attack them if they're in range. The user can summon the Shai Hulud.<br>
 * - GAME_LOST_PHASE: The player health is less than or equals to zero. The player can return to the main menu to
 * start a new game.<br>
 * - GAME_WON_PHASE: All wave phases were completed. The player can return to the main menu to start a new game.
 */
public enum GamePhase {
    BUILD_PHASE,
    WAVE_PHASE,
    GAME_LOST_PHASE,
    GAME_WON_PHASE
}