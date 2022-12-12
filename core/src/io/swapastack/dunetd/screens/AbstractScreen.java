package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.screens.listeners.ClickInputListener;
import io.swapastack.dunetd.settings.GameSettings;

import lombok.Getter;
import lombok.NonNull;

public abstract class AbstractScreen implements Screen {

    @Getter
    private final DuneTD game;
    @Getter
    private final Stage stage;
    @Getter
    private final GameSettings settings;
    private final ScreenColor screenColor;

    protected AbstractScreen(@NonNull DuneTD game, ScreenColor screenColor) {
        this.game = game;
        this.screenColor = screenColor;
        stage = new Stage(new ScreenViewport());
        settings = game.getSettings();
    }

    /**
     * Called when this screen becomes the current screen for a {@link DuneTD}.
     */
    @Override
    public final void show() {
        Gdx.input.setInputProcessor(stage);

        showScreen();
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public final void render(float delta) {
        clearColor(screenColor);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderScreen(delta);

        stage.act(delta);
        stage.draw();
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

    protected float getStageWidth() {
        return stage.getWidth();
    }

    protected float getStageHeight() {
        return stage.getHeight();
    }

    /**
     * Is called within {@link AbstractScreen#render(float)} after the screen is cleared and filled with the
     * {@link AbstractScreen#screenColor}.
     */
    protected void renderScreen(float delta) {
    }

    /**
     * Is called within {@link AbstractScreen#show()} after the input processor was set to the stage.
     */
    protected void showScreen() {
    }

    protected void addMainActor(@NonNull Actor actor) {
        stage.addActor(actor);
    }

    public ClickInputListener createChangeScreenInputListener(ScreenType screen) {
        return new ClickInputListener(() -> game.changeScreen(screen));
    }

    private void clearColor(ScreenColor newScreenColor) {
        Gdx.gl.glClearColor(newScreenColor.getRed(), newScreenColor.getGreen(), newScreenColor.getBlue(),
                newScreenColor.getAlpha());
    }
}
