package io.swapastack.dunetd.assets.controller;

import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.assets.AssetLoader;
import io.swapastack.dunetd.assets.GameModelSingle;
import io.swapastack.dunetd.game.GameModelData;
import io.swapastack.dunetd.hostileunits.HostileUnit;
import lombok.NonNull;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.UUID;

import static io.swapastack.dunetd.assets.AssetLoader.BOSS_UNIT_WALK_ANIMATION;
import static io.swapastack.dunetd.assets.AssetLoader.HARVESTER_WALK_ANIMATION;
import static io.swapastack.dunetd.assets.AssetLoader.INFANTRY_WALK_ANIMATION;
import static io.swapastack.dunetd.hostileunits.HostileUnitEnum.fromHostileUnit;

public final class HostileUnitController implements PropertyChangeListener {

    // Event names
    public static final String CREATE_EVENT_NAME = "create";
    public static final String SHOW_EVENT_NAME = "show";
    public static final String UPDATE_EVENT_NAME = "update";
    public static final String DESTROY_EVENT_NAME = "destroy";

    // Exception messages
    private static final String HOSTILE_UNIT_NOT_REGISTERED_MESSAGE = "Hostile unit is not registered in hostile unit" +
            " controller";

    private final SceneManager sceneManager;
    private final AssetLoader assetLoader;
    private final HashMap<UUID, GameModelSingle> hostileUnitModelMap;

    public HostileUnitController(@NonNull SceneManager sceneManager, @NonNull AssetLoader assetLoader) {
        this.sceneManager = sceneManager;
        this.assetLoader = assetLoader;
        hostileUnitModelMap = new HashMap<>();
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
                || !(event.getSource() instanceof HostileUnit hostileUnit)) {
            throw new IllegalArgumentException("Property name and source must be non null and source must be a " +
                    "hostile unit");
        }

        switch (event.getPropertyName()) {
            // Event when hostile unit is created
            case CREATE_EVENT_NAME -> handleCreateEvent(event.getNewValue(), hostileUnit);
            // Event when hostile unit is spawned on the grid / displayed on the screen
            case SHOW_EVENT_NAME -> handleShowEvent(hostileUnit);
            // Event whenever hostile unit is moved
            case UPDATE_EVENT_NAME -> handleUpdateEvent(event.getOldValue(), event.getNewValue(), hostileUnit);
            // Event when hostile unit is killed or reached the end portal
            case DESTROY_EVENT_NAME -> handleDestroyEvent(hostileUnit);
            default -> throw new IllegalArgumentException("Unexpected event name: " + event.getPropertyName());
        }
    }

    private void handleCreateEvent(@NonNull Object newValue, @NonNull HostileUnit hostileUnit)
            throws IllegalArgumentException {
        if (!(newValue instanceof GameModelData newHostileUnitData)) {
            throw new IllegalArgumentException("newValue must be a GameModelData object");
        }

        // Get animation name from hostile unit type
        var animationName = switch (fromHostileUnit(hostileUnit)) {
            case INFANTRY -> INFANTRY_WALK_ANIMATION;
            case HARVESTER -> HARVESTER_WALK_ANIMATION;
            case BOSS_UNIT -> BOSS_UNIT_WALK_ANIMATION;
        };

        // Set game models initial position and store both hostile unit and game model in the hashMap
        var gameModel = assetLoader.getHostileUnitGameModel(fromHostileUnit(hostileUnit));
        var newHostileUnitPosition = new Vector3(newHostileUnitData.position().x, 0f, newHostileUnitData.position().y);
        gameModel.rePosition(newHostileUnitPosition);
        gameModel.setAnimation(animationName, -1);
        hostileUnitModelMap.put(hostileUnit.getUuid(), gameModel);
    }

    private void handleShowEvent(@NonNull HostileUnit hostileUnit) throws IllegalArgumentException {
        if (!hostileUnitModelMap.containsKey(hostileUnit.getUuid())) {
            throw new IllegalStateException(HOSTILE_UNIT_NOT_REGISTERED_MESSAGE);
        }

        // Add scene of game model to scene manager
        var gameModel = hostileUnitModelMap.get(hostileUnit.getUuid());
        sceneManager.addScene(gameModel.getScene());
    }

    private void handleUpdateEvent(@NonNull Object oldValue, @NonNull Object newValue,
                                   @NonNull HostileUnit hostileUnit)
            throws IllegalStateException, IllegalArgumentException {
        if (!hostileUnitModelMap.containsKey(hostileUnit.getUuid())) {
            throw new IllegalStateException(HOSTILE_UNIT_NOT_REGISTERED_MESSAGE);
        }
        if (!(newValue instanceof GameModelData newGameModelData)) {
            throw new IllegalArgumentException("newValue must be a GameModelData object");
        }
        if (!(oldValue instanceof Float deltaTime)) {
            throw new IllegalArgumentException("newValue must be a float");
        }

        // Update game models position, rotation and animation
        var gameModel = hostileUnitModelMap.get(hostileUnit.getUuid());
        var newHostileUnitPosition = new Vector3(newGameModelData.position().x, 0f, newGameModelData.position().y);
        gameModel.rePositionAndRotate(newHostileUnitPosition, newGameModelData.rotation());
        gameModel.updateAnimation(deltaTime);
        hostileUnitModelMap.put(hostileUnit.getUuid(), gameModel);
    }

    private void handleDestroyEvent(@NonNull HostileUnit hostileUnit) {
        if (!hostileUnitModelMap.containsKey(hostileUnit.getUuid())) {
            throw new IllegalStateException(HOSTILE_UNIT_NOT_REGISTERED_MESSAGE);
        }

        // Remove scene of game model from scene manager and remove hostile unit from hash map
        var gameModel = hostileUnitModelMap.get(hostileUnit.getUuid());
        sceneManager.removeScene(gameModel.getScene());
        gameModel.getScene().modelInstance.model.dispose();
        hostileUnitModelMap.remove(hostileUnit.getUuid());
    }

    /**
     * Pauses or unpauses the animation of all game models.
     *
     * @param paused If true the animations will be paused, otherwise unpaused.
     */
    public void pauseAnimations(boolean paused) {
        for (var gameModel : hostileUnitModelMap.values()) {
            gameModel.pauseAnimation(paused);
        }
    }

    public void dispose() {
        for (var gameModel : hostileUnitModelMap.values()) {
            gameModel.getScene().modelInstance.model.dispose();
        }
    }
}
