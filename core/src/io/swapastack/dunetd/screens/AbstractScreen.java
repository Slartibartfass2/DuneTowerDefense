package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.settings.GameSettings;

import lombok.Getter;
import lombok.NonNull;

public abstract class AbstractScreen implements Screen {

    @Getter
    protected final DuneTD game;
    protected final Stage stage;
    protected final GameSettings settings;

    protected AbstractScreen(@NonNull DuneTD game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        settings = game.getSettings();
    }

    /**
     * Called when the {@link DuneTD} is resized. This can happen at any point during a non-paused state but will never
     * happen before a call to {@link DuneTD#create()}.
     *
     * @param width  the new width in pixels
     * @param height the new height in pixels
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    /**
     * Called when the {@link DuneTD} is paused, usually when it's not active or visible on-screen. An Application is
     * also paused before it is destroyed.
     */
    @Override
    public void pause() {
    }

    /**
     * Called when the {@link DuneTD} is resumed from a paused state, usually when it regains focus.
     */
    @Override
    public void resume() {
    }

    /**
     * Called when this screen is no longer the current screen for a {@link DuneTD}.
     */
    @Override
    public void hide() {
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
