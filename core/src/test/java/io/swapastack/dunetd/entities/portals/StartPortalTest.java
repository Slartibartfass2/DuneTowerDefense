package io.swapastack.dunetd.entities.portals;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class StartPortalTest {
    
    @Test
    public void testConstructor() {
        int x = new Random().nextInt();
        int y = new Random().nextInt();
        
        var startPortal = new StartPortal(x, y);
        
        assertEquals(x, startPortal.getX());
        assertEquals(y, startPortal.getY());
    }
}