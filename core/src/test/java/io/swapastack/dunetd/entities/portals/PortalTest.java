package io.swapastack.dunetd.entities.portals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

class PortalTest {

    @Test
    void testConstructor() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var portal = new Portal(x, y, null) {
        };

        Assertions.assertEquals(x, portal.getX());
        Assertions.assertEquals(y, portal.getY());
    }
}
