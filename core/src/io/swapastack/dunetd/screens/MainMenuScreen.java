package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.screens.listeners.ClickInputListener;

import lombok.NonNull;

/**
 * This is the MainMenuScreen class.
 * This class is used to display the main menu.
 * It displays the name of the game in latin and japanese letters.
 * Multiple buttons for interaction with the app.
 */
public final class MainMenuScreen extends AbstractScreen {

    private static final float VOLUME_MULTIPLIER = 0.6f;

    private final Image backgroundImage;
    private final FreeTypeFontGenerator bitmapFontGenerator;
    private final Music backgroundMusic;

    public MainMenuScreen(@NonNull DuneTD game) {
        super(game, ScreenColor.BLACK);

        backgroundImage = new Image(game.getAssetLoader().getMainMenuBackgroundImage());

        var fontFileHandle = Gdx.files.internal("fonts/NotoSansCJKtc_ttf/NotoSansCJKtc-Bold.ttf");
        bitmapFontGenerator = new FreeTypeFontGenerator(fontFileHandle);

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("piano/piano_loop.wav"));
    }

    private Label.LabelStyle getLabelStyle(String label) {
        // Specify parameters for BitmapFont generation
        var bitmapFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        // Set font size
        bitmapFontParameter.size = 60;
        // Specify available letters
        bitmapFontParameter.characters = label;
        // Set font color in RGBA format (red, green, blue, alpha)
        bitmapFontParameter.color = new Color(1f, 1f, 0, 1f);
        // Other specifications
        bitmapFontParameter.borderWidth = 1;
        bitmapFontParameter.borderColor = Color.BLACK;
        bitmapFontParameter.shadowOffsetX = 3;
        bitmapFontParameter.shadowOffsetY = 3;
        bitmapFontParameter.shadowColor = new Color(1f, 1f, 0, 0.25f);

        // Generate BitmapFont with FreeTypeFontGenerator and FreeTypeFontParameter specification
        var japaneseLatinFont = bitmapFontGenerator.generateFont(bitmapFontParameter);

        // Create a LabelStyle object to specify Label font
        return new Label.LabelStyle(japaneseLatinFont, null);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void showScreen() {
        var table = new VisTable(true);
        table.setFillParent(true);
        var stack = new Stack();

        // Adding Image to the stack
        backgroundImage.setScaling(Scaling.fill);
        stack.add(backgroundImage);

        var menuTable = new VisTable(true);

        // Create string for BitmapFont and Label creation
        var duneTD = "Dune TD";

        // Initialize FreeTypeFontGenerator for BitmapFont generation
        var japaneseLatinLabelStyle = getLabelStyle(duneTD);

        // Create a Label with the main menu title string
        var duneTDLabel = new VisLabel(duneTD, japaneseLatinLabelStyle);
        duneTDLabel.setFontScale(1, 1);
        duneTDLabel.setPosition(Gdx.graphics.getWidth() / 2f - duneTDLabel.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - duneTDLabel.getHeight() / 2f + 230
        );

        // Add main menu title string Label to Stage
        menuTable.addActor(duneTDLabel);

        createMenuButtons(menuTable);

        stack.add(menuTable);
        table.add(stack);
        addMainActor(table);

        // Load background music
        var masterVolume = getSettings().getMasterVolume();
        if (masterVolume > 0) {
            backgroundMusic.setVolume(masterVolume * VOLUME_MULTIPLIER);
            backgroundMusic.setLooping(true);
            backgroundMusic.play();
        }
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        backgroundMusic.stop();
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        super.dispose();
        backgroundMusic.dispose();
        bitmapFontGenerator.dispose();
    }

    /**
     * Creates Menu Buttons and adds InputListeners
     */
    private void createMenuButtons(@NonNull Table table) {
        // Button to switch to NewGameScreen
        var showNewGameConfigButton = new VisTextButton("New game");
        showNewGameConfigButton.addListener(createChangeScreenInputListener(ScreenType.NEW_GAME));

        // Button to switch to LoadGameScreen
        //var loadGameMenuButton = new VisTextButton("Load game");
        //loadGameMenuButton.addListener(createChangeScreenInputListener(ScreenType.LOAD_GAME));

        // Button to switch to SettingsScreen
        var showSettingsButton = new VisTextButton("Settings");
        showSettingsButton.addListener(createChangeScreenInputListener(ScreenType.SETTINGS));

        // Button to switch to HighscoreScreen
        //var showHighscoresButton = new VisTextButton("Highscores");
        //showHighscoresButton.addListener(createChangeScreenInputListener(ScreenType.HIGHSCORE));

        // Button to switch to CreditsScreen
        var showCreditsButton = new VisTextButton("Credits");
        showCreditsButton.addListener(createChangeScreenInputListener(ScreenType.CREDITS));

        // Button to exit game
        var exitButton = new VisTextButton("Leave game");
        exitButton.addListener(new ClickInputListener(() -> Gdx.app.exit()));

        table.add(showNewGameConfigButton).padBottom(10).row();
        //table.add(loadGameMenuButton).padBottom(10).row();
        table.add(showSettingsButton).padBottom(10).row();
        //table.add(showHighscoresButton).padBottom(10).row();
        table.add(showCreditsButton).padBottom(10).row();
        table.add(exitButton);
    }
}
