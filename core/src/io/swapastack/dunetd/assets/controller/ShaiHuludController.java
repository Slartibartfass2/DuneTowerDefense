package io.swapastack.dunetd.assets.controller;

import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.assets.AssetLoader;
import io.swapastack.dunetd.assets.GameModelSingle;
import io.swapastack.dunetd.game.GameModelData;
import io.swapastack.dunetd.shaihulud.ShaiHulud;
import lombok.NonNull;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public final class ShaiHuludController implements PropertyChangeListener {

    // Event names
    public static final String CREATE_EVENT_NAME = "create";
    public static final String SHOW_EVENT_NAME = "show";
    public static final String UPDATE_EVENT_NAME = "update";
    public static final String VANISH_EVENT_NAME = "vanish";

    // Exception messages
    private static final String SHAI_HULUD_ALREADY_INITIALIZED_MESSAGE = "There can only be one shai hulud";
    private static final String GAME_MODEL_MISSING_MESSAGE = "The game model has not been initialized";

    private final SceneManager sceneManager;
    private final AssetLoader assetLoader;
    private ShaiHulud shaiHulud;
    private GameModelSingle gameModel;

    public ShaiHuludController(@NonNull SceneManager sceneManager, @NonNull AssetLoader assetLoader) {
        this.sceneManager = sceneManager;
        this.assetLoader = assetLoader;
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param event A PropertyChangeEvent object describing the event source and the property that has changed.
     * @throws IllegalArgumentException If the event has illegal properties
     */
    @Override
    public void propertyChange(@NonNull PropertyChangeEvent event) throws IllegalArgumentException {
        if (event.getPropertyName() == null || event.getSource() == null
                || !(event.getSource() instanceof ShaiHulud shaiHuludSource)) {
            throw new IllegalArgumentException("Property name and source must be non null and source must be a shai " +
                    "hulud");
        }

        switch (event.getPropertyName()) {
            // Event when shai hulud is created
            case CREATE_EVENT_NAME -> handleCreateEvent(shaiHuludSource);
            // Event when shai hulud is displayed on the screen
            case SHOW_EVENT_NAME -> handleShowEvent(event.getNewValue(), shaiHuludSource);
            // Event whenever shai hulud is moved
            case UPDATE_EVENT_NAME -> handleUpdateEvent(event.getNewValue(), shaiHuludSource);
            // Event when shai hulud disappears from the screen
            case VANISH_EVENT_NAME -> handleVanishEvent(shaiHuludSource);
            default -> throw new IllegalStateException("Unexpected event name: " + event.getPropertyName());
        }
    }

    private void handleCreateEvent(@NonNull ShaiHulud shaiHulud) {
        // Create shai hulud and add game model
        this.shaiHulud = shaiHulud;
        gameModel = assetLoader.getShaiHuludGameModel();
    }

    @SuppressWarnings("DuplicatedCode")
    private void handleShowEvent(@NonNull Object newValue, @NonNull ShaiHulud shaiHulud)
            throws IllegalArgumentException, IllegalStateException {
        if (!(newValue instanceof GameModelData shaiHuludModelData)) {
            throw new IllegalArgumentException("newValue must be a GameModelData object");
        }
        if (this.shaiHulud != shaiHulud) {
            throw new IllegalStateException(SHAI_HULUD_ALREADY_INITIALIZED_MESSAGE);
        }
        if (gameModel == null) {
            throw new IllegalStateException(GAME_MODEL_MISSING_MESSAGE);
        }

        // Add scene of game model to scene manager and bring shai hulud in its initial position and rotation
        var newGameModelPosition = new Vector3(shaiHuludModelData.position().x, 0f, shaiHuludModelData.position().y);
        gameModel.rePositionAndRotate(newGameModelPosition, shaiHuludModelData.rotation());
        sceneManager.addScene(gameModel.getScene());
    }

    @SuppressWarnings("DuplicatedCode")
    private void handleUpdateEvent(@NonNull Object newValue, @NonNull ShaiHulud shaiHulud)
            throws IllegalArgumentException, IllegalStateException {
        if (!(newValue instanceof GameModelData shaiHuludModelData)) {
            throw new IllegalArgumentException("newValue must be a GameModelData object");
        }
        if (this.shaiHulud != shaiHulud) {
            throw new IllegalStateException(SHAI_HULUD_ALREADY_INITIALIZED_MESSAGE);
        }
        if (gameModel == null) {
            throw new IllegalStateException(GAME_MODEL_MISSING_MESSAGE);
        }

        // Update position and rotation of shai hulud
        var newGameModelPosition = new Vector3(shaiHuludModelData.position().x, 0f, shaiHuludModelData.position().y);
        gameModel.rePositionAndRotate(newGameModelPosition, shaiHuludModelData.rotation());
    }

    private void handleVanishEvent(@NonNull ShaiHulud shaiHulud) throws IllegalStateException {
        if (this.shaiHulud != shaiHulud) {
            throw new IllegalStateException(SHAI_HULUD_ALREADY_INITIALIZED_MESSAGE);
        }
        if (gameModel == null) {
            throw new IllegalStateException(GAME_MODEL_MISSING_MESSAGE);
        }

        // Removes shai hulud from scene manager to make him invisible
        sceneManager.removeScene(gameModel.getScene());
    }

    public void dispose() {
        gameModel.getScene().modelInstance.model.dispose();
    }
}