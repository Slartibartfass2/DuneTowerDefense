package io.swapastack.dunetd.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.Tower;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A graph for the {@link IndexedAStarPathFinder}.
 * Inspired by <a href="https://happycoding.io/tutorials/libgdx/pathfinding">https://happycoding.io/tutorials/libgdx/pathfinding</a>
 */
public final class EntityGraph implements IndexedGraph<EntityNode> {

    /**
     * Array with all nodes for the graph (all tiles on the grid)
     */
    private final Array<EntityNode> entityNodes;

    /**
     * Map with all connections from each node, used for path finding algorithm
     */
    private final ObjectMap<EntityNode, Array<Connection<EntityNode>>> connectionsMap;

    /**
     * Create new entity graph by using an entity grid to create nodes and connections.
     *
     * @param grid Entity grid to create nodes and connections
     * @throws IllegalArgumentException If the grid has a size of zero
     */
    public EntityGraph(@NonNull Entity[][] grid) throws IllegalArgumentException {
        if (grid.length == 0 || grid[0].length == 0) {
            throw new IllegalArgumentException("grid must consist of at least one entity");
        }

        int gridWidth = grid.length;
        int gridHeight = grid[0].length;
        int gridSize = gridWidth * gridHeight;
        entityNodes = new Array<>(gridSize);
        connectionsMap = new ObjectMap<>();

        int nodeIndex = 0;

        // Create temporary grid to store entity nodes
        var nodeGrid = new EntityNode[gridWidth][gridHeight];

        // Create nodes and connection between nodes
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                nodeIndex = createNodeAndConnectionForTile(grid, gridWidth, gridHeight, nodeIndex, nodeGrid, x, y);
            }
        }
    }

    private int createNodeAndConnectionForTile(@NonNull Entity[][] grid, int gridWidth, int gridHeight, int nodeIndex,
                                               EntityNode[][] nodeGrid, int x, int y) {
        var currentNode = nodeGrid[x][y];

        // Add entity node at position x y, if not already created
        if (currentNode == null) {
            currentNode = new EntityNode(grid[x][y], x, y, nodeIndex++);
            nodeGrid[x][y] = currentNode;
            entityNodes.add(currentNode);
        }

        // Add horizontal connection
        if (x < gridWidth - 1) {
            nodeIndex = addConnection(grid, nodeGrid, currentNode, x + 1, y, nodeIndex);
        }

        // Add vertical connection
        if (y < gridHeight - 1) {
            nodeIndex = addConnection(grid, nodeGrid, currentNode, x, y + 1, nodeIndex);
        }

        return nodeIndex;
    }

    /**
     * Adds a bidirectional connection between the <code>currentNode</code> and the node with the specified position.
     * Also creates new node with the index <code>nodeIndex</code> if there's no node with the specified position.
     *
     * @param grid        Grid with entities
     * @param nodeGrid    Grid with entity nodes
     * @param currentNode Node from which the connection starts
     * @param nextX       X coordinate of node to which the connection goes
     * @param nextY       Y coordinate of node to which the connection goes
     * @param nodeIndex   Next free index of nodes
     * @return Updated index value, was incremented by one if new node didn't exist, otherwise it's equal to
     * <code>nodeIndex</code> argument
     */
    private int addConnection(@NonNull Entity[][] grid, @NonNull EntityNode[][] nodeGrid,
                              @NonNull EntityNode currentNode, int nextX, int nextY, int nodeIndex) {
        // If node not already exists create one
        if (nodeGrid[nextX][nextY] == null) {
            var newEntityNode = new EntityNode(grid[nextX][nextY], nextX, nextY, nodeIndex++);
            nodeGrid[nextX][nextY] = newEntityNode;
            entityNodes.add(newEntityNode);
        }
        // Add connection to node
        addConnection(currentNode, nodeGrid[nextX][nextY]);

        return nodeIndex;
    }

    /**
     * Adding a bidirectional connection between <code>fromNode</code> and <code>toNode</code> to the
     * <code>connectionsMap</code>.
     *
     * @param fromNode First node of the connection
     * @param toNode   Second node if the connection
     */
    private void addConnection(@NonNull EntityNode fromNode, @NonNull EntityNode toNode) {
        // Don't create a connection if at least one of the nodes is a tower
        if (fromNode.getEntity() instanceof Tower || toNode.getEntity() instanceof Tower) {
            return;
        }

        // Create connection from fromNode to toNode and store it in connectionsMap
        var connectionTo = new EntityConnection(fromNode, toNode);
        if (!connectionsMap.containsKey(fromNode)) {
            connectionsMap.put(fromNode, new Array<>());
        }
        connectionsMap.get(fromNode).add(connectionTo);

        // Create connection from toNode to fromNode and store it in connectionsMap
        var connectionFrom = new EntityConnection(toNode, fromNode);
        if (!connectionsMap.containsKey(toNode)) {
            connectionsMap.put(toNode, new Array<>());
        }
        connectionsMap.get(toNode).add(connectionFrom);
    }

    /**
     * Searches for a node in <code>entityNodes</code> with the specified position.
     *
     * @param x X position of the node
     * @param y Y position of the node
     * @return The entity node with the specified position or null if there's no entity node with that position
     */
    private @Nullable EntityNode findNode(int x, int y) {
        for (var entityNode : new Array.ArrayIterator<>(entityNodes).iterator()) {
            if (entityNode.getX() == x && entityNode.getY() == y) {
                return entityNode;
            }
        }
        return null;
    }

    /**
     * Searches for a path from <code>startPosition</code> to <code>endPosition</code>.
     *
     * @param startPosition Start position of path
     * @param endPosition   End position of path
     * @return Best possible path from <code>startPosition</code> to <code>endPosition</code>, may not be successful
     * and returns a path with no waypoints because there was no path
     */
    public @NotNull GraphPath<EntityNode> findPath(@NonNull Vector2 startPosition, @NonNull Vector2 endPosition) {
        var startNode = findNode((int) startPosition.x, (int) startPosition.y);
        var endNode = findNode((int) endPosition.x, (int) endPosition.y);

        // Check if positions are inside the grid, therefor the nodes are non-null
        if (startNode == null || endNode == null) {
            throw new IllegalArgumentException("startPosition and endPosition must be inside the grid");
        }

        GraphPath<EntityNode> entityPath = new DefaultGraphPath<>();
        new IndexedAStarPathFinder<>(this).searchNodePath(startNode, endNode, new EntityHeuristic(), entityPath);
        return entityPath;
    }

    /**
     * Returns the unique index of the given node.
     *
     * @param node the node whose index will be returned
     * @return the unique index of the given node.
     */
    @Override
    public int getIndex(@NonNull EntityNode node) {
        return node.getIndex();
    }

    /**
     * Returns the number of nodes in this graph.
     */
    @Override
    public int getNodeCount() {
        return entityNodes.size;
    }

    /**
     * Returns the connections outgoing from the given node.
     *
     * @param fromNode the node whose outgoing connections will be returned
     * @return the array of connections outgoing from the given node.
     */
    @Override
    public Array<Connection<EntityNode>> getConnections(@NonNull EntityNode fromNode) {
        if (connectionsMap.containsKey(fromNode)) {
            return connectionsMap.get(fromNode);
        }
        return new Array<>(0);
    }
}
