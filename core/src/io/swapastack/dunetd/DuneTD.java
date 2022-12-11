package io.swapastack.dunetd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.VisUI;

import io.swapastack.dunetd.assets.AssetLoader;
import io.swapastack.dunetd.screens.CreditsScreen;
import io.swapastack.dunetd.screens.HighscoreScreen;
import io.swapastack.dunetd.screens.LoadGameScreen;
import io.swapastack.dunetd.screens.LoadingScreen;
import io.swapastack.dunetd.screens.MainMenuScreen;
import io.swapastack.dunetd.screens.NewGameScreen;
import io.swapastack.dunetd.screens.ScreenEnum;
import io.swapastack.dunetd.screens.SettingsScreen;
import io.swapastack.dunetd.screens.gamescreen.GameScreen;
import io.swapastack.dunetd.settings.GameSettings;

import lombok.Getter;
import lombok.NonNull;

public final class DuneTD extends Game {

    @Getter
    private GameSettings settings;
    @Getter
    private AssetLoader assetLoader;

    public DuneTD() {
        // Initialization is done in create method
    }

    @Override
    public void create() {
        // Load settings
        settings = new GameSettings();
        assetLoader = new AssetLoader();

        setPrimaryMonitor();

        // First show loading screen while loading all assets
        changeScreen(ScreenEnum.LOADING_SCREEN);
    }

    @Override
    public void dispose() {
        // If screen is not null dispose all resources from screen
        if (screen != null) {
            screen.dispose();
        }

        // Free all resources allocated by the asset loader
        assetLoader.dispose();

        // Dispose skin
        VisUI.dispose(true);
    }

    /**
     * This function can be used to switch screens.
     *
     * @param screen {@link ScreenEnum}
     */
    public void changeScreen(@NonNull ScreenEnum screen) {
        // Get reference to current screen object
        var currentScreen = this.screen;
        // set the screen to null
        setScreen(null);
        // If the current screen reference is not null, dispose all its resources
        if (currentScreen != null) {
            currentScreen.dispose();
        }

        // Create new screen and set it as current
        switch (screen) {
            case LOADING_SCREEN -> setScreen(new LoadingScreen(this));
            case MENU -> setScreen(new MainMenuScreen(this));
            case NEW_GAME -> setScreen(new NewGameScreen(this));
            case LOAD_GAME -> setScreen(new LoadGameScreen(this));
            case GAME -> setScreen(new GameScreen(this));
            case SETTINGS -> setScreen(new SettingsScreen(this));
            case HIGHSCORE -> setScreen(new HighscoreScreen(this));
            case CREDITS -> setScreen(new CreditsScreen(this));
            default -> throw new IllegalStateException("Unexpected value: " + screen);
        }
    }

    /**
     * Sets the primary monitor on which the game is displayed.
     */
    private void setPrimaryMonitor() {
        int monitorIndex = settings.getMonitorIndex();
        var monitors = Gdx.graphics.getMonitors();
        // If monitor is not primary (which would be already selected) or an invalid value, select new monitor
        if (monitorIndex > 0 && monitorIndex < monitors.length) {
            var displayMode = Gdx.graphics.getDisplayMode(monitors[monitorIndex]);
            Gdx.graphics.setFullscreenMode(displayMode);
        }
    }
}
