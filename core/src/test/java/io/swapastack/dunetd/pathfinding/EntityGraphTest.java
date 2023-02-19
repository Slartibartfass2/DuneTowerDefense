package io.swapastack.dunetd.pathfinding;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.GuardTower;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EntityGraphTest {

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testConstructorWithInvalidArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityGraph(new Entity[0][0]));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityGraph(new Entity[0][1]));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityGraph(new Entity[1][0]));
    }

    @Test
    void testFindPathWithValidArguments() {
        var entityGraph = new EntityGraph(new Entity[1][1]);
        Assertions.assertNotNull(entityGraph.findPath(Vector2.ZERO, Vector2.ZERO));

        var width = 10;
        var height = 10;
        var goalPosition = new Vector2(width - 1, height - 1);

        // path blocked
        var grid = getEntityGrid(width, height, new Vector2(0f, 1f), new Vector2(1f, 0f));
        entityGraph = new EntityGraph(grid);
        var path = entityGraph.findPath(Vector2.ZERO, goalPosition);
        Assertions.assertNotNull(path);
        Assertions.assertEquals(0, path.getCount());

        // path existing
        grid = getEntityGrid(width, height);
        entityGraph = new EntityGraph(grid);
        path = entityGraph.findPath(Vector2.ZERO, goalPosition);
        Assertions.assertNotNull(path);
        Assertions.assertEquals(width + height - 1, path.getCount());
    }

    @Test
    void testFindPathWithPositionsOutsideTheGrid() {
        var entityGraph = new EntityGraph(new Entity[1][1]);
        var positionOutsideGrid = new Vector2(2, 0);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> entityGraph.findPath(positionOutsideGrid, Vector2.ZERO));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> entityGraph.findPath(Vector2.ZERO, positionOutsideGrid));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> entityGraph.findPath(positionOutsideGrid, positionOutsideGrid));
    }

    @Test
    void testGetIndexWithValidArguments() {
        var entityGraph = new EntityGraph(new Entity[1][1]);
        Assertions.assertEquals(0, entityGraph.getIndex(new EntityNode(null, 0, 0, 0)));
    }

    @Test
    void testGetNodeCount() {
        var entityGraph = new EntityGraph(new Entity[1][1]);
        Assertions.assertEquals(1, entityGraph.getNodeCount());
    }

    @Test
    void testGetConnectionsWithValidArguments() {
        var entityGraph = new EntityGraph(new Entity[2][2]);
        var connections = entityGraph.getConnections(new EntityNode(null, 0, 0, 0));
        Assertions.assertNotNull(connections);
        Assertions.assertEquals(2, connections.size);

        entityGraph = new EntityGraph(getEntityGrid(2, 2, new Vector2(0f, 1f)));
        connections = entityGraph.getConnections(new EntityNode(null, 0, 0, 0));
        Assertions.assertNotNull(connections);
        Assertions.assertEquals(1, connections.size);
    }

    @Test
    void testGetConnectionsWithoutAnyConnections() {
        var entityGraph = new EntityGraph(new Entity[2][2]);
        var connections = entityGraph.getConnections(new EntityNode(null, 3, 3, 0));
        Assertions.assertNotNull(connections);
        Assertions.assertEquals(0, connections.size);
    }

    Entity[][] getEntityGrid(int width, int height, Vector2... towerPositions) {
        var grid = new Entity[width][height];
        for (var towerPosition : towerPositions) {
            grid[(int) towerPosition.x()][(int) towerPosition.y()] = new GuardTower(towerPosition, null);
        }
        return grid;
    }
}
