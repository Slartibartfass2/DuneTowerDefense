package io.swapastack.dunetd.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.GuardTower;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EntityGraphTest {

    static {
        TestHelper.readConfigFile();
    }

    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");

    @Test
    void testConstructorWithValidArguments() {
        for (int width = 1; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 1; height < MAX_GRID_HEIGHT; height++) {
                var entityGraph = new EntityGraph(new Entity[width][height]);
                Assertions.assertNotNull(entityGraph);
            }
        }
    }

    @Test
    void testConstructorWithInvalidArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityGraph(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityGraph(new Entity[0][0]));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityGraph(new Entity[0][1]));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new EntityGraph(new Entity[1][0]));
    }

    @Test
    void testFindPathWithValidArguments() {
        var entityGraph = new EntityGraph(new Entity[1][1]);
        Assertions.assertNotNull(entityGraph.findPath(Vector2.Zero, Vector2.Zero));

        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var goalPosition = new Vector2(width - 1, height - 1);

                // path blocked
                var grid = getEntityGrid(width, height, new Vector2(0f, 1f), new Vector2(1f, 0f));
                entityGraph = new EntityGraph(grid);
                var path = entityGraph.findPath(Vector2.Zero, goalPosition);
                Assertions.assertNotNull(path);
                Assertions.assertEquals(0, path.getCount());

                // path existing
                grid = getEntityGrid(width, height);
                entityGraph = new EntityGraph(grid);
                path = entityGraph.findPath(Vector2.Zero, goalPosition);
                Assertions.assertNotNull(path);
                Assertions.assertEquals(width + height - 1, path.getCount());
            }
        }
    }

    @Test
    void testFindPathWithInvalidArguments() {
        var entityGraph = new EntityGraph(new Entity[1][1]);
        var positionOutsideGrid = new Vector2(2, 0);

        Assertions.assertThrows(IllegalArgumentException.class, () -> entityGraph.findPath(null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> entityGraph.findPath(Vector2.Zero, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> entityGraph.findPath(null, Vector2.Zero));
        Assertions.assertThrows(IllegalArgumentException.class, () -> entityGraph.findPath(positionOutsideGrid, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> entityGraph.findPath(null, positionOutsideGrid));
        Assertions.assertThrows(IllegalArgumentException.class, () -> entityGraph.findPath(positionOutsideGrid,
                positionOutsideGrid));
    }

    @Test
    void testGetIndexWithValidArguments() {
        var entityGraph = new EntityGraph(new Entity[1][1]);
        Assertions.assertEquals(0, entityGraph.getIndex(new EntityNode(null, 0, 0, 0)));
    }

    @Test
    void testGetIndexWithInvalidArguments() {
        var entityGraph = new EntityGraph(new Entity[1][1]);
        Assertions.assertThrows(IllegalArgumentException.class, () -> entityGraph.getIndex(null));
    }

    @Test
    void testGetNodeCount() throws NoSuchFieldException, IllegalAccessException {
        var entityGraph = new EntityGraph(new Entity[1][1]);

        var field = EntityGraph.class.getDeclaredField("entityNodes");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        var entityNodes = (Array<EntityNode>) field.get(entityGraph);

        Assertions.assertEquals(entityNodes.size, entityGraph.getNodeCount());
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
    void testGetConnectionsWithInvalidArguments() {
        var entityGraph = new EntityGraph(new Entity[2][2]);
        Assertions.assertThrows(IllegalArgumentException.class, () -> entityGraph.getConnections(null));
    }

    Entity[][] getEntityGrid(int width, int height, Vector2... towerPositions) {
        var grid = new Entity[width][height];
        for (var towerPosition : towerPositions) {
            int x = (int) towerPosition.x;
            int y = (int) towerPosition.y;

            grid[x][y] = new GuardTower(x, y);
        }
        return grid;
    }
}
