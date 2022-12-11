package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.widget.ListView;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.assets.AssetLoader;
import io.swapastack.dunetd.savegame.SaveGame;

import java.util.ArrayList;

public final class LoadGameScreen extends AbstractScreen {

    private SaveGame lastClickedSaveGame;
    private long lastClickTimestamp = Long.MIN_VALUE;
    private final Drawable backgroundDrawable;
    private final Drawable selectionDrawable;

    public LoadGameScreen(final DuneTD game) {
        super(game, ScreenColor.MAIN_BACKGROUND);

        backgroundDrawable = game.getAssetLoader().getDrawable(AssetLoader.DRAWABLE_BACKGROUND_NAME);
        selectionDrawable = game.getAssetLoader().getDrawable(AssetLoader.DRAWABLE_SELECTION_NAME);
    }

    /**
     * Called when this screen becomes the current screen for a {@link DuneTD}.
     */
    @Override
    public void showScreen() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        // Create SampleData
        // TODO: special treatment when list is empty
        var array = new ArrayList<SaveGame>();
        array.add(new SaveGame("SaveGame1", 12083091));
        array.add(new SaveGame("SaveGame2", 12083091));

        var adapter = new ListViewAdapter(array, backgroundDrawable, selectionDrawable);
        var saveGameList = new ListView<>(adapter);

        saveGameList.getMainTable().setWidth(width / 4f);
        saveGameList.getMainTable().setHeight(height / 2.25f);
        var saveGameListX = width / 2f - saveGameList.getMainTable().getWidth() / 2f;
        var saveGameListY = height / 2f - saveGameList.getMainTable().getHeight() / 2f;
        saveGameList.getMainTable().setPosition(saveGameListX, saveGameListY);
        saveGameList.setItemClickListener(this::handleSaveGameClick);

        var table = new VisTable(true);
        table.addActor(saveGameList.getMainTable());

        var backToMainMenuButton = new VisTextButton("Back");
        backToMainMenuButton.setPosition(saveGameListX, saveGameListY - backToMainMenuButton.getHeight() - 10f);
        backToMainMenuButton.addListener(createChangeScreenInputListener(ScreenType.MENU));
        table.addActor(backToMainMenuButton);

        var loadGameButton = new VisTextButton("Load game");
        loadGameButton.setPosition(saveGameListX + saveGameList.getMainTable().getWidth() - loadGameButton.getWidth(),
                saveGameListY - loadGameButton.getHeight() - 10f);
        loadGameButton.addListener(createChangeScreenInputListener(ScreenType.GAME));
        table.addActor(loadGameButton);

        addMainActor(table);
    }

    private void handleSaveGameClick(SaveGame saveGame) {
        var timeNow = System.currentTimeMillis();
        var timePassed = timeNow - lastClickTimestamp;

        if (saveGame != lastClickedSaveGame || timePassed > 500) {
            lastClickedSaveGame = saveGame;
            lastClickTimestamp = timeNow;
        }
        // TODO: else -> Load game
    }
}
