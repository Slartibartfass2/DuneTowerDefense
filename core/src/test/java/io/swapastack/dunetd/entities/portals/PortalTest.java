package io.swapastack.dunetd.entities.portals;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class PortalTest {

    @Test
    public void testConstructor() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var portal = new Portal(x, y) {
        };

        assertEquals(x, portal.getX());
        assertEquals(y, portal.getY());
    }
}