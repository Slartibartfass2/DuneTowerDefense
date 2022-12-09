package io.swapastack.dunetd.pathfinding;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EntityConnectionTest {

    @Test
    void testConstructorWithValidArguments() {
        var entityNodeFrom = new EntityNode(null, 0, 0, 0);
        var entityNodeTo = new EntityNode(null, 0, 0, 0);
        var entityConnection = new EntityConnection(entityNodeFrom, entityNodeTo);
        Assertions.assertNotNull(entityConnection);
    }

    @Test
    void testConstructorWithInvalidArguments() {
        var entityNodeFrom = new EntityNode(null, 0, 0, 0);
        var entityNodeTo = new EntityNode(null, 0, 0, 0);

        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityConnection(null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityConnection(null, entityNodeTo));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityConnection(entityNodeFrom, null));
    }

    @Test
    void testGetter() {
        var entityConnection = new EntityConnection(
                new EntityNode(null, 0, 0, 0),
                new EntityNode(null, 0, 0, 0)
        );

        Assertions.assertTrue(entityConnection.getCost() >= 1);
        Assertions.assertNotNull(entityConnection.getFromNode());
        Assertions.assertNotNull(entityConnection.getToNode());
    }
}
