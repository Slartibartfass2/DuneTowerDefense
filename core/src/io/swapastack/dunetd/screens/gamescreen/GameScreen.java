package io.swapastack.dunetd.screens.gamescreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.assets.controller.EntityController;
import io.swapastack.dunetd.assets.controller.HostileUnitController;
import io.swapastack.dunetd.assets.controller.ShaiHuludController;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.towers.Tower;
import io.swapastack.dunetd.game.GameHandler;
import io.swapastack.dunetd.game.GamePhase;
import io.swapastack.dunetd.hud.Hud;
import io.swapastack.dunetd.math.DuneTDMath;
import io.swapastack.dunetd.screens.AbstractScreen;
import io.swapastack.dunetd.vectors.Vector2;
import io.swapastack.dunetd.vectors.Vector3;
import io.swapastack.dunetd.screens.ScreenColor;

import lombok.NonNull;
import lombok.Setter;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

/**
 * The GameScreen class.
 */
public final class GameScreen extends AbstractScreen {

    private static final Color SELECTION_COLOR = new Color(0, 0, 0, 0.5f);
    private static final Color THUMPER_OUTER_COLOR = new Color(0, 0, 0, 0.5f);
    private static final Color THUMPER_INNER_COLOR = new Color(1, 1, 1, 0.3f);
    private static final float NEAR_PLANE_DISTANCE = 0.1f;
    private static final float FAR_PLANE_DISTANCE = 300f;
    private static final int MAX_BONES = 128;
    private static final int CIRCLE_RENDER_SEGMENTS = 50;
    private static final float THUMPER_X_OFFSET = 0.07f;
    private static final float THUMPER_Y_OFFSET = 0.25f;
    private static final float THUMPER_OUTER_RADIUS = 0.5f;
    private static final float THUMPER_INNER_RADIUS = 0.3f;
    private static final int ENVIRONMENT_MAP_BASE_SIZE = 1024;
    private static final int CAMERA_FIELD_OF_VIEW = 60;
    private static final int IRRADIANCE_MAP_BASE_SIZE = 256;
    private static final int RADIANCE_MIP_MAP_LEVELS = 10;

    private final GameHandler gameHandler;
    private final EntityController entityController;
    private final HostileUnitController hostileUnitController;
    private final ShaiHuludController shaiHuludController;

    // Grid
    private final int gridWidth;
    private final int gridHeight;
    private final Entity[][] grid;
    private GameGround gameGround;

    // GDX GLTF
    private final SceneManager sceneManager;
    private final SceneManager groundSceneManager;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private SceneSkybox skybox;
    private final ShapeRenderer shapeRenderer;

    // libGDX
    private PerspectiveCamera camera;

    // HUD
    private final Hud hud;
    @Setter
    private boolean freezeInput;

    public GameScreen(@NonNull DuneTD game) {
        super(game, ScreenColor.BLACK);

        // Load game settings
        gridWidth = getSettings().getGridWidth();
        gridHeight = getSettings().getGridHeight();

        // GDX GLTF - Scene Manager
        sceneManager = new SceneManager(MAX_BONES);
        groundSceneManager = new SceneManager(MAX_BONES);
        shapeRenderer = new ShapeRenderer();

        entityController = new EntityController(sceneManager, game.getAssetLoader());
        hostileUnitController = new HostileUnitController(sceneManager, game.getAssetLoader());
        shaiHuludController = new ShaiHuludController(sceneManager, game.getAssetLoader());

        // Initialise GameHandler
        gameHandler = new GameHandler(gridWidth, gridHeight, entityController, hostileUnitController,
                shaiHuludController);
        grid = gameHandler.getGrid();

        hud = new Hud(this, gameHandler, getStage());
        freezeInput = false;
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void showScreen() {
        setUpEnvironment();

        var cameraFocusPosition = new Vector3(gridWidth / 2f, 0, gridHeight / 2f);
        var cameraPosition = new Vector3(gridWidth / 2f, Math.max(gridHeight, gridWidth) * 0.85f, 0);

        // Camera
        camera = new PerspectiveCamera(CAMERA_FIELD_OF_VIEW, stage.getWidth(), stage.getHeight());
        camera.position.set(cameraPosition.toLibGdx());
        camera.lookAt(cameraFocusPosition.toLibGdx());
        camera.near = NEAR_PLANE_DISTANCE;
        camera.far = FAR_PLANE_DISTANCE;

        sceneManager.setCamera(camera);
        groundSceneManager.setCamera(camera);

        hud.create(cameraFocusPosition);

        var inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(hud.getEscapeMenuStage());
        inputMultiplexer.addProcessor(getStage());
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Create ground tiles
        gameGround = new GameGround(gridWidth, gridHeight, groundSceneManager, getGame().getAssetLoader());
    }

    private void setUpEnvironment() {
        // GDX GLTF - Light
        var light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);
        groundSceneManager.environment.add(light);

        // GDX GLTF - Image Based Lighting
        var iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(ENVIRONMENT_MAP_BASE_SIZE);
        diffuseCubemap = iblBuilder.buildIrradianceMap(IRRADIANCE_MAP_BASE_SIZE);
        specularCubemap = iblBuilder.buildRadianceMap(RADIANCE_MIP_MAP_LEVELS);
        iblBuilder.dispose();

        // GDX GLTF - This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        // GDX GLTF - Cubemaps
        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));
        groundSceneManager.setAmbientLight(1f);
        groundSceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        groundSceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        groundSceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // GDX GLTF - Skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param deltaTimeInSeconds The time in seconds since the last render.
     */
    @Override
    public void renderScreen(float deltaTimeInSeconds) {
        var deltaInMilliseconds = deltaTimeInSeconds * 1000;
        // Run game logic
        gameHandler.update(deltaInMilliseconds);
        gameGround.updatePath(gameHandler.getPath());

        // Display game lost or game won window when phase is reached
        if (gameHandler.getGamePhase() == GamePhase.GAME_WON_PHASE) {
            hud.showGameWonWindow();
            freezeInput = true;
        } else if (gameHandler.getGamePhase() == GamePhase.GAME_LOST_PHASE) {
            hud.showGameLostWindow();
            freezeInput = true;
        }

        camera.update();

        var selectedTile3d = getSelectedTile();
        var selectedTile2d = new Vector2(selectedTile3d.x(), selectedTile3d.z());

        // Render ground tiles
        groundSceneManager.update(deltaTimeInSeconds);
        groundSceneManager.render();

        // Set up shape renderer
        var correctedProjectionMatrix = camera.combined.cpy().rotate(com.badlogic.gdx.math.Vector3.X, 90);
        shapeRenderer.setProjectionMatrix(correctedProjectionMatrix);
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw mouse selection (selected tile or selected tower
        if (!freezeInput) {
            drawMouseSelection((int) selectedTile2d.x(), (int) selectedTile2d.y());
        }

        // Draw thumpers
        var firstThumper = gameHandler.getShaiHulud().getFirstThumper();
        if (firstThumper != null) {
            drawThumper(firstThumper, true);
        }
        var secondThumper = gameHandler.getShaiHulud().getSecondThumper();
        if (secondThumper != null) {
            drawThumper(secondThumper, false);
        }

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Render towers, portals, hostile units and shai hulud
        sceneManager.update(deltaTimeInSeconds);
        sceneManager.render();

        // Render HUD above all
        hud.update(deltaTimeInSeconds, selectedTile2d, camera);
    }

    private void drawMouseSelection(int x, int y) {
        if (!DuneTDMath.isPositionInsideGrid(grid, x, y) || hud.isMouseOnToolBar()) {
            return;
        }

        shapeRenderer.setColor(SELECTION_COLOR);
        if (grid[x][y] instanceof Tower tower) {
            shapeRenderer.circle(x, y, tower.getRange(), CIRCLE_RENDER_SEGMENTS);
        } else {
            shapeRenderer.rect(x - 0.5f, y - 0.5f, 1, 1);
        }
    }

    private void drawThumper(@NonNull Vector2 position, boolean first) {
        // Draw outer circle
        shapeRenderer.setColor(THUMPER_OUTER_COLOR);
        shapeRenderer.circle(position.x(), position.y(), THUMPER_OUTER_RADIUS, CIRCLE_RENDER_SEGMENTS);

        // Draw inner circle
        shapeRenderer.setColor(THUMPER_INNER_COLOR);
        shapeRenderer.circle(position.x(), position.y(), THUMPER_INNER_RADIUS, CIRCLE_RENDER_SEGMENTS);

        // Draw a one or a two, indicating which thumper it is
        shapeRenderer.setColor(Color.BLACK);
        if (first) {
            shapeRenderer.rectLine(position.x(), position.y() - THUMPER_Y_OFFSET, position.x(),
                    position.y() + THUMPER_Y_OFFSET, NEAR_PLANE_DISTANCE);
        } else {
            shapeRenderer.rectLine(position.x() - THUMPER_X_OFFSET, position.y() - THUMPER_Y_OFFSET,
                    position.x() - THUMPER_X_OFFSET, position.y() + THUMPER_Y_OFFSET, NEAR_PLANE_DISTANCE);
            shapeRenderer.rectLine(position.x() + THUMPER_X_OFFSET, position.y() - THUMPER_Y_OFFSET,
                    position.x() + THUMPER_X_OFFSET, position.y() + THUMPER_Y_OFFSET, NEAR_PLANE_DISTANCE);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        sceneManager.updateViewport(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        sceneManager.dispose();
        groundSceneManager.dispose();
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();
        entityController.dispose();
        hostileUnitController.dispose();
        shaiHuludController.dispose();
        hud.dispose();
    }

    public Vector3 getSelectedTile() {
        // Use Raytracing to select tile on grid
        var ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());
        var factor = ray.origin.y / ray.direction.y;
        // scale direction by factor and sub it from origin to hit spot on grid where y is 0
        ray.origin.sub(ray.direction.scl(factor));
        return new Vector3(Math.round(ray.origin.x), 0f, Math.round(ray.origin.z));
    }
}
