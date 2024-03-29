package io.swapastack.dunetd.game;

import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.DamageTower;
import io.swapastack.dunetd.entities.towers.TowerEnum;
import io.swapastack.dunetd.vectors.Vector2;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.NonNull;

class GameHandlerTest {

    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");

    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");

    private static final int GAME_BUILD_PHASE_DURATION_IN_MS = Configuration.getInstance()
            .getIntProperty("GAME_BUILD_PHASE_DURATION_IN_MILLISECONDS");

    @BeforeAll
    static void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        TestHelper.readConfigFile();
    }

    @Test
    void testBuildTowerWithEndlessSpice() throws NoSuchFieldException, IllegalAccessException {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setSpice(gameHandler, Integer.MAX_VALUE);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        var position = new Vector2(x, y);
                        if (gameHandler.getStartPortal().getPosition().equals(position)
                                || gameHandler.getEndPortal().getPosition().equals(position)) {
                            Assertions.assertFalse(gameHandler.buildTower(TowerEnum.GUARD_TOWER, x, y));
                            continue;
                        }

                        var doBreak = false;
                        for (var wayPoint : gameHandler.getPath().getWaypoints()) {
                            if (wayPoint.equals(position)) {
                                // could be true or false depending on if there's another path
                                doBreak = true;
                                break;
                            }
                        }
                        if (doBreak) {
                            break;
                        }

                        Assertions.assertTrue(gameHandler.buildTower(TowerEnum.GUARD_TOWER, x, y));
                        Assertions.assertNotNull(gameHandler.getGrid()[x][y]);
                    }
                }
            }
        }
    }

    @Test
    void testBuildTowerWithNotEnoughSpice() throws NoSuchFieldException, IllegalAccessException {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setSpice(gameHandler, 0);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        Assertions.assertFalse(gameHandler.buildTower(TowerEnum.GUARD_TOWER, x, y));
                    }
                }
            }
        }
    }

    @Test
    void whenTowerIsBuiltInTheWavePhase_thenBuildingIsCancelled()
            throws NoSuchFieldException, IllegalAccessException {
        var gameHandler = new GameHandler(10, 10);
        setPhase(gameHandler, GamePhase.WAVE_PHASE);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (x == 0 && y == 0 || x == 9 && y == 9) {
                    continue;
                }
                Assertions.assertFalse(gameHandler.buildTower(TowerEnum.GUARD_TOWER, x, y));
            }
        }
    }

    @Test
    void whenTowerIsBuiltInTheGameLostPhase_thenBuildingIsCancelled()
            throws NoSuchFieldException, IllegalAccessException {
        var gameHandler = new GameHandler(10, 10);
        setPhase(gameHandler, GamePhase.GAME_LOST_PHASE);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (x == 0 && y == 0 || x == 9 && y == 9) {
                    continue;
                }
                Assertions.assertFalse(gameHandler.buildTower(TowerEnum.GUARD_TOWER, x, y));
            }
        }
    }

    @Test
    void whenTowerIsBuiltInTheGameWonPhase_thenBuildingIsCancelled()
            throws NoSuchFieldException, IllegalAccessException {
        var gameHandler = new GameHandler(10, 10);
        setPhase(gameHandler, GamePhase.GAME_WON_PHASE);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (x == 0 && y == 0 || x == 9 && y == 9) {
                    continue;
                }
                Assertions.assertFalse(gameHandler.buildTower(TowerEnum.GUARD_TOWER, x, y));
            }
        }
    }

    @Test
    void testBuildTowerOutsideGrid() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);

                Assertions.assertFalse(gameHandler.buildTower(TowerEnum.GUARD_TOWER, -1, 0));
                Assertions.assertFalse(gameHandler.buildTower(TowerEnum.GUARD_TOWER, 0, -1));
                Assertions.assertFalse(gameHandler.buildTower(TowerEnum.GUARD_TOWER, -1, -1));
                Assertions.assertFalse(gameHandler.buildTower(TowerEnum.GUARD_TOWER, MAX_GRID_WIDTH, 0));
                Assertions.assertFalse(gameHandler.buildTower(TowerEnum.GUARD_TOWER, 0, MAX_GRID_HEIGHT));
                Assertions.assertFalse(gameHandler.buildTower(TowerEnum.GUARD_TOWER, MAX_GRID_WIDTH, MAX_GRID_HEIGHT));
            }
        }
    }

    @Test
    void testBuildTowerPathBlocking() throws NoSuchFieldException, IllegalAccessException {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setSpice(gameHandler, Integer.MAX_VALUE);

                Assertions.assertTrue(gameHandler.buildTower(TowerEnum.GUARD_TOWER, 0, 1));
                Assertions.assertNotNull(gameHandler.getGrid()[0][1]);
                Assertions.assertFalse(gameHandler.buildTower(TowerEnum.GUARD_TOWER, 1, 0));
            }
        }
    }

    @Test
    void whenTowerIsTearedDownInTheWavePhase_thenTearingDownIsCancelled()
            throws NoSuchFieldException, IllegalAccessException {
        var gameHandler = new GameHandler(10, 10);
        setPhase(gameHandler, GamePhase.WAVE_PHASE);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (x == 0 && y == 0 || x == 9 && y == 9) {
                    continue;
                }
                Assertions.assertFalse(gameHandler.tearDownTower(x, y));
            }
        }
    }

    @Test
    void whenTowerIsTearedDownInTheGameLostPhase_thenTearingDownIsCancelled()
            throws NoSuchFieldException, IllegalAccessException {
        var gameHandler = new GameHandler(10, 10);
        setPhase(gameHandler, GamePhase.GAME_LOST_PHASE);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (x == 0 && y == 0 || x == 9 && y == 9) {
                    continue;
                }
                Assertions.assertFalse(gameHandler.tearDownTower(x, y));
            }
        }
    }

    @Test
    void whenTowerIsTearedDownInTheGameWonPhase_thenTearingDownIsCancelled()
            throws NoSuchFieldException, IllegalAccessException {
        var gameHandler = new GameHandler(10, 10);
        setPhase(gameHandler, GamePhase.GAME_WON_PHASE);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (x == 0 && y == 0 || x == 9 && y == 9) {
                    continue;
                }
                Assertions.assertFalse(gameHandler.tearDownTower(x, y));
            }
        }
    }

    @Test
    void testTearDownTowerWhenPositionIsAvailable() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        var position = new Vector2(x, y);
                        if (gameHandler.getStartPortal().getPosition().equals(position)
                                || gameHandler.getEndPortal().getPosition().equals(position)) {
                            continue;
                        }

                        Assertions.assertFalse(gameHandler.tearDownTower(x, y));
                    }
                }
            }
        }
    }

    @Test
    void testTearDownTowerOutsideGrid() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);

                Assertions.assertFalse(gameHandler.tearDownTower(-1, 0));
                Assertions.assertFalse(gameHandler.tearDownTower(0, -1));
                Assertions.assertFalse(gameHandler.tearDownTower(-1, -1));
                Assertions.assertFalse(gameHandler.tearDownTower(MAX_GRID_WIDTH, 0));
                Assertions.assertFalse(gameHandler.tearDownTower(0, MAX_GRID_HEIGHT));
                Assertions.assertFalse(gameHandler.tearDownTower(MAX_GRID_WIDTH, MAX_GRID_HEIGHT));
            }
        }
    }

    @Test
    void testTearDownTowerOnPortals() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);

                Assertions.assertFalse(gameHandler.tearDownTower((int) gameHandler.getStartPortal().getPosition().x(),
                        (int) gameHandler.getStartPortal().getPosition().y()));
                Assertions.assertFalse(gameHandler.tearDownTower((int) gameHandler.getEndPortal().getPosition().x(),
                        (int) gameHandler.getEndPortal().getPosition().y()));
            }
        }
    }

    @Test
    void testTearDownTowerCreatingShorterPath() throws NoSuchFieldException, IllegalAccessException {
        for (int width = 5; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 5; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setSpice(gameHandler, Integer.MAX_VALUE);

                for (int x = 0; x < width - 1; x++) {
                    Assertions.assertTrue(gameHandler.buildTower(TowerEnum.GUARD_TOWER, x, 1));
                }
                for (int x = width - 1; x > 0; x--) {
                    Assertions.assertTrue(gameHandler.buildTower(TowerEnum.GUARD_TOWER, x, 3));
                }
                int pathLength = gameHandler.getPath().getLength();
                Assertions.assertTrue(gameHandler.tearDownTower(0, 1));
                Assertions.assertTrue(pathLength > gameHandler.getPath().getLength());
            }
        }
    }

    @Test
    void testTearDownTower() throws NoSuchFieldException, IllegalAccessException {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setSpice(gameHandler, Integer.MAX_VALUE);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        var position = new Vector2(x, y);
                        if (gameHandler.getStartPortal().getPosition().equals(position)
                                || gameHandler.getEndPortal().getPosition().equals(position)) {
                            continue;
                        }

                        var doBreak = false;
                        for (var wayPoint : gameHandler.getPath().getWaypoints()) {
                            if (wayPoint.equals(position)) {
                                doBreak = true;
                                break;
                            }
                        }
                        if (doBreak) {
                            break;
                        }

                        Assertions.assertTrue(gameHandler.buildTower(TowerEnum.GUARD_TOWER, x, y));
                        Assertions.assertNotNull(gameHandler.getGrid()[x][y]);
                        Assertions.assertTrue(gameHandler.tearDownTower(x, y));
                        Assertions.assertNull(gameHandler.getGrid()[x][y]);
                    }
                }
            }
        }
    }

    @Test
    void testUpdateUntilGameIsLost() throws NoSuchFieldException, IllegalAccessException {
        // deltaTimeInMilliseconds is ~ 60 FPS
        var deltaTimeInMilliseconds = 16.6f;
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setGameStarted(gameHandler);

                while (gameHandler.getGamePhase() == GamePhase.BUILD_PHASE) {
                    // Build phase
                    while (gameHandler.getGamePhase() == GamePhase.BUILD_PHASE) {
                        gameHandler.update(deltaTimeInMilliseconds);
                    }
                    Assertions.assertTrue(gameHandler.getRemainingBuildPhaseDurationInMilliseconds() <= 0);
                    Assertions.assertSame(GamePhase.WAVE_PHASE, gameHandler.getGamePhase());
                    Assertions.assertTrue(gameHandler.getNumberOfRemainingHostileUnits() > 0);
                    Assertions.assertTrue(gameHandler.isGameStarted());

                    // Wave phase
                    while (gameHandler.getGamePhase() == GamePhase.WAVE_PHASE) {
                        gameHandler.update(deltaTimeInMilliseconds);
                    }

                    if (gameHandler.getGamePhase() == GamePhase.BUILD_PHASE) {
                        Assertions.assertEquals(GAME_BUILD_PHASE_DURATION_IN_MS,
                                gameHandler.getRemainingBuildPhaseDurationInMilliseconds());
                        Assertions.assertSame(GamePhase.BUILD_PHASE, gameHandler.getGamePhase());
                    }
                }
                Assertions.assertSame(GamePhase.GAME_LOST_PHASE, gameHandler.getGamePhase());
                Assertions.assertTrue(gameHandler.isGamePaused());
            }
        }
    }

    // Achtung geht 20-30 Sekunden
    @Test
    void testUpdateUntilGameIsWon() throws NoSuchFieldException, IllegalAccessException {
        // deltaTimeInMilliseconds is ~ 60 FPS
        var deltaTimeInMilliseconds = 16.6f;
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setSpice(gameHandler, Integer.MAX_VALUE);
                setHealthToInfinity(gameHandler);

                for (int x = 0; x < width - 1; x++) {
                    Assertions.assertTrue(gameHandler.buildTower(TowerEnum.GUARD_TOWER, x, 1));
                    setTowerDamageToInfinity(gameHandler.getGrid()[x][1]);
                }

                while (gameHandler.getGamePhase() == GamePhase.BUILD_PHASE) {
                    // Build phase
                    while (gameHandler.getGamePhase() == GamePhase.BUILD_PHASE) {
                        gameHandler.update(deltaTimeInMilliseconds);
                    }
                    Assertions.assertTrue(gameHandler.getRemainingBuildPhaseDurationInMilliseconds() <= 0);
                    Assertions.assertSame(GamePhase.WAVE_PHASE, gameHandler.getGamePhase());
                    Assertions.assertTrue(gameHandler.getNumberOfRemainingHostileUnits() > 0);
                    Assertions.assertTrue(gameHandler.isGameStarted());

                    // Wave phase
                    while (gameHandler.getGamePhase() == GamePhase.WAVE_PHASE) {
                        gameHandler.update(deltaTimeInMilliseconds);
                    }

                    if (gameHandler.getGamePhase() == GamePhase.BUILD_PHASE) {
                        Assertions.assertEquals(GAME_BUILD_PHASE_DURATION_IN_MS,
                                gameHandler.getRemainingBuildPhaseDurationInMilliseconds());
                        Assertions.assertSame(GamePhase.BUILD_PHASE, gameHandler.getGamePhase());
                    }
                }
                Assertions.assertNotSame(GamePhase.GAME_LOST_PHASE, gameHandler.getGamePhase());
                Assertions.assertSame(GamePhase.GAME_WON_PHASE, gameHandler.getGamePhase());
                Assertions.assertTrue(gameHandler.isGamePaused());
                Assertions.assertEquals(0, gameHandler.getNumberOfRemainingHostileUnits());
            }
        }
    }

    void setSpice(@NonNull GameHandler gameHandler, int spice) throws NoSuchFieldException,
            IllegalAccessException {
        var field = GameHandler.class.getDeclaredField("playerSpice");
        field.setAccessible(true);
        field.set(gameHandler, spice);
    }

    void setPhase(@NonNull GameHandler gameHandler, @NonNull GamePhase phase) throws NoSuchFieldException,
            IllegalAccessException {
        var field = GameHandler.class.getDeclaredField("gamePhase");
        field.setAccessible(true);
        field.set(gameHandler, phase);
    }

    void setTowerDamageToInfinity(@NonNull Entity entity) throws NoSuchFieldException, IllegalAccessException {
        if (entity instanceof DamageTower tower) {
            var field = DamageTower.class.getDeclaredField("damage");
            field.setAccessible(true);
            field.set(tower, Integer.MAX_VALUE);
        }
    }

    void setHealthToInfinity(@NonNull GameHandler gameHandler) throws NoSuchFieldException, IllegalAccessException {
        var field = GameHandler.class.getDeclaredField("playerHealth");
        field.setAccessible(true);
        field.set(gameHandler, Integer.MAX_VALUE);
    }

    void setGameStarted(@NonNull GameHandler gameHandler) throws NoSuchFieldException, IllegalAccessException {
        var field = GameHandler.class.getDeclaredField("gameStarted");
        field.setAccessible(true);
        field.set(gameHandler, true);
    }
}
