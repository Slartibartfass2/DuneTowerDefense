package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.screens.listeners.ChangeScreenInputListener;
import io.swapastack.dunetd.screens.listeners.ClickInputListener;

import lombok.NonNull;

public final class NewGameScreen extends AbstractScreen {

    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");

    private Spinner gridWidthSpinner;
    private Spinner gridHeightSpinner;

    public NewGameScreen(@NonNull DuneTD game) {
        super(game);
    }

    /**
     * Called when this screen becomes the current screen for a {@link DuneTD}.
     */
    @Override
    public void show() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        // Spinners to set grid dimensions
        gridWidthSpinner = new Spinner("Width", new IntSpinnerModel(settings.getGridWidth(), 2,
                MAX_GRID_WIDTH, 1));
        gridHeightSpinner = new Spinner("Height", new IntSpinnerModel(settings.getGridHeight(), 2,
                MAX_GRID_HEIGHT, 1));

        // Button to switch to MainMenuScreen
        var backToMainMenuButton = new VisTextButton("Back");
        backToMainMenuButton.addListener(new ChangeScreenInputListener(game, ScreenEnum.MENU));

        // Button to switch to GameScreen (and start new game)
        var startNewGameButton = new VisTextButton("Start game");
        startNewGameButton.addListener(new ClickInputListener(() -> {
            saveGameSettings();
            game.changeScreen(ScreenEnum.GAME);
        }));

        var table = new VisTable(true);

        table.add(gridWidthSpinner);
        table.add(gridHeightSpinner);
        table.row();
        table.add(backToMainMenuButton);
        table.add(startNewGameButton);

        table.setPosition(width / 2f, height / 2f);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(ScreenColors.BACKGROUND_COLOR_RED, ScreenColors.BACKGROUND_COLOR_GREEN,
                ScreenColors.BACKGROUND_COLOR_BLUE, ScreenColors.BACKGROUND_COLOR_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    /**
     * Called when this screen is no longer the current screen for a {@link DuneTD}.
     */
    @Override
    public void hide() {
        saveGameSettings();
    }

    private void saveGameSettings() {
        settings.setGridWidth(Integer.parseInt(gridWidthSpinner.getModel().getText()));
        settings.setGridHeight(Integer.parseInt(gridHeightSpinner.getModel().getText()));
    }
}
