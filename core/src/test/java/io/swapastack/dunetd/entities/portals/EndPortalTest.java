package io.swapastack.dunetd.entities.portals;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class EndPortalTest {

    @Test
    public void testConstructor() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var endPortal = new EndPortal(x, y);

        assertEquals(x, endPortal.getX());
        assertEquals(y, endPortal.getY());
    }
}