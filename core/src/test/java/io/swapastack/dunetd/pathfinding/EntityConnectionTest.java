package io.swapastack.dunetd.pathfinding;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntityConnectionTest {

    @Test
    public void testConstructorWithValidArguments() {
        var entityNodeFrom = new EntityNode(null, 0, 0, 0);
        var entityNodeTo = new EntityNode(null, 0, 0, 0);
        var entityConnection = new EntityConnection(entityNodeFrom, entityNodeTo);
        assertNotNull(entityConnection);
    }

    @Test
    public void testConstructorWithInvalidArguments() {
        var entityNodeFrom = new EntityNode(null, 0, 0, 0);
        var entityNodeTo = new EntityNode(null, 0, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> new EntityConnection(null, null));
        assertThrows(IllegalArgumentException.class, () -> new EntityConnection(null, entityNodeTo));
        assertThrows(IllegalArgumentException.class, () -> new EntityConnection(entityNodeFrom, null));
    }

    @Test
    public void testGetter() {
        var entityConnection = new EntityConnection(
                new EntityNode(null, 0, 0, 0),
                new EntityNode(null, 0, 0, 0)
        );

        assertTrue(entityConnection.getCost() >= 1);
        assertNotNull(entityConnection.getFromNode());
        assertNotNull(entityConnection.getToNode());
    }
}