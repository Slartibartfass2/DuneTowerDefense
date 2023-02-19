package io.swapastack.dunetd.pathfinding;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EntityHeuristicTest {

    @Test
    void testEstimateWithValidArguments() {
        var entityHeuristic = new EntityHeuristic();

        var entityNode1 = new EntityNode(null, 0, 0, 0);
        var entityNode2 = new EntityNode(null, 1, 0, 0);

        Assertions.assertEquals(1f, entityHeuristic.estimate(entityNode1, entityNode2), 0f);

        int x = new Random().nextInt(1000);
        int y = new Random().nextInt(1000);

        var entityNode3 = new EntityNode(null, 0, 0, 0);
        var entityNode4 = new EntityNode(null, x, y, 0);

        Assertions.assertEquals(x * x + y * y, entityHeuristic.estimate(entityNode3, entityNode4), 0f);
    }
}
