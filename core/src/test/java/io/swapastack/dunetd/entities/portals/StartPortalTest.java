package io.swapastack.dunetd.entities.portals;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StartPortalTest {

    @Test
    void testConstructor() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var startPortal = new StartPortal(x, y, null);

        assertEquals(x, startPortal.getX());
        assertEquals(y, startPortal.getY());
    }
}
