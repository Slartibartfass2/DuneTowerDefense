package io.swapastack.dunetd.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.swapastack.dunetd.assets.AssetLoader;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.game.GameHandler;
import io.swapastack.dunetd.game.GamePhase;
import io.swapastack.dunetd.screens.gamescreen.EscapeMenu;
import io.swapastack.dunetd.screens.gamescreen.GameEndWindow;
import io.swapastack.dunetd.screens.gamescreen.GameScreen;
import io.swapastack.dunetd.screens.listeners.ClickInputListener;
import io.swapastack.dunetd.screens.listeners.HudInputListener;
import io.swapastack.dunetd.shaihulud.ShaiHulud;
import io.swapastack.dunetd.vectors.Vector2;
import io.swapastack.dunetd.vectors.Vector3;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import lombok.Getter;
import lombok.NonNull;

public final class Hud implements Disposable {

    // Constants
    private static final int SHAI_HULUD_COOLDOWN_IN_MS = Configuration.getInstance()
            .getIntProperty("SHAI_HULUD_COOLDOWN_IN_MILLISECONDS");

    private static final float CAMERA_ROTATION_ANGLE = 0.8f;

    private static final float CAMERA_MOVEMENT_SPEED = 0.3f;

    private static final float CAMERA_BORDER_OFFSET = 4f;

    private static final com.badlogic.gdx.math.Vector3 Y_AXIS = new com.badlogic.gdx.math.Vector3(0f, 1f, 0f);

    private static final Color ESCAPE_MENU_BACKGROUND_COLOR = new Color(0, 0, 0, 0.6f);

    private static final Color REMAINING_COOLDOWN_COLOR = new Color(0, 0, 0, 0.5f);

    private final GameScreen screen;

    private final GameHandler gameHandler;

    private final ShapeRenderer shapeRenderer;

    private final SpriteBatch spriteBatch;

    private final Stage mainStage;

    // Escape Menu
    @Getter
    private final Stage escapeMenuStage;

    private EscapeMenu escapeMenu;

    @Getter
    private boolean escapeMenuVisible;

    // Toolbar
    private ToolBar toolBar;

    // End screens
    private GameEndWindow gameWonWindow;

    private GameEndWindow gameLostWindow;

    private final ShaiHulud shaiHulud;

    // Camera
    private Vector3 cameraFocusPosition;

    private Vector3 cameraFacingDirection;

    private int zoomFactor = 5;

    // Debugging
    private Vector2 selectedTilePosition;

    private String logText = "";

    public Hud(@NonNull GameScreen screen, @NonNull GameHandler gameHandler, @NonNull Stage mainStage) {
        this.screen = screen;
        this.gameHandler = gameHandler;
        selectedTilePosition = Vector2.ZERO;
        spriteBatch = new SpriteBatch();
        this.mainStage = mainStage;
        shapeRenderer = new ShapeRenderer();
        escapeMenuStage = new Stage(new ScreenViewport());
        escapeMenuVisible = false;
        cameraFacingDirection = Vector3.ZERO;
        shaiHulud = gameHandler.getShaiHulud();
    }

    public void create(@NonNull Vector3 initialCameraFocusPosition) {
        this.cameraFocusPosition = initialCameraFocusPosition;

        // Escape menu added to extra stage
        escapeMenu = new EscapeMenu(screen, new ClickInputListener(this::switchEscapeMenuVisibility));
        escapeMenuStage.addActor(escapeMenu);

        // In the top center of the screen game phase and progress bar
        var gamePhaseBar = new GamePhaseBar(gameHandler);
        mainStage.addActor(gamePhaseBar);

        // In the bottom center of the screen spice, health and towers are displayed
        var background = screen.getGame().getAssetLoader().getDrawable(AssetLoader.DRAWABLE_BACKGROUND_NAME);
        var guardTowerDrawable = screen.getGame().getAssetLoader().getDrawable(AssetLoader.DRAWABLE_GUARD_TOWER_NAME);
        var bombTowerDrawable = screen.getGame().getAssetLoader().getDrawable(AssetLoader.DRAWABLE_BOMB_TOWER_NAME);
        var soundTowerDrawable = screen.getGame().getAssetLoader().getDrawable(AssetLoader.DRAWABLE_SOUND_TOWER_NAME);
        var shaiHuludDrawable = screen.getGame().getAssetLoader().getDrawable(AssetLoader.DRAWABLE_SHAI_HULUD_NAME);
        toolBar = new ToolBar(gameHandler, background, guardTowerDrawable, bombTowerDrawable, soundTowerDrawable,
                shaiHuludDrawable);
        mainStage.addActor(toolBar);

        // Add Listeners to Stage
        mainStage.addListener(
                new HudInputListener(this::handleMouseClickEvent, this::handleScrollEvent, this::handleKeyDownEvent));
    }

    public void update(float deltaTimeInSeconds, @NonNull Vector2 mousePos, @NonNull PerspectiveCamera camera) {
        // Update facing direction of the camera
        var cameraPositionFlat = new Vector3(camera.position.x, 0f, camera.position.z);
        var cameraDifferenceToFocusPoint = Vector3.subtract(cameraFocusPosition, cameraPositionFlat);
        cameraFacingDirection = cameraDifferenceToFocusPoint.normalize();

        // Check if user presses camera control keys and update the camera position and where it's looking at
        checkCameraControls(camera);
        camera.lookAt(cameraFocusPosition.toLibGdx());

        selectedTilePosition = mousePos;

        // Make background darker
        if (escapeMenuVisible) {
            mainStage.draw();

            // Make whole screen darker
            drawDarkBackground();
        } else {
            mainStage.act(deltaTimeInSeconds);
            mainStage.draw();

            // Draw Cooldown on top of button
            if (shaiHulud.getRemainingCooldownInMilliseconds() > 0) {
                var x = toolBar.getShaiHuludButton().getCenterX() + toolBar.getX();
                var y = toolBar.getShaiHuludButton().getCenterY() + toolBar.getY();
                var radius = toolBar.getShaiHuludButton().getButtonRadius() / 2f;
                var degrees = shaiHulud.getRemainingCooldownInMilliseconds() / (float) SHAI_HULUD_COOLDOWN_IN_MS * 360f;
                Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                shapeRenderer.setColor(REMAINING_COOLDOWN_COLOR);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.arc(x, y, radius, 90f, degrees, 50);
                shapeRenderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
            }
        }

        escapeMenuStage.act(deltaTimeInSeconds);
        escapeMenuStage.draw();
    }

    private void drawDarkBackground() {
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setColor(ESCAPE_MENU_BACKGROUND_COLOR);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void debug(@NonNull PerspectiveCamera camera) {
        ImGui.begin("HUD", ImGuiWindowFlags.AlwaysAutoResize);
        ImGui.text("Selected Tower: " + toolBar.getSelectedTower());
        ImGui.text("Shai Hulud selected: " + toolBar.isShaiHuludSelected());
        ImGui.text(String.format("Selected Tile: %2.0f, %2.0f", selectedTilePosition.x(), selectedTilePosition.y()));
        ImGui.text(String.format("CamFacDir: %3.3f, %3.3f, %3.3f", cameraFacingDirection.x(), cameraFacingDirection.y(),
                cameraFacingDirection.z()));
        ImGui.text(String.format("CamPos: %3.3f, %3.3f, %3.3f", camera.position.x, camera.position.y,
                camera.position.z));
        ImGui.text(String.format("CamFocPos: %3.3f, %3.3f, %3.3f", cameraFocusPosition.x(), cameraFocusPosition.y(),
                cameraFocusPosition.z()));
        ImGui.text(String.format("%d", zoomFactor));
        ImGui.text(logText);
        ImGui.end();
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        spriteBatch.dispose();
        escapeMenuStage.dispose();
    }

    private void handleMouseClickEvent(int button) {
        if (escapeMenuVisible || toolBar.isMouseOnToolBar()) {
            return;
        }

        int selectedX = (int) selectedTilePosition.x();
        int selectedY = (int) selectedTilePosition.y();

        switch (button) {
            // Build tower
            case Input.Buttons.LEFT -> {
                var selectedTower = toolBar.getSelectedTower();
                if (selectedTower != null) {
                    gameHandler.buildTower(selectedTower, selectedX, selectedY);
                } else if (toolBar.isShaiHuludSelected() && gameHandler.getGamePhase() == GamePhase.WAVE_PHASE) {
                    shaiHulud.setThumper(selectedTilePosition);
                }
            }
            // Tear down tower
            case Input.Buttons.RIGHT -> {
                if (toolBar.isShaiHuludSelected()) {
                    shaiHulud.cancelAttack();
                    toolBar.selectShaiHulud();
                } else {
                    gameHandler.tearDownTower(selectedX, selectedY);
                }
            }
            default -> { /* Do nothing when nothing is pressed */ }
        }
    }

    private void handleScrollEvent(int amount) {
        if (escapeMenuVisible) {
            return;
        }

        // TODO: Zoom
    }

    private void handleKeyDownEvent(int keycode) {
        if (!escapeMenuVisible) {
            switch (keycode) {
                case Input.Keys.SPACE -> gameHandler.pauseGame(!gameHandler.isGamePaused());
                case Input.Keys.L -> gameHandler.skipBuildPhase();
                case Input.Keys.UP -> gameHandler.speedUpTime();
                case Input.Keys.DOWN -> gameHandler.slowDownTime();
                default -> { /* Do nothing when nothing is pressed */ }
            }
        }

        if (keycode == Input.Keys.ESCAPE) {
            switchEscapeMenuVisibility();
        }
    }

    /**
     * Checks keys for controlling the camera.
     * Q and E for rotating.
     * WASD for moving.
     */
    private void checkCameraControls(@NonNull PerspectiveCamera camera) {
        if (escapeMenuVisible) {
            return;
        }

        // Rotate camera by pressing Q for clockwise and E for counterclockwise
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.rotateAround(cameraFocusPosition.toLibGdx(), Y_AXIS, -CAMERA_ROTATION_ANGLE);
        } else if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotateAround(cameraFocusPosition.toLibGdx(), Y_AXIS, CAMERA_ROTATION_ANGLE);
        }

        // Vector indicates in which direction the camera should move
        // User can control the movement with WASD
        var cameraMovementIndicator = getCameraMovement();

        // Angle to which camera should move
        var movementAngle = -cameraMovementIndicator.getAngleInDegrees();
        // Distance of camera movement
        var movingDistance = cameraMovementIndicator.isZero() ? 0f : CAMERA_MOVEMENT_SPEED;

        // Vector to add on to camera position and focus position when moving
        var cameraFacingDirectionScaled = Vector3.multiply(cameraFacingDirection, movingDistance / zoomFactor);
        var cameraFacingDirectionLibGdx = cameraFacingDirectionScaled.toLibGdx();
        // TODO: write own rotate logic
        var cameraPositionChangeLibGdx = cameraFacingDirectionLibGdx.rotate(Y_AXIS, movementAngle);

        var newX = camera.position.x + cameraPositionChangeLibGdx.x;
        var newZ = camera.position.z + cameraPositionChangeLibGdx.z;

        // Restrain camera position to keep camera around the game field
        cameraPositionChangeLibGdx = getCameraPositionChange(camera, cameraPositionChangeLibGdx, newX, newZ);

        // Move camera if cameraPositionChange is not Vector3.ZERO
        camera.position.add(cameraPositionChangeLibGdx);
        var cameraPositionChange = Vector3.fromLibGdx(cameraFacingDirectionLibGdx);
        cameraFocusPosition = Vector3.add(cameraFocusPosition, cameraPositionChange);
    }

    private com.badlogic.gdx.math.Vector3 getCameraPositionChange(
            @NonNull PerspectiveCamera camera,
            @NonNull com.badlogic.gdx.math.Vector3 cameraPositionChange,
            float newX,
            float newZ) {
        float x;
        float z;

        if (newX < -CAMERA_BORDER_OFFSET) {
            x = -CAMERA_BORDER_OFFSET - camera.position.x;
        } else if (newX > gameHandler.getGridWidth() + CAMERA_BORDER_OFFSET) {
            x = gameHandler.getGridWidth() + CAMERA_BORDER_OFFSET - camera.position.x;
        } else {
            x = cameraPositionChange.x;
        }

        if (newZ < -CAMERA_BORDER_OFFSET) {
            z = -CAMERA_BORDER_OFFSET - camera.position.z;
        } else if (newZ > gameHandler.getGridHeight() + CAMERA_BORDER_OFFSET) {
            z = gameHandler.getGridHeight() + CAMERA_BORDER_OFFSET - camera.position.z;
        } else {
            z = cameraPositionChange.z;
        }

        return new com.badlogic.gdx.math.Vector3(x, 0f, z);
    }

    private Vector2 getCameraMovement() {
        float x;
        float y;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            x = 1f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            x = -1f;
        } else {
            x = 0f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            y = 1f;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            y = -1f;
        } else {
            y = 0f;
        }

        return new Vector2(x, y);
    }

    private void switchEscapeMenuVisibility() {
        // Don't show escape menu when game end windows are shown
        if (gameLostWindow != null || gameWonWindow != null) {
            return;
        }

        escapeMenuVisible ^= true;
        screen.setFreezeInput(escapeMenuVisible);
        gameHandler.pauseGame(escapeMenuVisible);
        escapeMenu.setVisible(escapeMenuVisible);
        toolBar.setFrozenInput(escapeMenuVisible);
    }

    public void showGameWonWindow() {
        if (gameWonWindow != null) {
            return;
        }
        if (escapeMenuVisible) {
            switchEscapeMenuVisibility();
        }
        gameWonWindow = new GameEndWindow("Game won", screen, gameHandler.getStatistics());
        mainStage.addActor(gameWonWindow);
        toolBar.setVisible(false);
    }

    public void showGameLostWindow() {
        if (gameLostWindow != null) {
            return;
        }
        if (escapeMenuVisible) {
            switchEscapeMenuVisibility();
        }
        gameLostWindow = new GameEndWindow("Game over", screen, gameHandler.getStatistics());
        mainStage.addActor(gameLostWindow);
        toolBar.setVisible(false);
    }

    public boolean isMouseOnToolBar() {
        return toolBar.isMouseOnToolBar();
    }
}
