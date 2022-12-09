package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisTextButton;
import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.assets.AssetLoader;
import io.swapastack.dunetd.savegame.SaveGame;
import io.swapastack.dunetd.screens.listeners.ChangeScreenInputListener;

import java.util.ArrayList;

public final class LoadGameScreen extends AbstractScreen {

    private SaveGame lastClickedSaveGame;
    private long lastClickTimestamp = Long.MIN_VALUE;

    public LoadGameScreen(final DuneTD game) {
        super(game);
    }

    /**
     * Called when this screen becomes the current screen for a {@link DuneTD}.
     */
    @Override
    public void show() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        // Create SampleData
        // TODO: special treatment when list is empty
        var array = new ArrayList<SaveGame>();
        array.add(new SaveGame("SaveGame1", 12083091));
        array.add(new SaveGame("SaveGame2", 12083091));

        var backgroundDrawable = game.getAssetLoader().getDrawable(AssetLoader.DRAWABLE_BACKGROUND_NAME);
        var selectionDrawable = game.getAssetLoader().getDrawable(AssetLoader.DRAWABLE_SELECTION_NAME);
        var adapter = new ListViewAdapter(array, backgroundDrawable, selectionDrawable);
        var saveGameList = new ListView<>(adapter);

        saveGameList.getMainTable().setWidth(width / 4f);
        saveGameList.getMainTable().setHeight(height / 2.25f);
        var saveGameListX = width / 2f - saveGameList.getMainTable().getWidth() / 2f;
        var saveGameListY = height / 2f - saveGameList.getMainTable().getHeight() / 2f;
        saveGameList.getMainTable().setPosition(saveGameListX, saveGameListY);
        saveGameList.setItemClickListener(this::handleSaveGameClick);
        stage.addActor(saveGameList.getMainTable());

        var backToMainMenuButton = new VisTextButton("Back");
        backToMainMenuButton.setPosition(saveGameListX, saveGameListY - backToMainMenuButton.getHeight() - 10f);
        backToMainMenuButton.addListener(new ChangeScreenInputListener(game, ScreenEnum.MENU));
        stage.addActor(backToMainMenuButton);

        var loadGameButton = new VisTextButton("Load game");
        loadGameButton.setPosition(saveGameListX + saveGameList.getMainTable().getWidth() - loadGameButton.getWidth(),
                saveGameListY - loadGameButton.getHeight() - 10f);
        loadGameButton.addListener(new ChangeScreenInputListener(game, ScreenEnum.GAME));
        stage.addActor(loadGameButton);

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2353f, 0.2471f, 0.2549f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    public void handleSaveGameClick(SaveGame saveGame) {
        var timeNow = System.currentTimeMillis();
        var timePassed = timeNow - lastClickTimestamp;

        if (saveGame == lastClickedSaveGame && timePassed <= 500) {
            // TODO: Load game
        } else {
            lastClickedSaveGame = saveGame;
            lastClickTimestamp = timeNow;
        }
    }
}
