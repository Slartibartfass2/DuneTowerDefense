package io.swapastack.dunetd.screens.listeners;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import lombok.NonNull;

public class ClickInputListener extends InputListener {

    private final InputCommand command;

    public ClickInputListener(@NonNull InputCommand command) {
        this.command = command;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        command.execute();
    }
}
