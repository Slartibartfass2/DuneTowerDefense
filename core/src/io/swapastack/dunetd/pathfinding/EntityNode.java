package io.swapastack.dunetd.pathfinding;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;

import io.swapastack.dunetd.entities.Entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Wrapper for entity which stores the nullable entity, the position and the index, used for the
 * <code>EntityGraph</code> class. Inspired by
 * <a href="https://happycoding.io/tutorials/libgdx/pathfinding">https://happycoding.io/tutorials/libgdx/pathfinding</a>
 */
@EqualsAndHashCode
@SuppressWarnings("ClassCanBeRecord")
public class EntityNode {
    
    /** Nullable entity with gets wrapped by this entity node */
    @Getter
    private final Entity entity;
    
    /** X coordinate of entities position */
    @Getter
    private final int x;
    
    /** Y coordinate of entities position */
    @Getter
    private final int y;
    
    /** Unique index of entity node (used for {@link IndexedAStarPathFinder}) */
    @Getter
    private final int index;
    
    /**
     * Creates new entity node, which wraps an entity and adds an index to it.
     *
     * @param entity Wrapped entity
     * @param x      X coordinate of entities position
     * @param y      Y coordinate of entities position
     * @param index  Unique index of entity node
     */
    public EntityNode(Entity entity, int x, int y, int index) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.index = index;
    }
}
