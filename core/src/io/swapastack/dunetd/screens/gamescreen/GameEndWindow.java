package io.swapastack.dunetd.screens.gamescreen;

import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

import io.swapastack.dunetd.game.Statistics;
import io.swapastack.dunetd.math.PixelsConverter;
import io.swapastack.dunetd.screens.AbstractScreen;
import io.swapastack.dunetd.screens.ScreenType;

import lombok.NonNull;

@SuppressWarnings("squid:S110")
public class GameEndWindow extends VisWindow {

    private static final float WINDOW_WIDTH_PERCENTAGE = 0.5f;
    private static final float WINDOW_HEIGHT_PERCENTAGE = 0.9f;

    public GameEndWindow(@NonNull String title, @NonNull AbstractScreen screen, @NonNull Statistics statistics) {
        super(title + " - highscore:", true);

        var scrollPane = new VisScrollPane(statistics.getStatisticsTable());
        scrollPane.setFlickScroll(false);
        scrollPane.setFadeScrollBars(false);
        add(scrollPane).spaceTop(8).growX().row();

        var menuButton = new VisTextButton("Return back to main menu");
        menuButton.addListener(screen.createChangeScreenInputListener(ScreenType.MENU));
        add(menuButton);

        setSize(PixelsConverter.getX(WINDOW_WIDTH_PERCENTAGE), PixelsConverter.getY(WINDOW_HEIGHT_PERCENTAGE));
        centerWindow();
    }
}
