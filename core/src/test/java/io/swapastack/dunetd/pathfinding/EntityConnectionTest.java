package io.swapastack.dunetd.pathfinding;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityConnectionTest {

    @Test
    void testConstructorWithValidArguments() {
        var entityNodeFrom = new EntityNode(null, 0, 0, 0);
        var entityNodeTo = new EntityNode(null, 0, 0, 0);
        var entityConnection = new EntityConnection(entityNodeFrom, entityNodeTo);
        assertNotNull(entityConnection);
    }

    @Test
    void testConstructorWithInvalidArguments() {
        var entityNodeFrom = new EntityNode(null, 0, 0, 0);
        var entityNodeTo = new EntityNode(null, 0, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> new EntityConnection(null, null));
        assertThrows(IllegalArgumentException.class, () -> new EntityConnection(null, entityNodeTo));
        assertThrows(IllegalArgumentException.class, () -> new EntityConnection(entityNodeFrom, null));
    }

    @Test
    void testGetter() {
        var entityConnection = new EntityConnection(
                new EntityNode(null, 0, 0, 0),
                new EntityNode(null, 0, 0, 0)
        );

        assertTrue(entityConnection.getCost() >= 1);
        assertNotNull(entityConnection.getFromNode());
        assertNotNull(entityConnection.getToNode());
    }
}
