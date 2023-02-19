package io.swapastack.dunetd.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;

import lombok.Getter;
import lombok.NonNull;

/**
 * Representing a connection between to entity nodes. Storing the entity nodes and the cost of the connection. Used
 * for EntityGraph class.
 * Inspired by <a href="https://happycoding.io/tutorials/libgdx/pathfinding">https://happycoding.io/tutorials/libgdx/pathfinding</a>
 */
public final class EntityConnection implements Connection<EntityNode> {

    /**
     * Cost of connection (is static because nodes which are connected are always neighbors)
     */
    private static final int COST = 1000;

    /**
     * Entity node from which the connection originates
     */
    @Getter
    private final EntityNode fromNode;

    /**
     * Entity node to which the connection leads
     */
    @Getter
    private final EntityNode toNode;

    /**
     * Creates connection between to entity nodes.
     *
     * @param fromNode Entity node from which the connection originates
     * @param toNode   Entity node to which the connection leads
     */
    public EntityConnection(@NonNull EntityNode fromNode, @NonNull EntityNode toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    /**
     * Returns the non-negative cost of this connection
     */
    @Override
    public float getCost() {
        return COST;
    }
}
