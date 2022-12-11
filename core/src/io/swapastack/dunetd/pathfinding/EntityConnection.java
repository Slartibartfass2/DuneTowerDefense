package io.swapastack.dunetd.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;

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
    private final EntityNode fromEntity;

    /**
     * Entity node to which the connection leads
     */
    private final EntityNode toEntity;

    /**
     * Creates connection between to entity nodes.
     *
     * @param fromEntity Entity node from which the connection originates
     * @param toEntity   Entity node to which the connection leads
     */
    public EntityConnection(@NonNull EntityNode fromEntity, @NonNull EntityNode toEntity) {
        this.fromEntity = fromEntity;
        this.toEntity = toEntity;
    }

    /**
     * Returns the non-negative cost of this connection
     */
    @Override
    public float getCost() {
        return COST;
    }

    /**
     * Returns the node that this connection came from.
     */
    @Override
    public EntityNode getFromNode() {
        return fromEntity;
    }

    /**
     * Returns the node that this connection leads to.
     */
    @Override
    public EntityNode getToNode() {
        return toEntity;
    }
}
