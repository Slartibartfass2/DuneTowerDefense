package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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
import io.swapastack.dunetd.screens.listeners.ChangeScreenInputListener;
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

    private final FreeTypeFontGenerator bitmapFontGenerator;
    private final Music backgroundMusic;

    public MainMenuScreen(@NonNull DuneTD game) {
        super(game);

        var table = new VisTable(true);
        table.setFillParent(true);
        var stack = new Stack();

        // Adding Image to the stack
        var backgroundImage = new Image(game.getAssetLoader().getMainMenuBackgroundImage());
        backgroundImage.setScaling(Scaling.fill);
        stack.add(backgroundImage);

        var menuTable = new VisTable(true);

        // Create string for BitmapFont and Label creation
        var duneTD = "Dune TD";

        // Initialize FreeTypeFontGenerator for BitmapFont generation
        bitmapFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/NotoSansCJKtc_ttf/NotoSansCJKtc-Bold.ttf"));
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
        stage.addActor(table);

        // Load background music
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("piano/piano_loop.wav"));
        var masterVolume = settings.getMasterVolume();
        if (masterVolume > 0) {
            backgroundMusic.setVolume(masterVolume * VOLUME_MULTIPLIER);
            backgroundMusic.setLooping(true);
            backgroundMusic.play();
        }
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
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // clear the client area (Screen) with the clear color (black)
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
        showNewGameConfigButton.addListener(new ChangeScreenInputListener(game, ScreenEnum.NEW_GAME));

        // Button to switch to LoadGameScreen
        //var loadGameMenuButton = new VisTextButton("Load game");
        //loadGameMenuButton.addListener(new ChangeScreenInputListener(game, ScreenEnum.LOAD_GAME));

        // Button to switch to SettingsScreen
        var showSettingsButton = new VisTextButton("Settings");
        showSettingsButton.addListener(new ChangeScreenInputListener(game, ScreenEnum.SETTINGS));

        // Button to switch to HighscoreScreen
        //var showHighscoresButton = new VisTextButton("Highscores");
        //showHighscoresButton.addListener(new ChangeScreenInputListener(game, ScreenEnum.HIGHSCORE));

        // Button to switch to CreditsScreen
        var showCreditsButton = new VisTextButton("Credits");
        showCreditsButton.addListener(new ChangeScreenInputListener(game, ScreenEnum.CREDITS));

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
