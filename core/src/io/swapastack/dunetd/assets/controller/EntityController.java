package io.swapastack.dunetd.assets.controller;

import io.swapastack.dunetd.assets.AssetLoader;
import io.swapastack.dunetd.assets.GameModelInterface;
import io.swapastack.dunetd.assets.GameModelTower;
import io.swapastack.dunetd.entities.Entity;
import io.swapastack.dunetd.entities.portals.Portal;
import io.swapastack.dunetd.entities.towers.Tower;
import io.swapastack.dunetd.entities.towers.TowerEnum;
import io.swapastack.dunetd.game.GameModelData;
import io.swapastack.dunetd.vectors.Vector3;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import lombok.NonNull;
import net.mgsx.gltf.scene3d.scene.SceneManager;

public final class EntityController implements PropertyChangeListener {

    // Event names
    public static final String CREATE_EVENT_NAME = "create";

    public static final String SHOW_EVENT_NAME = "show";

    public static final String UPDATE_EVENT_NAME = "update";

    public static final String TO_DEBRIS_EVENT_NAME = "to_debris";

    public static final String DESTROY_EVENT_NAME = "destroy";

    // Exception messages
    private static final String ENTITY_NOT_REGISTERED_MESSAGE = "Entity is not registered in entity controller";

    private final SceneManager sceneManager;

    private final AssetLoader assetLoader;

    private final HashMap<UUID, GameModelInterface> entityModelMap;

    public EntityController(@NonNull SceneManager sceneManager, @NonNull AssetLoader assetLoader) {
        this.sceneManager = sceneManager;
        this.assetLoader = assetLoader;
        entityModelMap = new HashMap<>();
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
                || !(event.getSource() instanceof Entity entity)) {
            throw new IllegalArgumentException("Property name and source must be non null and source must be an " +
                    "entity");
        }

        switch (event.getPropertyName()) {
            // Event when entity is created
            case CREATE_EVENT_NAME -> handleCreateEvent(event.getNewValue(), entity);
            // Event when entity is displayed on the screen
            case SHOW_EVENT_NAME -> handleShowEvent(entity);
            // Event whenever entity is moved or rotated
            case UPDATE_EVENT_NAME -> handleUpdateEvent(event.getNewValue(), entity);
            // Event when entity (as tower) is set to debris
            case TO_DEBRIS_EVENT_NAME -> handleToDebrisEvent(entity);
            // Event when entity is destroyed
            case DESTROY_EVENT_NAME -> handleDestroyEvent(entity);
            default -> throw new IllegalArgumentException("Unexpected event name: " + event.getPropertyName());
        }
    }

    private void handleCreateEvent(@NonNull Object newValue, @NonNull Entity entity) throws IllegalArgumentException {
        if (!(newValue instanceof GameModelData entityModelData)) {
            throw new IllegalArgumentException("newValue must be a GameModelData object");
        }

        // Set game models initial position and store both entity and game model in the hashMap
        GameModelInterface gameModel;
        if (entity instanceof Tower tower) {
            gameModel = assetLoader.getTowerGameModel(TowerEnum.fromTower(tower));
        } else if (entity instanceof Portal portal) {
            gameModel = assetLoader.getPortalGameModel(portal);
        } else {
            return;
        }

        var gameModelPosition = Vector3.fromVector2(entityModelData.position(), 0);
        gameModel.rePositionAndRotate(gameModelPosition, entityModelData.rotation());
        entityModelMap.put(entity.getUuid(), gameModel);
    }

    private void handleShowEvent(@NonNull Entity entity) throws IllegalStateException {
        if (!entityModelMap.containsKey(entity.getUuid())) {
            throw new IllegalStateException(ENTITY_NOT_REGISTERED_MESSAGE);
        }

        // Add scene of game model to scene manager
        var gameModel = entityModelMap.get(entity.getUuid());
        for (var scene : gameModel.getScenes()) {
            sceneManager.addScene(scene);
        }
    }

    private void handleUpdateEvent(@NonNull Object newValue, @NonNull Entity entity)
            throws IllegalStateException, IllegalArgumentException {
        if (!entityModelMap.containsKey(entity.getUuid())) {
            throw new IllegalStateException(ENTITY_NOT_REGISTERED_MESSAGE);
        }
        if (!(newValue instanceof GameModelData newGameModelData)) {
            throw new IllegalArgumentException("newValue must be a GameModelData object");
        }

        // Update game models position, rotation and animation
        var gameModel = entityModelMap.get(entity.getUuid());
        var gameModelPosition = Vector3.fromVector2(newGameModelData.position(), 0);
        if (newGameModelData.rotation() != -1f) {
            gameModel.rePositionAndRotate(gameModelPosition, newGameModelData.rotation());
        } else {
            gameModel.rePosition(gameModelPosition);
        }
        entityModelMap.put(entity.getUuid(), gameModel);
    }

    private void handleToDebrisEvent(@NonNull Entity entity)
            throws IllegalStateException, IllegalArgumentException {
        if (!entityModelMap.containsKey(entity.getUuid())) {
            throw new IllegalStateException(ENTITY_NOT_REGISTERED_MESSAGE);
        }
        if (!(entity instanceof Tower tower)) {
            throw new IllegalArgumentException("Entity must be a tower");
        }

        // Remove game model and add new debris game model to scene manager
        var gameModel = (GameModelTower) entityModelMap.get(tower.getUuid());
        for (var scene : gameModel.getScenes()) {
            sceneManager.removeScene(scene);
        }
        gameModel.setToDebris(assetLoader);
        for (var scene : gameModel.getScenes()) {
            sceneManager.addScene(scene);
        }
    }

    private void handleDestroyEvent(@NonNull Entity entity) throws IllegalStateException {
        if (!entityModelMap.containsKey(entity.getUuid())) {
            throw new IllegalStateException(ENTITY_NOT_REGISTERED_MESSAGE);
        }

        // Remove scene of game model from scene manager and remove entity from hash map
        var gameModel = entityModelMap.get(entity.getUuid());
        var scenes = gameModel.getScenes();
        for (var scene : scenes) {
            sceneManager.removeScene(scene);
            scene.modelInstance.model.dispose();
        }
        entityModelMap.remove(entity.getUuid());
    }

    public void dispose() {
        entityModelMap.values().stream()
                .flatMap(gameModel -> Arrays.stream(gameModel.getScenes()))
                .forEach(scene -> scene.modelInstance.model.dispose());
    }
}
