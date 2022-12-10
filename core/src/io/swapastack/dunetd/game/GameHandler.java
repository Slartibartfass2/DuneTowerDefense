package io.swapastack.dunetd.game;

import com.badlogic.gdx.math.Vector2;

import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.assets.controller.HostileUnitController;
import io.swapastack.dunetd.assets.controller.ShaiHuludController;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.portals.EndPortal;
import io.swapastack.dunetd.entities.portals.StartPortal;
import io.swapastack.dunetd.entities.towers.Tower;
import io.swapastack.dunetd.entities.towers.TowerEnum;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import io.swapastack.dunetd.hostileunits.HostileUnitEnum;
import io.swapastack.dunetd.math.DuneTDMath;
import io.swapastack.dunetd.pathfinding.Path;
import io.swapastack.dunetd.shaihulud.ShaiHulud;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public final class GameHandler {

    // Constants
    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");
    private static final int PLAYER_INITIAL_HEALTH = Configuration.getInstance()
            .getIntProperty("PLAYER_INITIAL_HEALTH");
    private static final int PLAYER_INITIAL_SPICE = Configuration.getInstance().getIntProperty("PLAYER_INITIAL_SPICE");
    private static final int INFANTRY_INITIAL_WAVE_BUDGET = Configuration.getInstance()
            .getIntProperty("INFANTRY_INITIAL_WAVE_BUDGET");
    private static final int HARVESTER_INITIAL_WAVE_BUDGET = Configuration.getInstance()
            .getIntProperty("HARVESTER_INITIAL_WAVE_BUDGET");
    private static final int BOSS_UNIT_INITIAL_WAVE_BUDGET = Configuration.getInstance()
            .getIntProperty("BOSS_UNIT_INITIAL_WAVE_BUDGET");
    private static final int GAME_BUILD_PHASE_DURATION_IN_MILLISECONDS = Configuration.getInstance()
            .getIntProperty("GAME_BUILD_PHASE_DURATION_IN_MILLISECONDS");
    private static final int MAX_WAVE_COUNT = Configuration.getInstance().getIntProperty("MAX_WAVE_COUNT");
    private static final int END_OF_WAVE_SPICE_REWARD = Configuration.getInstance()
            .getIntProperty("END_OF_WAVE_SPICE_REWARD");
    private static final float INFANTRY_BUDGET_MULTIPLIER = Configuration.getInstance()
            .getFloatProperty("INFANTRY_BUDGET_MULTIPLIER");
    private static final float HARVESTER_BUDGET_MULTIPLIER = Configuration.getInstance()
            .getFloatProperty("HARVESTER_BUDGET_MULTIPLIER");
    private static final float BOSS_UNIT_BUDGET_MULTIPLIER = Configuration.getInstance()
            .getFloatProperty("BOSS_UNIT_BUDGET_MULTIPLIER");
    private static final float TOWER_TEAR_DOWN_REFUND = Configuration.getInstance()
            .getFloatProperty("TOWER_TEAR_DOWN_REFUND");
    private static final int INFANTRY_PURCHASE_PRICE = Configuration.getInstance()
            .getIntProperty("INFANTRY_PURCHASE_PRICE");
    private static final int HARVESTER_PURCHASE_PRICE = Configuration.getInstance()
            .getIntProperty("HARVESTER_PURCHASE_PRICE");
    private static final int BOSS_UNIT_PURCHASE_PRICE = Configuration.getInstance()
            .getIntProperty("BOSS_UNIT_PURCHASE_PRICE");
    private static final int HOSTILE_UNIT_RELEASE_DELAY_IN_MILLISECONDS = Configuration.getInstance()
            .getIntProperty("HOSTILE_UNIT_RELEASE_DELAY_IN_MILLISECONDS");
    private static final float HOSTILE_UNIT_RELEASE_DELAY_MULTIPLIER = Configuration.getInstance()
            .getFloatProperty("HOSTILE_UNIT_RELEASE_DELAY_MULTIPLIER");
    private static final int INFANTRY_MAX_COUNT = Configuration.getInstance().getIntProperty("INFANTRY_MAX_COUNT");
    private static final int HARVESTER_MAX_COUNT = Configuration.getInstance().getIntProperty("HARVESTER_MAX_COUNT");

    // Controller for entities and hostile units
    private final EntityController entityController;
    private final HostileUnitController hostileUnitController;

    // Player information
    @Getter
    private final Statistics statistics;
    @Getter
    private int playerHealth;
    @Getter
    private int playerSpice;

    // Grid
    @Getter
    private final Entity[][] grid;
    @Getter
    private final int gridWidth;
    @Getter
    private final int gridHeight;

    // Hostile units and wave
    @Getter
    private final ArrayList<HostileUnit> hostileUnitsOnGrid;
    private Queue<HostileUnit> hostileUnitsQueue;
    private int infantryWaveBudget;
    private int harvesterWaveBudget;
    private int bossUnitWaveBudget;
    @Getter
    private int waveNumber;
    private int remainingHostileUnitReleaseDelayInMilliseconds;
    private int hostileUnitReleaseDelayInMilliseconds;
    @Getter
    private int waveHostileUnitCount;

    // Game
    @Getter
    private GamePhase gamePhase;
    @Getter
    private int remainingBuildPhaseDurationInMilliseconds;
    @Getter
    private boolean gameStarted;
    @Getter
    @Setter
    private boolean gamePaused;
    @Getter
    private Path path;
    private TimeFactor timeFactor;

    // ShaiHulud
    @Getter
    private final ShaiHulud shaiHulud;

    // Portals
    @Getter
    private final StartPortal startPortal;
    @Getter
    private final EndPortal endPortal;

    /**
     * Creates a game handler with a grid with the specified dimensions.
     *
     * @param gridWidth  Width of the grid
     * @param gridHeight Height of the grid
     */
    public GameHandler(int gridWidth, int gridHeight) {
        this(gridWidth, gridHeight, null, null, null);
    }

    /**
     * Creates a game handler with a grid with the specified dimensions.
     *
     * @param gridWidth  Width of the grid
     * @param gridHeight Height of the grid
     */
    public GameHandler(int gridWidth, int gridHeight, @Nullable EntityController entityController,
                       @Nullable HostileUnitController hostileUnitController,
                       @Nullable ShaiHuludController shaiHuludController) {
        // Check if gridWidth and gridHeight have valid values
        if (gridWidth < 2 || gridWidth > MAX_GRID_WIDTH) {
            throw new IllegalArgumentException("The grid must have a width between 2 and " + MAX_GRID_WIDTH);
        }
        if (gridHeight < 2 || gridHeight > MAX_GRID_HEIGHT) {
            throw new IllegalArgumentException("The grid must have a height between 2 and " + MAX_GRID_HEIGHT);
        }

        // Set initial player values
        playerHealth = PLAYER_INITIAL_HEALTH;
        playerSpice = PLAYER_INITIAL_SPICE;
        hostileUnitReleaseDelayInMilliseconds = HOSTILE_UNIT_RELEASE_DELAY_IN_MILLISECONDS;

        // Create grid and its dimensions
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        grid = new Entity[gridWidth][gridHeight];

        this.entityController = entityController;
        this.hostileUnitController = hostileUnitController;

        // Placing portals on the grid and adding game models to portals and adding them to the scene manager
        startPortal = new StartPortal(0, 0, entityController);
        placeEntityOnGrid(startPortal);
        endPortal = new EndPortal(gridWidth - 1, gridHeight - 1, entityController);
        placeEntityOnGrid(endPortal);

        // Find first path and check if it's valid
        path = Path.calculatePath(grid, startPortal.getGridPosition2d(), endPortal.getGridPosition2d());
        if (path.getLength() < 1) {
            throw new IllegalArgumentException("The path must have a length of at least one");
        }

        // Prepare first wave
        hostileUnitsOnGrid = new ArrayList<>();
        // Set initial game state
        infantryWaveBudget = INFANTRY_INITIAL_WAVE_BUDGET;
        harvesterWaveBudget = HARVESTER_INITIAL_WAVE_BUDGET;
        bossUnitWaveBudget = BOSS_UNIT_INITIAL_WAVE_BUDGET;
        waveNumber = 1;
        remainingHostileUnitReleaseDelayInMilliseconds = 0;
        gamePhase = GamePhase.BUILD_PHASE;
        remainingBuildPhaseDurationInMilliseconds = GAME_BUILD_PHASE_DURATION_IN_MILLISECONDS;
        gameStarted = false;
        gamePaused = false;
        timeFactor = TimeFactor.NORMAL;

        shaiHulud = new ShaiHulud(grid, shaiHuludController);
        statistics = new Statistics();
    }

    /**
     * Updates the game logic (execution of different game phases).
     *
     * @param deltaTimeInMilliseconds The time in milliseconds since the last update
     */
    public void update(float deltaTimeInMilliseconds) {
        if (!gameStarted || gamePaused) {
            return;
        }

        // speed up or slow down time
        var factoredDeltaTimeInMilliseconds = deltaTimeInMilliseconds * timeFactor.getFactor();

        switch (gamePhase) {
            case BUILD_PHASE -> executeBuildPhase(factoredDeltaTimeInMilliseconds);
            case WAVE_PHASE -> executeWavePhase(factoredDeltaTimeInMilliseconds);
            default -> { /* Do nothing because game is finished */ }
        }
    }

    /**
     * Executes the logic of the build phase of the game.
     *
     * @param deltaTimeInMilliseconds The time in milliseconds since the last update
     */
    private void executeBuildPhase(float deltaTimeInMilliseconds) {
        remainingBuildPhaseDurationInMilliseconds -= deltaTimeInMilliseconds;

        // End of build phase, create new wave
        if (remainingBuildPhaseDurationInMilliseconds <= 0) {
            gamePhase = GamePhase.WAVE_PHASE;
            hostileUnitsQueue = createNewWave(infantryWaveBudget, harvesterWaveBudget, bossUnitWaveBudget);
            remainingHostileUnitReleaseDelayInMilliseconds = 0;
            hostileUnitReleaseDelayInMilliseconds *= HOSTILE_UNIT_RELEASE_DELAY_MULTIPLIER;
        }
    }

    /**
     * Executes the logic of the wave phase of the game.
     *
     * @param deltaTimeInMilliseconds The time in milliseconds since the last update
     */
    private void executeWavePhase(float deltaTimeInMilliseconds) {
        // Release new hostile units on the grid through the start portal
        releaseHostileUnits(deltaTimeInMilliseconds);

        // Update shai hulud (moving, killing hostile units, setting towers to debris)
        shaiHulud.update(hostileUnitsOnGrid, deltaTimeInMilliseconds, statistics);

        // Let towers attack the hostile units
        updateTowers(deltaTimeInMilliseconds);

        // Update HostileUnits
        updateHostileUnits(deltaTimeInMilliseconds);

        // If too many HostileUnits reached the end portal, the player has lost
        // Otherwise check if the wave phase is finished (all HostileUnits dead or reached portal)
        if (playerHealth <= 0) {
            gamePhase = GamePhase.GAME_LOST_PHASE;
            pauseGame(true);

        } else if (hostileUnitsQueue.isEmpty() && hostileUnitsOnGrid.isEmpty()) {
            removeDebris();
            timeFactor = TimeFactor.NORMAL;

            // If the last wave was completed the player has won
            // Otherwise change to build phase
            if (waveNumber == MAX_WAVE_COUNT) {
                gamePhase = GamePhase.GAME_WON_PHASE;
                pauseGame(true);

            } else {
                playerSpice += END_OF_WAVE_SPICE_REWARD;

                shaiHulud.reset(true);

                // Multiply budgets with multipliers to prepare next wave
                waveNumber++;
                infantryWaveBudget *= INFANTRY_BUDGET_MULTIPLIER;
                harvesterWaveBudget *= HARVESTER_BUDGET_MULTIPLIER;
                bossUnitWaveBudget *= BOSS_UNIT_BUDGET_MULTIPLIER;

                // Set phase to build phase
                gamePhase = GamePhase.BUILD_PHASE;
                remainingBuildPhaseDurationInMilliseconds = GAME_BUILD_PHASE_DURATION_IN_MILLISECONDS;
            }
        }
    }

    /**
     * Places a tower at the specified position on the grid.
     *
     * @param towerEnum Selected tower which should be placed
     * @param x         X coordinate on grid where the tower should be placed
     * @param y         Y coordinate on grid where the tower should be placed
     * @return True if the tower could be placed, otherwise false
     */
    public boolean buildTower(@NonNull TowerEnum towerEnum, int x, int y) {
        // If the game is not in the build phase or the position is occupied, the tower cannot be built
        if (gamePhase != GamePhase.BUILD_PHASE || !DuneTDMath.isPositionAvailable(grid, x, y)) {
            return false;
        }

        // Create new tower according to the players selection
        var tower = towerEnum.toTower(x, y, entityController);

        // Place newly created tower
        return buildTower(tower, x, y);
    }

    /**
     * Places a tower at the specified position on the grid.
     *
     * @param tower Tower which should be placed
     * @param x     X coordinate on grid where the tower should be placed
     * @param y     Y coordinate on grid where the tower should be placed
     * @return true if the tower could be placed, otherwise false
     */
    private boolean buildTower(@NonNull Tower tower, int x, int y) {
        // If the game is not in the build phase, the position is occupied or the player has not enough spice, the
        // tower cannot be built
        if (gamePhase != GamePhase.BUILD_PHASE || !DuneTDMath.isPositionAvailable(grid, x, y)
                || playerSpice < tower.getBuildCost()) {
            tower.destroy();
            return false;
        }

        // Set tower at new position
        grid[x][y] = tower;

        boolean isPositionOnPath = false;
        for (var wayPoint : path.getWaypoints()) {
            if (wayPoint != null && wayPoint.equals(new Vector2(x, y))) {
                isPositionOnPath = true;
                break;
            }
        }

        // If position is on the path, check if there's another path
        if (isPositionOnPath) {
            var newPath = Path.calculatePath(grid, startPortal.getGridPosition2d(), endPortal.getGridPosition2d());

            // If there's no other path, abort building of tower
            if (newPath.isBlocked()) {
                // Remove path blocking entity
                grid[x][y] = null;
                tower.destroy();
                return false;
            }

            // Replace old path with new path
            path = newPath;
        }

        // Start the countdown of the build phase if the first tower was built
        if (!gameStarted) {
            gameStarted = true;
        }

        playerSpice -= tower.getBuildCost();

        tower.show();

        statistics.builtTower(TowerEnum.fromTower(tower));
        gamePaused = false;

        return true;
    }

    /**
     * Removes the tower at gridPosition on the grid.
     *
     * @param x X coordinate on grid where the tower should be removed
     * @param y Y coordinate on grid where the tower should be removed
     * @return true if the tower could be removed, otherwise false
     */
    public boolean tearDownTower(int x, int y) {
        // The player can tear down a tower only in the build phase or
        if (gamePhase != GamePhase.BUILD_PHASE) {
            return false;
        }

        // If the position is available, there's no tower to tear down
        if (DuneTDMath.isPositionAvailable(grid, x, y)) {
            return false;
        }

        // If position is outside the grid, there's no tower to tear down
        if (!DuneTDMath.isPositionInsideGrid(grid, x, y)) {
            return false;
        }

        var entity = grid[x][y];
        if (entity instanceof Tower tower) {
            tower.destroy();
            grid[x][y] = null;

            // Get refund
            playerSpice += TOWER_TEAR_DOWN_REFUND * tower.getBuildCost();

            // Calculate new path, only if new path is shorter replace old path
            var newPath = Path.calculatePath(grid, startPortal.getGridPosition2d(), endPortal.getGridPosition2d());
            if (newPath.getLength() < path.getLength()) {
                path = newPath;
            }

            return true;
        }

        // The entity was not a tower (probably a portal)
        return false;
    }

    /**
     * Creates a new queue of hostile units representing a wave. Each type of hostile unit has its own budget and max
     * count which cannot be exceeded.
     *
     * @param infantryWaveBudget  Budget for infantries
     * @param harvesterWaveBudget Budget for harvesters
     * @param bossUnitWaveBudget  Budget for boss units
     * @return A queue of hostile units representing a wave. The amount of each type in the queue is limited by the
     * MAX_COUNT constants
     */
    @NotNull
    private Queue<HostileUnit> createNewWave(int infantryWaveBudget, int harvesterWaveBudget, int bossUnitWaveBudget) {
        var hostileUnitsQueueTmp = new LinkedBlockingQueue<HostileUnit>();
        var spawnPoint = new Vector2(startPortal.getX(), startPortal.getY());

        // Add infantry to wave queue
        int remainingBudget = getHostileUnitsFromBudget(hostileUnitsQueueTmp, HostileUnitEnum.INFANTRY,
                infantryWaveBudget, INFANTRY_PURCHASE_PRICE, spawnPoint, INFANTRY_MAX_COUNT);
        var remainingHarvesterWaveBudget = harvesterWaveBudget + remainingBudget;

        // Add harvesters to wave queue
        remainingBudget = getHostileUnitsFromBudget(hostileUnitsQueueTmp, HostileUnitEnum.HARVESTER,
                remainingHarvesterWaveBudget, HARVESTER_PURCHASE_PRICE, spawnPoint, HARVESTER_MAX_COUNT);
        var remainingBossUnitWaveBudget = bossUnitWaveBudget + remainingBudget;

        // Add boss units to wave queue
        getHostileUnitsFromBudget(hostileUnitsQueueTmp, HostileUnitEnum.BOSS_UNIT, remainingBossUnitWaveBudget,
                BOSS_UNIT_PURCHASE_PRICE, spawnPoint, Integer.MAX_VALUE);

        waveHostileUnitCount = hostileUnitsQueueTmp.size();
        return hostileUnitsQueueTmp;
    }

    /**
     * Adds hostile units of the specified type to the specified queue, limited by the specified budget and max count
     * variables.
     *
     * @param hostileUnitsQueue Queue to which the hostile units are added
     * @param hostileUnitEnum   Type of hostile units added
     * @param budget            Budget for hostile units
     * @param purchasePrice     Price to 'buy' hostile units
     * @param spawnPoint        Spawn point of each hostile unit (start portal)
     * @param maxCount          Maximum amount of hostile units of specified type added to the queue
     * @return Budget remaining after purchasing hostile units
     */
    private int getHostileUnitsFromBudget(@NonNull Queue<HostileUnit> hostileUnitsQueue,
                                          @NonNull HostileUnitEnum hostileUnitEnum, int budget, int purchasePrice,
                                          @NonNull Vector2 spawnPoint, int maxCount) {
        int remainingBudget = budget;
        int count = 0;
        while (remainingBudget >= purchasePrice && count < maxCount) {
            var hostileUnit = hostileUnitEnum.toHostileUnit(spawnPoint.cpy(), hostileUnitController);
            remainingBudget -= purchasePrice;
            hostileUnitsQueue.add(hostileUnit);
            count++;
        }
        return remainingBudget;
    }

    /**
     * Releases hostile units on the grid, if available in <code>hostileUnitsQueue</code>.
     *
     * @param deltaTimeInMilliseconds The time in milliseconds since the last update
     */
    private void releaseHostileUnits(float deltaTimeInMilliseconds) {
        if (remainingHostileUnitReleaseDelayInMilliseconds > 0) {
            remainingHostileUnitReleaseDelayInMilliseconds -= deltaTimeInMilliseconds;
        } else if (!hostileUnitsQueue.isEmpty()) {
            // Get hostile unit from queue and put in on the grid
            var hostileUnit = hostileUnitsQueue.poll();
            hostileUnit.show();
            hostileUnitsOnGrid.add(hostileUnit);
            // Reset delay
            remainingHostileUnitReleaseDelayInMilliseconds = hostileUnitReleaseDelayInMilliseconds;
        }
    }

    /**
     * Updates hostile units. Removes dead ones and moves the rest along the path. Checks if hostile units reached
     * the end portal.
     *
     * @param deltaTimeInMilliseconds The time in milliseconds since the last update
     */
    private void updateHostileUnits(float deltaTimeInMilliseconds) {
        for (int i = hostileUnitsOnGrid.size() - 1; i >= 0; i--) {
            var hostileUnit = hostileUnitsOnGrid.get(i);

            // Remove hostile unit if dead
            if (hostileUnit.isDead()) {
                playerSpice += hostileUnit.getSpiceReward();
                hostileUnitsOnGrid.remove(hostileUnit);
                statistics.killedHostileUnitByTower(HostileUnitEnum.fromHostileUnit(hostileUnit));

                // Remove hostile unit if at end portal
            } else if (hostileUnit.getPosition().equals(endPortal.getGridPosition2d())) {
                playerHealth -= hostileUnit.getHealth();
                hostileUnit.kill();
                hostileUnitsOnGrid.remove(hostileUnit);
                statistics.hostileUnitReachedEndPortal(HostileUnitEnum.fromHostileUnit(hostileUnit));

                // Otherwise, move hostile unit
            } else {
                hostileUnit.move(path, deltaTimeInMilliseconds);
            }
        }
    }

    /**
     * Updates all towers on the grid.
     *
     * @param deltaTimeInMilliseconds The time in milliseconds since the last update
     */
    private void updateTowers(float deltaTimeInMilliseconds) {
        for (var entities : grid) {
            for (var entity : entities) {
                if (entity instanceof Tower tower && !tower.isDebris()) {
                    tower.update(hostileUnitsOnGrid, deltaTimeInMilliseconds);
                }
            }
        }
    }

    /**
     * Removes all debris from the grid.
     */
    private void removeDebris() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] instanceof Tower tower && tower.isDebris()) {
                    tower.destroy();
                    grid[x][y] = null;
                }
            }
        }
        // Calculate new path, only if new path is shorter replace old path
        var newPath = Path.calculatePath(grid, startPortal.getGridPosition2d(), endPortal.getGridPosition2d());
        if (newPath.getLength() < path.getLength()) {
            path = newPath;
        }
    }

    /**
     * Returns the number of hostile units remaining in current wave. (Sum of hostile units on grid and hostile units
     * in queue).
     *
     * @return Number of hostile units remaining in current wave
     */
    public int getNumberOfRemainingHostileUnits() {
        return hostileUnitsOnGrid.size() + hostileUnitsQueue.size();
    }

    /**
     * Sets grid entry at position of specified entity to entity.
     *
     * @param entity Entity to place on the grid
     */
    private void placeEntityOnGrid(@NonNull Entity entity) {
        grid[entity.getX()][entity.getY()] = entity;
    }

    /**
     * Skips the build phase by setting the remaining duration to zero.
     */
    public void skipBuildPhase() {
        if (gamePhase == GamePhase.BUILD_PHASE) {
            remainingBuildPhaseDurationInMilliseconds = 0;
        }
    }

    /**
     * Un/pauses game according to specified pause boolean. If the game is paused all animations are paused as well.
     *
     * @param pause If game should be paused or not
     */
    public void pauseGame(boolean pause) {
        gamePaused = pause;
        if (hostileUnitController != null) {
            hostileUnitController.pauseAnimations(gamePaused);
        }
    }

    /**
     * Sets the time factor to the next higher value.
     */
    public void speedUpTime() {
        timeFactor = timeFactor.speedUp();
    }

    /**
     * Sets the time factor to the next lower value.
     */
    public void slowDownTime() {
        timeFactor = timeFactor.slowDown();
    }
}
