package io.swapastack.dunetd.screens;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import lombok.NonNull;

/**
 * Source:
 * <a href="https://github.com/Matsemann/libgdx-loading-screen/blob/libgdx-1.9.9-LaurenceWarne/core/src/com/matsemann/libgdxloadingscreen/LoadingBar.java">https://github.com/Matsemann/libgdx-loading-screen/blob/libgdx-1.9.9-LaurenceWarne/core/src/com/matsemann/libgdxloadingscreen/LoadingBar.java</a>
 *
 * @author Mats Svensson & Laurence Warne
 */
public class LoadingBar extends Actor {

    private final Animation<? extends TextureRegion> animation;
    private TextureRegion reg;
    private float stateTime;

    public LoadingBar(@NonNull Animation<? extends TextureRegion> animation) {
        this.animation = animation;
        reg = animation.getKeyFrame(0);
    }

    /**
     * Updates the state of the LoadingBar.
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void act(float delta) {
        stateTime += delta;
        reg = animation.getKeyFrame(stateTime);
    }

    /**
     * Draws the LoadingBar with the specified batch.
     *
     * @param parentAlpha has no effect
     */
    @Override
    public void draw(@NonNull Batch batch, float parentAlpha) {
        batch.draw(reg, getX(), getY());
    }
}
