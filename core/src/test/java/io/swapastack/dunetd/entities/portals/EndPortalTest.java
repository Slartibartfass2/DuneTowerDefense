package io.swapastack.dunetd.entities.portals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

class EndPortalTest {

    @Test
    void testConstructor() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();

        var endPortal = new EndPortal(x, y, null);

        Assertions.assertEquals(x, endPortal.getX());
        Assertions.assertEquals(y, endPortal.getY());
    }
}
