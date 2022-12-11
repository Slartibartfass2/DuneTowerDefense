package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.screens.listeners.ClickInputListener;

import lombok.NonNull;

public final class NewGameScreen extends AbstractScreen {

    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");

    private Spinner gridWidthSpinner;
    private Spinner gridHeightSpinner;

    public NewGameScreen(@NonNull DuneTD game) {
        super(game, ScreenColor.MAIN_BACKGROUND);
    }

    /**
     * Called when this screen becomes the current screen for a {@link DuneTD}.
     */
    @Override
    public void showScreen() {
        var settings = getSettings();
        // Spinners to set grid dimensions
        gridWidthSpinner = new Spinner("Width", new IntSpinnerModel(settings.getGridWidth(), 2,
                MAX_GRID_WIDTH, 1));
        gridHeightSpinner = new Spinner("Height", new IntSpinnerModel(settings.getGridHeight(), 2,
                MAX_GRID_HEIGHT, 1));

        // Button to switch to MainMenuScreen
        var backToMainMenuButton = new VisTextButton("Back");
        backToMainMenuButton.addListener(createChangeScreenInputListener(ScreenType.MENU));

        // Button to switch to GameScreen (and start new game)
        var startNewGameButton = new VisTextButton("Start game");
        startNewGameButton.addListener(new ClickInputListener(this::saveGameSettings));
        startNewGameButton.addListener(createChangeScreenInputListener(ScreenType.GAME));

        var table = new VisTable(true);

        table.add(gridWidthSpinner);
        table.add(gridHeightSpinner);
        table.row();
        table.add(backToMainMenuButton);
        table.add(startNewGameButton);

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        table.setPosition(width / 2f, height / 2f);
        addMainActor(table);
    }

    /**
     * Called when this screen is no longer the current screen for a {@link DuneTD}.
     */
    @Override
    public void hide() {
        saveGameSettings();
    }

    private void saveGameSettings() {
        getSettings().setGridWidth(Integer.parseInt(gridWidthSpinner.getModel().getText()));
        getSettings().setGridHeight(Integer.parseInt(gridHeightSpinner.getModel().getText()));
    }
}
