package io.swapastack.dunetd.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.math.Vector2;

import lombok.NonNull;

/**
 * EntityHeuristic class, estimating the heuristic value for a node to another node. Used for EntityGraph class.
 * Inspired by <a href="https://happycoding.io/tutorials/libgdx/pathfinding">https://happycoding.io/tutorials/libgdx/pathfinding</a>
 */
public final class EntityHeuristic implements Heuristic<EntityNode> {

    /**
     * Calculates an estimated cost to reach the goal node from the given node.
     *
     * @param node    the start node
     * @param endNode the end node
     * @return the estimated cost
     */
    @Override
    public float estimate(@NonNull EntityNode node, @NonNull EntityNode endNode) {
        // Heuristic value is squared distance between nodes
        return Vector2.dst2(node.getX(), node.getY(), endNode.getX(), endNode.getY());
    }
}
