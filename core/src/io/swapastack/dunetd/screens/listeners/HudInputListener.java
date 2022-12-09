package io.swapastack.dunetd.screens.listeners;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import lombok.NonNull;

public final class HudInputListener extends InputListener {

    private final IntegerInputCommand mouseClickInputCommand;
    private final IntegerInputCommand scrollInputCommand;
    private final IntegerInputCommand keyInputCommand;

    public HudInputListener(@NonNull IntegerInputCommand mouseClickInputCommand,
                            @NonNull IntegerInputCommand scrollInputCommand,
                            @NonNull IntegerInputCommand keyInputCommand) {
        this.mouseClickInputCommand = mouseClickInputCommand;
        this.scrollInputCommand = scrollInputCommand;
        this.keyInputCommand = keyInputCommand;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        mouseClickInputCommand.execute(button);
    }

    @Override
    public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
        scrollInputCommand.execute((int) amountY);
        return true;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        keyInputCommand.execute(keycode);
        return true;
    }
}
