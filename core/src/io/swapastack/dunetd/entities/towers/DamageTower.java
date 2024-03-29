package io.swapastack.dunetd.entities.towers;

import com.badlogic.gdx.math.MathUtils;

import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.vectors.Vector2;

import org.jetbrains.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 * A damage tower representing a part of the game grid. A damage tower can be: <br>
 * - a guard tower: a tower which deals damage to exactly one hostile unit in range. <br>
 * - a bomb tower: a tower which deals area damage to at least one hostile unit in range. <br>
 * It consists of the same properties as a tower plus the amount of damage it deals to hostile units.
 */
@EqualsAndHashCode(callSuper = true)
public abstract class DamageTower extends Tower {

    private static final int THREE_HUNDRED_SIXTY_DEGREES = 360;

    /**
     * Amount of health decreased when this tower attacks a hostile unit
     */
    @Getter
    private final int damage;

    /**
     * Creates a new damage tower with a specified position, range, build cost, damage and reload time.
     *
     * @param position                 position of this guard tower
     * @param range                    Range of this damage tower, in which it attacks hostile units
     * @param buildCost                Costs to build this damage tower
     * @param damage                   Damage of this damage tower
     * @param reloadTimeInMilliseconds Time in milliseconds needed to reload
     * @param entityController         Controller for towers
     */
    protected DamageTower(@NonNull Vector2 position, float range, int buildCost, int damage,
                          int reloadTimeInMilliseconds, @Nullable EntityController entityController) {
        super(position, range, buildCost, reloadTimeInMilliseconds, entityController,
                MathUtils.random(0, THREE_HUNDRED_SIXTY_DEGREES));
        this.damage = damage;
    }
}
