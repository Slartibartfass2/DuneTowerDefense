package io.swapastack.dunetd.game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GamePhaseTest {

    @Test
    public void testGamePhase() {
        assertEquals(4, GamePhase.values().length);
    }
}