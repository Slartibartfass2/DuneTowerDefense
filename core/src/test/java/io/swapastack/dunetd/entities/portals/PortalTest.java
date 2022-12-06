package io.swapastack.dunetd.entities.portals;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PortalTest {

    @Test
    void testConstructor() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var portal = new Portal(x, y, null) {
        };

        assertEquals(x, portal.getX());
        assertEquals(y, portal.getY());
    }
}
