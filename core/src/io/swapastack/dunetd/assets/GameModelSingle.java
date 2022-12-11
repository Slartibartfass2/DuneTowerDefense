package io.swapastack.dunetd.assets;

import io.swapastack.dunetd.vectors.Vector3;

import lombok.NonNull;
import net.mgsx.gltf.scene3d.scene.Scene;

public final class GameModelSingle implements GameModelInterface {

    private final GameModelPart gameModelPart;

    public GameModelSingle(@NonNull GameModelPart gameModelPart) {
        this.gameModelPart = gameModelPart;
    }

    /**
     * Sets the position and rotation of the model and updates the model.
     *
     * @param newPosition New position of the model
     * @param rotation    New rotation of the model
     */
    @Override
    public void rePositionAndRotate(@NonNull Vector3 newPosition, float rotation) {
        gameModelPart.setPosition(newPosition);
        gameModelPart.setRotation(rotation);
        gameModelPart.update();
    }

    /**
     * Sets the position of the model and updates the model.
     *
     * @param newPosition New position of the model
     */
    @Override
    public void rePosition(@NonNull Vector3 newPosition) {
        gameModelPart.setPosition(newPosition);
        gameModelPart.update();
    }

    /**
     * Updates the animationController of the model.
     *
     * @param deltaTimeInSeconds The time in seconds since the last update
     */
    @Override
    public void updateAnimation(float deltaTimeInSeconds) {
        gameModelPart.updateAnimation(deltaTimeInSeconds);
    }

    /**
     * Sets the paused property of the animation controller to the specified boolean.
     *
     * @param paused If true the animations are paused, otherwise not
     */
    @Override
    public void pauseAnimation(boolean paused) {
        gameModelPart.pauseAnimation(paused);
    }

    @Override
    public float getRotation() {
        return gameModelPart.getRotation();
    }

    @Override
    public Scene[] getScenes() {
        return new Scene[]{gameModelPart.getScene()};
    }

    public Scene getScene() {
        return gameModelPart.getScene();
    }

    /**
     * Sets the animation of the model.
     *
     * @param animationName ID of the animation
     * @param loopCount     The number of times to loop the animation, zero to play the animation only once, negative to
     *                      continuously loop the animation.
     */
    public void setAnimation(@NonNull String animationName, int loopCount) {
        gameModelPart.setAnimation(animationName, loopCount);
    }
}
