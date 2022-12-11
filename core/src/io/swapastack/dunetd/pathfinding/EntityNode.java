package io.swapastack.dunetd.pathfinding;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;

import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.vectors.Vector2;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Wrapper for entity which stores the nullable entity, the position and the index, used for the
 * <code>EntityGraph</code> class. Inspired by
 * <a href="https://happycoding.io/tutorials/libgdx/pathfinding">https://happycoding.io/tutorials/libgdx/pathfinding</a>
 */
@EqualsAndHashCode
public class EntityNode {

    /**
     * Nullable entity with gets wrapped by this entity node
     */
    @Getter
    private final Entity entity;

    /**
     * Position of entity
     */
    @Getter
    private final Vector2 position;

    /**
     * Unique index of entity node (used for {@link IndexedAStarPathFinder})
     */
    @Getter
    private final int index;

    /**
     * Creates new entity node, which wraps an entity and adds an index to it.
     *
     * @param entity Wrapped entity
     * @param x      X Coordinate of entity
     * @param y      Y Coordinate of entity
     * @param index  Unique index of entity node
     */
    public EntityNode(Entity entity, int x, int y, int index) {
        this.entity = entity;
        this.position = new Vector2(x, y);
        this.index = index;
    }
}
