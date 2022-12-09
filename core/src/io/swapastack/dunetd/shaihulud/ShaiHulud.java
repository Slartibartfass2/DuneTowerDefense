package io.swapastack.dunetd.shaihulud;

import com.badlogic.gdx.math.Vector2;

import io.swapastack.dunetd.assets.controller.ShaiHuludController;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.Tower;
import io.swapastack.dunetd.entities.towers.TowerEnum;
import io.swapastack.dunetd.game.CardinalDirection;
import io.swapastack.dunetd.game.GameModelData;
import io.swapastack.dunetd.game.Statistics;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.hostileunits.HostileUnitEnum;
import io.swapastack.dunetd.math.DuneTDMath;

import java.beans.PropertyChangeSupport;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import lombok.NonNull;

public final class ShaiHulud {

    private static final int COOLDOWN_IN_MS = Configuration.getInstance().getIntProperty("SHAI_HULUD_COOLDOWN_IN_MS");
    private static final float SPEED = Configuration.getInstance().getFloatProperty("SHAI_HULUD_SPEED");
    private static final float RANGE = 0.8f;

    private final Entity[][] grid;
    @Getter
    private int remainingCooldownInMs;
    private Vector2 firstThumper;
    private Vector2 secondThumper;
    @Getter
    private Vector2 gridPosition;
    private CardinalDirection movingDirection;
    private final PropertyChangeSupport support;
    private boolean alreadySummoned;

    public ShaiHulud(@NonNull Entity[][] grid, @Nullable ShaiHuludController shaiHuludController) {
        this.grid = grid;
        remainingCooldownInMs = 0;
        firstThumper = null;
        secondThumper = null;
        gridPosition = null;
        movingDirection = null;
        alreadySummoned = false;

        if (shaiHuludController != null) {
            support = new PropertyChangeSupport(this);

            // Add shai hulud controller as observer and call create event
            support.addPropertyChangeListener(shaiHuludController);
            support.firePropertyChange(ShaiHuludController.CREATE_EVENT_NAME, null, null);
        } else {
            support = null;
        }
    }

    /**
     * Updates the logic of the shai hulud (moving and checking cooldown).
     *
     * @param deltaTime The time in seconds since the last update
     */
    public void update(@NonNull List<HostileUnit> hostileUnits, float deltaTime, @NonNull Statistics statistics) {
        if (remainingCooldownInMs > 0) {
            remainingCooldownInMs -= deltaTime * 1000;
        }

        // If grid position is set, the shai hulud was already summoned
        if (gridPosition != null) {
            var moveDistance = SPEED * deltaTime;
            var direction = movingDirection.getDirection();

            gridPosition.add(direction.scl(moveDistance));

            // Reset shai hulud if he reached the edge of the grid
            if (!DuneTDMath.isPositionInsideGrid(grid, gridPosition.x, gridPosition.y)) {
                reset(false);
            } else {
                updateGameModel();
                destroyTowerOnCollision(statistics);
                killHostileUnits(hostileUnits, statistics);
            }
            // If both thumpers were set but the grid position is null, the shai hulud needs to be summoned
        } else if (firstThumper != null && secondThumper != null) {
            summonShaiHulud();
        }
    }

    private void updateGameModel() {
        // Update game model if existing
        if (support != null) {
            var gameModelData = new GameModelData(movingDirection.getDegrees(),
                    new Vector2(gridPosition.x, gridPosition.y));
            support.firePropertyChange(ShaiHuludController.UPDATE_EVENT_NAME, null, gameModelData);
        }
    }

    private void destroyTowerOnCollision(@NotNull Statistics statistics) {
        // If shai hulud hits tower, set tower to debris
        var entity = grid[(int) gridPosition.x][(int) gridPosition.y];
        if (entity instanceof Tower tower && !tower.isDebris()) {
            tower.setToDebris();
            statistics.destroyedTowerByShaiHulud(TowerEnum.fromTower(tower));
        }
    }

    private void killHostileUnits(@NonNull List<HostileUnit> hostileUnits, @NonNull Statistics statistics) {
        // If shai hulud hits hostile units kill hostile units
        for (var hostileUnit : hostileUnits) {
            var hostileUnitPosition = hostileUnit.getPosition();
            if (gridPosition.dst2(hostileUnitPosition) <= RANGE) {
                hostileUnit.kill();
                statistics.killedHostileUnitByShaiHulud(HostileUnitEnum.fromHostileUnit(hostileUnit));
            }
        }
    }

    private void summonShaiHulud() {
        // Calculate moving direction by using the vector between both thumpers
        var directionVector = secondThumper.cpy().sub(firstThumper).nor();
        movingDirection = CardinalDirection.fromDirection(directionVector);

        // Set initial grid position
        gridPosition = getSpawnPoint();

        alreadySummoned = true;

        // Make game model visible at right position if existing
        if (support != null) {
            support.firePropertyChange(ShaiHuludController.SHOW_EVENT_NAME, null,
                    new GameModelData(movingDirection.getDegrees(), new Vector2(gridPosition.x, gridPosition.y)));
        }
    }

    /**
     * Sets a thumper for the shai hulud (first or second according to previous method calls).
     *
     * @param position Position of new thumper
     * @return true if setting thumper was successful
     */
    public boolean setThumper(@NonNull Vector2 position) {
        // If the shai hulud has to cool down, is still active or was already summoned in this round, no new thumper
        // can be set
        if (remainingCooldownInMs > 0 || gridPosition != null || alreadySummoned) {
            return false;
        }

        // The user can't set a thumper outside the grid
        if (!DuneTDMath.isPositionInsideGrid(grid, position.x, position.y)) {
            return false;
        }

        // Set first thumper if there's none, if there was already one, so set second one
        if (firstThumper == null) {
            firstThumper = position.cpy();
        }
        // If the second thumper is not in the same row or column, or it's the same position the thumper can't be set
        else if (position.x != firstThumper.x && position.y != firstThumper.y || position.equals(firstThumper)) {
            return false;
        } else {
            secondThumper = position.cpy();
        }
        return true;
    }

    /**
     * Calculates the spawn point of the Shai Hulud at the edge of the grid
     *
     * @return Spawn point of Shai Hulud
     */
    private Vector2 getSpawnPoint() {
        return switch (movingDirection) {
            case NORTH -> new Vector2(firstThumper.x, 0f);
            case SOUTH -> new Vector2(firstThumper.x, grid[(int) firstThumper.x].length - 1f);
            case EAST -> new Vector2(0f, firstThumper.y);
            case WEST -> new Vector2(grid.length - 1f, firstThumper.y);
        };
    }

    /**
     * Sets thumpers to null.
     */
    public void cancelAttack() {
        firstThumper = null;
        secondThumper = null;
    }

    /**
     * Sets everything to default values.
     *
     * @param resetAlreadySummoned If the shai hulud should be possible to summon again
     */
    public void reset(boolean resetAlreadySummoned) {
        if (gridPosition != null) {
            remainingCooldownInMs = COOLDOWN_IN_MS;
        }
        cancelAttack();
        movingDirection = null;
        gridPosition = null;
        if (resetAlreadySummoned) {
            alreadySummoned = false;
        }
        if (support != null) {
            support.firePropertyChange(ShaiHuludController.VANISH_EVENT_NAME, null, null);
        }
    }

    public @Nullable Vector2 getFirstThumper() {
        if (firstThumper == null) {
            return null;
        }
        return firstThumper.cpy();
    }

    public @Nullable Vector2 getSecondThumper() {
        if (secondThumper == null) {
            return null;
        }
        return secondThumper.cpy();
    }
}
