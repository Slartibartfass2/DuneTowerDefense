package io.swapastack.dunetd.assets;

import com.badlogic.gdx.math.Vector3;
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
        position = Vector3.Zero;
        offsetRotation = 0f;
        offsetPosition = Vector3.Zero;

        update();
    }

    public GameModelPart(@NonNull SceneModel sceneModel, @NonNull Vector3 scale, float rotation,
                         @NonNull Vector3 position) {
        scene = new Scene(sceneModel);
        this.scale = scale.cpy();
        this.rotation = rotation;
        this.position = position.cpy();
        offsetRotation = 0f;
        offsetPosition = Vector3.Zero;

        update();
    }

    public GameModelPart(@NonNull SceneModel sceneModel, @NonNull Vector3 scale, float rotation,
                         @NonNull Vector3 position, float offsetRotation, @NonNull Vector3 offsetPosition) {
        scene = new Scene(sceneModel);
        this.scale = scale.cpy();
        this.rotation = rotation;
        this.position = position.cpy();
        this.offsetRotation = offsetRotation;
        this.offsetPosition = offsetPosition.cpy();

        update();
    }

    /**
     * Updates the position, scale and rotation of this model.
     */
    public void update() {
        var newPosition = position.cpy().add(offsetPosition);
        scene.modelInstance.transform.setToTranslation(newPosition)
                .scale(scale.x, scale.y, scale.z)
                .rotate(new Vector3(0f, 1f, 0f), (rotation + offsetRotation) % 360f);
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
     * @param deltaTime The time in seconds since the last update
     */
    public void updateAnimation(float deltaTime) {
        if (scene.animationController != null) {
            scene.animationController.update(deltaTime);
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
