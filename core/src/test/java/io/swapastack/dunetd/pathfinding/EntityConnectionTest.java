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
}
