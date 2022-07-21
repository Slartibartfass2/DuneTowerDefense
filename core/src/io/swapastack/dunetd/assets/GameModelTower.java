package io.swapastack.dunetd.assets;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import lombok.NonNull;
import net.mgsx.gltf.scene3d.scene.Scene;
import org.jetbrains.annotations.Nullable;

public final class GameModelTower implements GameModelInterface {

    private GameModelPart baseTowerPart;
    private GameModelPart topTowerPart;
    private final float topPartYValue;

    public GameModelTower(@NonNull GameModelPart baseTowerPart, @Nullable GameModelPart topTowerPart) {
        this.baseTowerPart = baseTowerPart;
        this.topTowerPart = topTowerPart;

        // Calculate bounding box to set top part on top of base part
        if (topTowerPart != null) {
            var boundingBox = new BoundingBox();
            baseTowerPart.getScene().modelInstance.calculateBoundingBox(boundingBox);
            var modelDimensions = new Vector3();
            boundingBox.getDimensions(modelDimensions);
            topPartYValue = modelDimensions.y;
        } else {
            topPartYValue = 0f;
        }
    }

    /**
     * Sets the position and rotation of the model and updates the model.
     *
     * @param newPosition New position of the model
     * @param rotation    New rotation of the model
     */
    @Override
    public void rePositionAndRotate(@NonNull Vector3 newPosition, float rotation) {
        baseTowerPart.setPosition(newPosition.cpy());
        baseTowerPart.update();

        // Only rotate top part of the model
        if (topTowerPart != null) {
            topTowerPart.setPosition(new Vector3(newPosition.x, topPartYValue, newPosition.z));
            topTowerPart.setRotation(rotation);
            topTowerPart.update();
        }
    }

    /**
     * Sets the position of the model and updates the model.
     *
     * @param newPosition new position of the model
     */
    @Override
    public void rePosition(@NonNull Vector3 newPosition) {
        baseTowerPart.setPosition(newPosition.cpy());
        baseTowerPart.update();

        if (topTowerPart != null) {
            topTowerPart.setPosition(new Vector3(newPosition.x, topPartYValue, newPosition.z));
            topTowerPart.update();
        }
    }

    /**
     * Sets the rotation of the top part of the model and updates the model.
     *
     * @param rotation new rotation of the top part of the model
     */
    @Override
    public void rotate(float rotation) {
        if (topTowerPart == null) {
            throw new IllegalStateException("This GameModelTower has no top part and cannot be rotated");
        }

        // Only rotate top part of the model
        topTowerPart.setRotation(rotation);
        topTowerPart.update();
    }

    /**
     * Updates the animationController of the model.
     *
     * @param deltaTime The time in seconds since the last update
     */
    @Override
    public void updateAnimation(float deltaTime) {
        baseTowerPart.updateAnimation(deltaTime);
        if (topTowerPart != null) {
            topTowerPart.updateAnimation(deltaTime);
        }
    }

    /**
     * Sets the paused property of the animation controller to the specified boolean.
     *
     * @param paused If true the animations are paused, otherwise not
     */
    @Override
    public void pauseAnimation(boolean paused) {
        baseTowerPart.pauseAnimation(paused);

        if (topTowerPart != null) {
            topTowerPart.pauseAnimation(paused);
        }
    }

    @Override
    public float getRotation() {
        if (topTowerPart != null) {
            return topTowerPart.getRotation();
        }
        return -1f;
    }

    @Override
    public Scene[] getScenes() {
        if (topTowerPart == null) {
            return new Scene[]{baseTowerPart.getScene()};
        }
        return new Scene[]{
                baseTowerPart.getScene(),
                topTowerPart.getScene()
        };
    }

    /**
     * Sets the topTowerPart to null and the baseTowerPart to the TowerDebrisGameModel from the AssetLoader.
     */
    public void setToDebris(@NonNull AssetLoader assetLoader) {
        // Dispose tower models
        if (topTowerPart != null) {
            topTowerPart.getScene().modelInstance.model.dispose();
        }
        topTowerPart = null;

        baseTowerPart.getScene().modelInstance.model.dispose();

        // Get debris game model
        baseTowerPart = assetLoader.getTowerDebrisGameModelPart(
                baseTowerPart.getScale(),
                baseTowerPart.getRotation(),
                baseTowerPart.getPosition()
        );

        baseTowerPart.update();
    }
}