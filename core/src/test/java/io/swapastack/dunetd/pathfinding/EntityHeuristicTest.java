package io.swapastack.dunetd.pathfinding;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntityHeuristicTest {

    @Test
    void testEstimateWithValidArguments() {
        var entityHeuristic = new EntityHeuristic();

        var entityNode1 = new EntityNode(null, 0, 0, 0);
        var entityNode2 = new EntityNode(null, 1, 0, 0);

        assertEquals(1f, entityHeuristic.estimate(entityNode1, entityNode2), 0f);

        int x = new Random().nextInt(1000);
        int y = new Random().nextInt(1000);

        var entityNode3 = new EntityNode(null, 0, 0, 0);
        var entityNode4 = new EntityNode(null, x, y, 0);

        assertEquals(x * x + y * y, entityHeuristic.estimate(entityNode3, entityNode4), 0f);
    }

    @Test
    void testEstimateWithInvalidArguments() {
        var entityHeuristic = new EntityHeuristic();

        var entityNode1 = new EntityNode(null, 0, 0, 0);
        var entityNode2 = new EntityNode(null, 0, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> entityHeuristic.estimate(null, null));
        assertThrows(IllegalArgumentException.class, () -> entityHeuristic.estimate(null, entityNode2));
        assertThrows(IllegalArgumentException.class, () -> entityHeuristic.estimate(entityNode1, null));
    }
}
