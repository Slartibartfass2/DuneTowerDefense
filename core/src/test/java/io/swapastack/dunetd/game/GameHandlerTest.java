package io.swapastack.dunetd.game;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.TestHelper;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.DamageTower;
import lombok.NonNull;
import org.junit.jupiter.api.Test;

import static io.swapastack.dunetd.entities.towers.TowerEnum.GUARD_TOWER;
import static io.swapastack.dunetd.game.GamePhase.BUILD_PHASE;
import static io.swapastack.dunetd.game.GamePhase.GAME_LOST_PHASE;
import static io.swapastack.dunetd.game.GamePhase.GAME_WON_PHASE;
import static io.swapastack.dunetd.game.GamePhase.WAVE_PHASE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameHandlerTest {

    static {
        TestHelper.readConfigFile();
    }

    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");
    private static final int GAME_BUILD_PHASE_DURATION_IN_MS = Configuration.getInstance()
            .getIntProperty("GAME_BUILD_PHASE_DURATION_IN_MS");

    @Test
    void testBuildTowerWithEndlessSpice() throws NoSuchFieldException, IllegalAccessException {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setSpice(gameHandler, Integer.MAX_VALUE);

                for (int x = 0; x < width; x++) {
                    loop:
                    for (int y = 0; y < height; y++) {
                        var position = new Vector2(x, y);
                        if (gameHandler.getStartPortal().getGridPosition2d().equals(position)
                                || gameHandler.getEndPortal().getGridPosition2d().equals(position)) {
                            assertFalse(gameHandler.buildTower(GUARD_TOWER, x, y));
                            continue;
                        }
                        for (var wayPoint : gameHandler.getPath().getWaypoints()) {
                            if (wayPoint.equals(position)) {
                                // could be true or false depending on if there's another path
                                continue loop;
                            }
                        }

                        assertTrue(gameHandler.buildTower(GUARD_TOWER, x, y));
                        assertNotNull(gameHandler.getGrid()[x][y]);
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
                        assertFalse(gameHandler.buildTower(GUARD_TOWER, x, y));
                    }
                }
            }
        }
    }

    @Test
    void testBuildTowerInEveryPhaseButBuildPhase() throws NoSuchFieldException, IllegalAccessException {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setPhase(gameHandler, WAVE_PHASE);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        assertFalse(gameHandler.buildTower(GUARD_TOWER, x, y));
                    }
                }
            }
        }

        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setPhase(gameHandler, GAME_LOST_PHASE);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        assertFalse(gameHandler.buildTower(GUARD_TOWER, x, y));
                    }
                }
            }
        }

        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setPhase(gameHandler, GAME_WON_PHASE);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        assertFalse(gameHandler.buildTower(GUARD_TOWER, x, y));
                    }
                }
            }
        }
    }

    @Test
    void testBuildTowerOutsideGrid() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);

                assertFalse(gameHandler.buildTower(GUARD_TOWER, -1, 0));
                assertFalse(gameHandler.buildTower(GUARD_TOWER, 0, -1));
                assertFalse(gameHandler.buildTower(GUARD_TOWER, -1, -1));
                assertFalse(gameHandler.buildTower(GUARD_TOWER, MAX_GRID_WIDTH, 0));
                assertFalse(gameHandler.buildTower(GUARD_TOWER, 0, MAX_GRID_HEIGHT));
                assertFalse(gameHandler.buildTower(GUARD_TOWER, MAX_GRID_WIDTH, MAX_GRID_HEIGHT));
            }
        }
    }

    @Test
    void testBuildTowerPathBlocking() throws NoSuchFieldException, IllegalAccessException {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setSpice(gameHandler, Integer.MAX_VALUE);

                assertTrue(gameHandler.buildTower(GUARD_TOWER, 0, 1));
                assertNotNull(gameHandler.getGrid()[0][1]);
                assertFalse(gameHandler.buildTower(GUARD_TOWER, 1, 0));
            }
        }
    }

    @Test
    void testTearDownTowerInEveryPhaseButBuildPhase() throws NoSuchFieldException, IllegalAccessException {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setPhase(gameHandler, WAVE_PHASE);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        assertFalse(gameHandler.tearDownTower(x, y));
                    }
                }
            }
        }

        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setPhase(gameHandler, GAME_LOST_PHASE);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        assertFalse(gameHandler.tearDownTower(x, y));
                    }
                }
            }
        }

        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setPhase(gameHandler, GAME_WON_PHASE);

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        assertFalse(gameHandler.tearDownTower(x, y));
                    }
                }
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
                        if (gameHandler.getStartPortal().getGridPosition2d().equals(position)
                                || gameHandler.getEndPortal().getGridPosition2d().equals(position)) {
                            continue;
                        }

                        assertFalse(gameHandler.tearDownTower(x, y));
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

                assertFalse(gameHandler.tearDownTower(-1, 0));
                assertFalse(gameHandler.tearDownTower(0, -1));
                assertFalse(gameHandler.tearDownTower(-1, -1));
                assertFalse(gameHandler.tearDownTower(MAX_GRID_WIDTH, 0));
                assertFalse(gameHandler.tearDownTower(0, MAX_GRID_HEIGHT));
                assertFalse(gameHandler.tearDownTower(MAX_GRID_WIDTH, MAX_GRID_HEIGHT));
            }
        }
    }

    @Test
    void testTearDownTowerOnPortals() {
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);

                assertFalse(gameHandler.tearDownTower(gameHandler.getStartPortal().getX(),
                        gameHandler.getStartPortal().getY()));
                assertFalse(gameHandler.tearDownTower(gameHandler.getEndPortal().getX(),
                        gameHandler.getEndPortal().getY()));
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
                    assertTrue(gameHandler.buildTower(GUARD_TOWER, x, 1));
                }
                for (int x = width - 1; x > 0; x--) {
                    assertTrue(gameHandler.buildTower(GUARD_TOWER, x, 3));
                }
                int pathLength = gameHandler.getPath().getLength();
                assertTrue(gameHandler.tearDownTower(0, 1));
                assertTrue(pathLength > gameHandler.getPath().getLength());
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
                    loop:
                    for (int y = 0; y < height; y++) {
                        var position = new Vector2(x, y);
                        if (gameHandler.getStartPortal().getGridPosition2d().equals(position)
                                || gameHandler.getEndPortal().getGridPosition2d().equals(position)) {
                            continue;
                        }
                        for (var wayPoint : gameHandler.getPath().getWaypoints()) {
                            if (wayPoint.equals(position)) {
                                continue loop;
                            }
                        }

                        assertTrue(gameHandler.buildTower(GUARD_TOWER, x, y));
                        assertNotNull(gameHandler.getGrid()[x][y]);
                        assertTrue(gameHandler.tearDownTower(x, y));
                        assertNull(gameHandler.getGrid()[x][y]);
                    }
                }
            }
        }
    }

    @Test
    void testUpdateUntilGameIsLost() throws NoSuchFieldException, IllegalAccessException {
        // deltaTime is ~ 60 FPS
        var deltaTime = 0.0166f;
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setGameStarted(gameHandler);

                while (gameHandler.getGamePhase() == BUILD_PHASE) {
                    // Build phase
                    while (gameHandler.getGamePhase() == BUILD_PHASE) {
                        gameHandler.update(deltaTime);
                    }
                    assertTrue(gameHandler.getRemainingBuildPhaseDurationInMs() <= 0);
                    assertSame(WAVE_PHASE, gameHandler.getGamePhase());
                    assertTrue(gameHandler.getNumberOfRemainingHostileUnits() > 0);
                    assertTrue(gameHandler.isGameStarted());

                    // Wave phase
                    while (gameHandler.getGamePhase() == WAVE_PHASE) {
                        gameHandler.update(deltaTime);
                    }

                    if (gameHandler.getGamePhase() == BUILD_PHASE) {
                        assertEquals(GAME_BUILD_PHASE_DURATION_IN_MS, gameHandler.getRemainingBuildPhaseDurationInMs());
                        assertSame(BUILD_PHASE, gameHandler.getGamePhase());
                    }
                }
                assertSame(GAME_LOST_PHASE, gameHandler.getGamePhase());
                assertTrue(gameHandler.isGamePaused());
            }
        }
    }

    // Achtung geht 20-30 Sekunden
    @Test
    void testUpdateUntilGameIsWon() throws NoSuchFieldException, IllegalAccessException {
        // deltaTime is ~ 60 FPS
        var deltaTime = 0.0166f;
        for (int width = 2; width <= MAX_GRID_WIDTH; width++) {
            for (int height = 2; height <= MAX_GRID_HEIGHT; height++) {
                var gameHandler = new GameHandler(width, height);
                setSpice(gameHandler, Integer.MAX_VALUE);
                setHealthToInfinity(gameHandler);

                for (int x = 0; x < width - 1; x++) {
                    assertTrue(gameHandler.buildTower(GUARD_TOWER, x, 1));
                    setTowerDamageToInfinity(gameHandler.getGrid()[x][1]);
                }

                while (gameHandler.getGamePhase() == BUILD_PHASE) {
                    // Build phase
                    while (gameHandler.getGamePhase() == BUILD_PHASE) {
                        gameHandler.update(deltaTime);
                    }
                    assertTrue(gameHandler.getRemainingBuildPhaseDurationInMs() <= 0);
                    assertSame(WAVE_PHASE, gameHandler.getGamePhase());
                    assertTrue(gameHandler.getNumberOfRemainingHostileUnits() > 0);
                    assertTrue(gameHandler.isGameStarted());

                    // Wave phase
                    while (gameHandler.getGamePhase() == WAVE_PHASE) {
                        gameHandler.update(deltaTime);
                    }

                    if (gameHandler.getGamePhase() == BUILD_PHASE) {
                        assertEquals(GAME_BUILD_PHASE_DURATION_IN_MS, gameHandler.getRemainingBuildPhaseDurationInMs());
                        assertSame(BUILD_PHASE, gameHandler.getGamePhase());
                    }
                }
                assertNotSame(GAME_LOST_PHASE, gameHandler.getGamePhase());
                assertSame(GAME_WON_PHASE, gameHandler.getGamePhase());
                assertTrue(gameHandler.isGamePaused());
                assertEquals(0, gameHandler.getNumberOfRemainingHostileUnits());
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
