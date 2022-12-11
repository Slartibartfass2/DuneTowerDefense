package io.swapastack.dunetd.assets;

import com.badlogic.gdx.math.collision.BoundingBox;

import io.swapastack.dunetd.vectors.Vector3;

import org.jetbrains.annotations.Nullable;

import lombok.NonNull;
import net.mgsx.gltf.scene3d.scene.Scene;

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
            var modelDimensions = new com.badlogic.gdx.math.Vector3();
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
        baseTowerPart.setPosition(newPosition);
        baseTowerPart.update();

        // Only rotate top part of the model
        if (topTowerPart != null) {
            topTowerPart.setPosition(new Vector3(newPosition.x(), topPartYValue, newPosition.z()));
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
        baseTowerPart.setPosition(newPosition);
        baseTowerPart.update();

        if (topTowerPart != null) {
            topTowerPart.setPosition(new Vector3(newPosition.x(), topPartYValue, newPosition.z()));
            topTowerPart.update();
        }
    }

    /**
     * Updates the animationController of the model.
     *
     * @param deltaTimeInSeconds The time in seconds since the last update
     */
    @Override
    public void updateAnimation(float deltaTimeInSeconds) {
        baseTowerPart.updateAnimation(deltaTimeInSeconds);
        if (topTowerPart != null) {
            topTowerPart.updateAnimation(deltaTimeInSeconds);
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
                topTowerPart.getScene(),
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
