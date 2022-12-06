package io.swapastack.dunetd.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GamePhaseTest {

    @Test
    void testGamePhase() {
        assertEquals(4, GamePhase.values().length);
    }
}