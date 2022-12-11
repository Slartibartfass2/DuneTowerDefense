package io.swapastack.dunetd.assets.controller;

import io.swapastack.dunetd.assets.AssetLoader;
import io.swapastack.dunetd.assets.GameModelSingle;
import io.swapastack.dunetd.game.GameModelData;
import io.swapastack.dunetd.shaihulud.ShaiHulud;
import io.swapastack.dunetd.vectors.Vector3;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import lombok.NonNull;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public final class ShaiHuludController implements PropertyChangeListener {

    // Event names
    public static final String CREATE_EVENT_NAME = "create";

    public static final String SHOW_EVENT_NAME = "show";

    public static final String UPDATE_EVENT_NAME = "update";

    public static final String VANISH_EVENT_NAME = "vanish";

    // Exception messages
    private static final String GAME_MODEL_MISSING_MESSAGE = "The game model has not been initialized";

    private final SceneManager sceneManager;

    private final AssetLoader assetLoader;

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
                || !(event.getSource() instanceof ShaiHulud)) {
            throw new IllegalArgumentException("Property name and source must be non null and source must be a shai " +
                    "hulud");
        }

        switch (event.getPropertyName()) {
            // Event when shai hulud is created
            case CREATE_EVENT_NAME -> handleCreateEvent();
            // Event when shai hulud is displayed on the screen
            case SHOW_EVENT_NAME -> handleShowEvent(event.getNewValue());
            // Event whenever shai hulud is moved
            case UPDATE_EVENT_NAME -> handleUpdateEvent(event.getNewValue());
            // Event when shai hulud disappears from the screen
            case VANISH_EVENT_NAME -> handleVanishEvent();
            default -> throw new IllegalStateException("Unexpected event name: " + event.getPropertyName());
        }
    }

    private void handleCreateEvent() {
        gameModel = assetLoader.getShaiHuludGameModel();
    }

    @SuppressWarnings("DuplicatedCode")
    private void handleShowEvent(@NonNull Object newValue) throws IllegalArgumentException, IllegalStateException {
        if (!(newValue instanceof GameModelData shaiHuludModelData)) {
            throw new IllegalArgumentException("newValue must be a GameModelData object");
        }
        if (gameModel == null) {
            throw new IllegalStateException(GAME_MODEL_MISSING_MESSAGE);
        }

        // Add scene of game model to scene manager and bring shai hulud in its initial position and rotation
        var newGameModelPosition = Vector3.fromVector2(shaiHuludModelData.position(), 0);
        gameModel.rePositionAndRotate(newGameModelPosition, shaiHuludModelData.rotation());
        sceneManager.addScene(gameModel.getScene());
    }

    @SuppressWarnings("DuplicatedCode")
    private void handleUpdateEvent(@NonNull Object newValue)
            throws IllegalArgumentException, IllegalStateException {
        if (!(newValue instanceof GameModelData shaiHuludModelData)) {
            throw new IllegalArgumentException("newValue must be a GameModelData object");
        }
        if (gameModel == null) {
            throw new IllegalStateException(GAME_MODEL_MISSING_MESSAGE);
        }

        // Update position and rotation of shai hulud
        var newGameModelPosition = Vector3.fromVector2(shaiHuludModelData.position(), 0);
        gameModel.rePositionAndRotate(newGameModelPosition, shaiHuludModelData.rotation());
    }

    private void handleVanishEvent() throws IllegalStateException {
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
