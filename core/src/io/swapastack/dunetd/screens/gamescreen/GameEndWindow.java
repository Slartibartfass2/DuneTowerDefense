package io.swapastack.dunetd.screens.gamescreen;

import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

import io.swapastack.dunetd.game.Statistics;
import io.swapastack.dunetd.math.PixelsConverter;
import io.swapastack.dunetd.screens.AbstractScreen;
import io.swapastack.dunetd.screens.ScreenEnum;
import io.swapastack.dunetd.screens.listeners.ClickInputListener;

import lombok.NonNull;

@SuppressWarnings("squid:S110")
public class GameEndWindow extends VisWindow {

    public GameEndWindow(@NonNull String title, @NonNull AbstractScreen screen, @NonNull Statistics statistics) {
        super(title + " - highscore:", true);

        var scrollPane = new VisScrollPane(statistics.getStatisticsTable());
        scrollPane.setFlickScroll(false);
        scrollPane.setFadeScrollBars(false);
        add(scrollPane).spaceTop(8).growX().row();

        var menuButton = new VisTextButton("Return back to main menu");
        menuButton.addListener(new ClickInputListener(() -> screen.getGame().changeScreen(ScreenEnum.MENU)));
        add(menuButton);

        setSize(PixelsConverter.getX(0.5f), PixelsConverter.getY(0.9f));
        centerWindow();
    }
}
