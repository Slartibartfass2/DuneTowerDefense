package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTextButton;
import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.screens.listeners.ChangeScreenInputListener;
import lombok.NonNull;

public final class HighscoreScreen extends AbstractScreen {
    
    public HighscoreScreen(@NonNull DuneTD game) {
        super(game);
    }
    
    /**
     * Called when this screen becomes the current screen for a {@link DuneTD}.
     */
    @Override
    public void show() {
        Table table = new Table();
        table.setFillParent(true);
    
        VisTextButton backToMainMenuButton = new VisTextButton("Back");
    
        table.add(backToMainMenuButton);
    
        stage.addActor(table);
    
        Gdx.input.setInputProcessor(stage);
    
        // Add Listeners
        backToMainMenuButton.addListener(new ChangeScreenInputListener(game, ScreenEnum.MENU));
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
