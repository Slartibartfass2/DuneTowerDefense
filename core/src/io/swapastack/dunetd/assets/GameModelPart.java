package io.swapastack.dunetd.assets;

import io.swapastack.dunetd.vectors.Vector3;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneModel;

public final class GameModelPart {

    @Getter
    private final Scene scene;
    @Getter
    private final Vector3 scale;
    @Getter
    @Setter
    private float rotation;
    @Getter
    @Setter
    private Vector3 position;
    private final float offsetRotation;
    private final Vector3 offsetPosition;

    public GameModelPart(@NonNull SceneModel sceneModel) {
        scene = new Scene(sceneModel);
        scale = new Vector3(1f, 1f, 1f);
        rotation = 0f;
        position = Vector3.ZERO;
        offsetRotation = 0f;
        offsetPosition = Vector3.ZERO;

        update();
    }

    public GameModelPart(@NonNull SceneModel sceneModel, @NonNull Vector3 scale, float rotation,
                         @NonNull Vector3 position) {
        scene = new Scene(sceneModel);
        this.scale = scale;
        this.rotation = rotation;
        this.position = position;
        offsetRotation = 0f;
        offsetPosition = Vector3.ZERO;

        update();
    }

    public GameModelPart(@NonNull SceneModel sceneModel, @NonNull Vector3 scale, float rotation,
                         @NonNull Vector3 position, float offsetRotation, @NonNull Vector3 offsetPosition) {
        scene = new Scene(sceneModel);
        this.scale = scale;
        this.rotation = rotation;
        this.position = position;
        this.offsetRotation = offsetRotation;
        this.offsetPosition = offsetPosition;

        update();
    }

    /**
     * Updates the position, scale and rotation of this model.
     */
    public void update() {
        var newPosition = Vector3.add(position, offsetPosition);
        var newPositionLibGdx = new com.badlogic.gdx.math.Vector3(newPosition.x(), newPosition.y(), newPosition.z());
        var scaleLibGdx = new com.badlogic.gdx.math.Vector3(scale.x(), scale.y(), scale.z());
        var axis = new com.badlogic.gdx.math.Vector3(0f, 1f, 0f);

        scene.modelInstance.transform.setToTranslation(newPositionLibGdx)
                .scale(scaleLibGdx.x, scaleLibGdx.y, scaleLibGdx.z)
                .rotate(axis, (rotation + offsetRotation) % 360f);
    }

    /**
     * Sets the animation of the model.
     *
     * @param animationName ID of the animation
     * @param loopCount     The number of times to loop the animation, zero to play the animation only once, negative to
     *                      continuously loop the animation.
     */
    public void setAnimation(@NonNull String animationName, int loopCount) {
        scene.animationController.setAnimation(animationName, loopCount);
    }

    /**
     * Updates animation of this model.
     *
     * @param deltaTimeInSeconds The time in seconds since the last update
     */
    public void updateAnimation(float deltaTimeInSeconds) {
        if (scene.animationController != null) {
            scene.animationController.update(deltaTimeInSeconds);
        }
    }

    /**
     * Sets the paused property of the animation controller to the specified boolean.
     *
     * @param paused If true the animations are paused, otherwise not
     */
    public void pauseAnimation(boolean paused) {
        scene.animationController.paused = paused;
    }
}
