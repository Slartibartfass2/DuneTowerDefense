package io.swapastack.dunetd.screens;

import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import io.swapastack.dunetd.DuneTD;

import lombok.NonNull;

public final class HighscoreScreen extends AbstractScreen {

    public HighscoreScreen(@NonNull DuneTD game) {
        super(game, ScreenColor.MAIN_BACKGROUND);
    }

    /**
     * Called when this screen becomes the current screen for a {@link DuneTD}.
     */
    @Override
    public void showScreen() {
        var table = new VisTable(true);
        table.setFillParent(true);

        var backToMainMenuButton = new VisTextButton("Back");
        backToMainMenuButton.addListener(createChangeScreenInputListener(ScreenType.MENU));

        table.add(backToMainMenuButton);

        addMainActor(table);
    }
}
