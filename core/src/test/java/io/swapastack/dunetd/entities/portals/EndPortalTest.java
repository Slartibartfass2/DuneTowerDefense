package io.swapastack.dunetd.entities.portals;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EndPortalTest {

    @Test
    void testConstructor() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var endPortal = new EndPortal(x, y, null);

        assertEquals(x, endPortal.getX());
        assertEquals(y, endPortal.getY());
    }
}