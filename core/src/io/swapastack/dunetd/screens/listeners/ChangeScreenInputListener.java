package io.swapastack.dunetd.screens.listeners;

import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.screens.ScreenEnum;
import lombok.NonNull;

public final class ChangeScreenInputListener extends ClickInputListener {

    public ChangeScreenInputListener(@NonNull DuneTD game, @NonNull ScreenEnum toScreenEnum) {
        super(() -> game.changeScreen(toScreenEnum));
    }
}