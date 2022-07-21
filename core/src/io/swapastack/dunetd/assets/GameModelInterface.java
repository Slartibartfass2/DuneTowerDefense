package io.swapastack.dunetd.assets;

import com.badlogic.gdx.math.Vector3;
import lombok.NonNull;
import net.mgsx.gltf.scene3d.scene.Scene;

public interface GameModelInterface {

    /**
     * Sets the position and rotation of the model and updates the model.
     *
     * @param newPosition New position of the model
     * @param rotation    New rotation of the model
     */
    void rePositionAndRotate(@NonNull Vector3 newPosition, float rotation);

    /**
     * Sets the position of the model and updates the model.
     *
     * @param newPosition New position of the model
     */
    void rePosition(@NonNull Vector3 newPosition);

    /**
     * Sets the rotation of the model and updates the model.
     *
     * @param rotation New rotation of the model
     */
    void rotate(float rotation);
    
    /**
     * Updates the animationController of the model.
     *
     * @param deltaTime The time in seconds since the last update
     */
    void updateAnimation(float deltaTime);

    /**
     * Sets the paused property of the animation controller to the specified boolean.
     *
     * @param paused If true the animations are paused, otherwise not
     */
    void pauseAnimation(boolean paused);

    float getRotation();

    Scene[] getScenes();
}