package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisTextButton;
import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.savegame.SaveGame;
import io.swapastack.dunetd.screens.listeners.ChangeScreenInputListener;

import java.util.ArrayList;

import static io.swapastack.dunetd.assets.AssetLoader.DRAWABLE_BACKGROUND_NAME;
import static io.swapastack.dunetd.assets.AssetLoader.DRAWABLE_SELECTION_NAME;

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
        array.add(new SaveGame("SaveGame3", 12083091));
        array.add(new SaveGame("SaveGame4", 12083091));
        array.add(new SaveGame("SaveGame5", 12083091));
        array.add(new SaveGame("SaveGame6", 12083091));
        array.add(new SaveGame("SaveGame7", 12083091));
        array.add(new SaveGame("SaveGame8", 12083091));
        array.add(new SaveGame("SaveGame9", 12083091));
        array.add(new SaveGame("SaveGame10", 12083091));
        array.add(new SaveGame("SaveGame11", 12083091));
        array.add(new SaveGame("SaveGame12", 12083091));
        array.add(new SaveGame("SaveGame13", 12083091));
        array.add(new SaveGame("SaveGame14", 12083091));
        array.add(new SaveGame("SaveGame15", 12083091));
        array.add(new SaveGame("SaveGame16", 12083091));
        array.add(new SaveGame("SaveGame17", 12083091));
        array.add(new SaveGame("SaveGame18", 12083091));
        array.add(new SaveGame("SaveGame19", 12083091));
        array.add(new SaveGame("SaveGame20", 12083091));

        var backgroundDrawable = game.getAssetLoader().getDrawable(DRAWABLE_BACKGROUND_NAME);
        var selectionDrawable = game.getAssetLoader().getDrawable(DRAWABLE_SELECTION_NAME);
        var adapter = new ListViewAdapter(array, backgroundDrawable, selectionDrawable);
        var saveGameList = new ListView<>(adapter);

        saveGameList.getMainTable().setWidth(width / 4f);
        saveGameList.getMainTable().setHeight(height / 2.25f);
        var saveGameListX = width / 2f - saveGameList.getMainTable().getWidth() / 2f;
        var saveGameListY = height / 2f - saveGameList.getMainTable().getHeight() / 2f;
        saveGameList.getMainTable().setPosition(saveGameListX, saveGameListY);
        saveGameList.setItemClickListener(item -> {
            var timeNow = System.currentTimeMillis();
            var timePassed = timeNow - lastClickTimestamp;

            if (item == lastClickedSaveGame && timePassed <= 500) {
                // Load game
            } else {
                lastClickedSaveGame = item;
                lastClickTimestamp = timeNow;
            }
        });
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
}
