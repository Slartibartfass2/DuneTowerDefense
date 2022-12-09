package io.swapastack.dunetd.entities.portals;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StartPortalTest {

    @Test
    void testConstructor() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var startPortal = new StartPortal(x, y, null);

        Assertions.assertEquals(x, startPortal.getX());
        Assertions.assertEquals(y, startPortal.getY());
    }
}
