package io.swapastack.dunetd.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GamePhaseTest {

    @Test
    void testGamePhase() {
        Assertions.assertEquals(4, GamePhase.values().length);
    }
}
