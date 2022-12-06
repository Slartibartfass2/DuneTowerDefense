package io.swapastack.dunetd.screens.gamescreen;

import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import io.swapastack.dunetd.math.PixelsConverter;
import io.swapastack.dunetd.screens.AbstractScreen;
import io.swapastack.dunetd.screens.ScreenEnum;
import io.swapastack.dunetd.screens.listeners.ChangeScreenInputListener;
import io.swapastack.dunetd.screens.listeners.ClickInputListener;
import lombok.NonNull;

@SuppressWarnings("squid:S110")
public final class EscapeMenu extends VisWindow {

    private static final float PADDING_HORIZONTAL = 0.02325f;
    private static final float PADDING_VERTICAL = 0.1041f;
    private static final float SPACING = 0.01388f;

    public EscapeMenu(@NonNull AbstractScreen screen, @NonNull ClickInputListener backToGameCommand) {
        super("Game paused", true);

        getTitleLabel().setAlignment(Align.center);
        getTitleTable().pack();

        // Button which brings the user back to the game
        var backToGameButton = new VisTextButton("Resume to game");
        backToGameButton.addListener(backToGameCommand);

        // Button to save the game
        //var saveGameButton = new VisTextButton("Save game");

        // Button to show the settings
        //var settingsButton = new VisTextButton("Settings");

        // Button to get back to the main menu
        var menuButton = new VisTextButton("Back to main menu");
        menuButton.addListener(new ChangeScreenInputListener(screen.getGame(), ScreenEnum.MENU));

        var padHorizontal = PixelsConverter.getX(PADDING_HORIZONTAL);
        var padVertical = PixelsConverter.getY(PADDING_VERTICAL);
        var spacing = PixelsConverter.getY(SPACING);
        add(backToGameButton).pad(padVertical - getTitleTable().getHeight(), padHorizontal, spacing, padHorizontal).row();
        //add(saveGameButton).pad(0f, padHorizontal, spacing, padHorizontal).row();
        //add(settingsButton).pad(0f, padHorizontal, spacing, padHorizontal).row();
        add(menuButton).pad(0f, padHorizontal, padVertical, padHorizontal).row();
        pack();
        centerWindow();
        setVisible(false);
    }
}
