package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics.Lwjgl3DisplayMode;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.math.PixelsConverter;
import io.swapastack.dunetd.screens.listeners.ChangeScreenInputListener;
import io.swapastack.dunetd.screens.listeners.ClickInputListener;
import io.swapastack.dunetd.settings.MonitorSetting;

import lombok.NonNull;

public final class SettingsScreen extends AbstractScreen {

    public SettingsScreen(@NonNull DuneTD game) {
        super(game);
    }

    /*
    Setting Ideas:
    different audio sliders when there are more audio sources (master, music, GUI, Game effects)
    controls
    autosave on off
    delete highscores button
    more graphic settings (animation on off, extra graphics)
     */

    /**
     * Called when this screen becomes the current screen for a {@link DuneTD}.
     */
    @Override
    public void show() {
        // VSync checkbox
        var vsyncCheckbox = new VisCheckBox("Wait for VSync");
        vsyncCheckbox.setChecked(settings.getVSync());

        // Monitor selection
        var monitorSelectBox = new VisSelectBox<MonitorSetting>();

        // Get all available monitors
        addMonitorSettings(monitorSelectBox);

        // Volume sliders
        var masterVolumeSlider = createMasterVolumeSlider();

        // Buttons
        var backToMainMenuButton = new VisTextButton("Back");
        backToMainMenuButton.setPosition(backToMainMenuButton.getWidth() + 100, backToMainMenuButton.getHeight() + 100);
        backToMainMenuButton.addListener(new ChangeScreenInputListener(game, ScreenEnum.MENU));

        var saveSettingsButton = new VisTextButton("Apply settings");
        saveSettingsButton.setPosition(100f, 100f);
        saveSettingsButton.addListener(new ClickInputListener(() -> {
            settings.setVSync(vsyncCheckbox.isChecked());
            settings.setMonitorIndex(monitorSelectBox.getSelected().getIndex());
            Gdx.graphics.setFullscreenMode(monitorSelectBox.getSelected().getDisplayMode());
            settings.setMasterVolume(masterVolumeSlider.getValue() / 100f);
        }));

        // Create table and put everything together
        var table = new VisTable();

        // VSync checkbox
        table.add(vsyncCheckbox).left().row();

        // Monitor selection
        var monitorLabel = new VisLabel("Monitor: ");
        var monitorSelectionTable = new VisTable(true);
        monitorSelectionTable.add(monitorLabel).left();
        monitorSelectionTable.add(monitorSelectBox).left();
        table.add(monitorSelectionTable).left().row();

        // Volume sliders
        var masterVolumeTable = createMasterVolumeTable(masterVolumeSlider);
        table.add(masterVolumeTable).grow().left().row();

        // Buttons
        var buttonsTable = new VisTable(true);
        buttonsTable.add(backToMainMenuButton).left();
        buttonsTable.add(saveSettingsButton).right();
        table.add(buttonsTable).growX();

        table.setPosition(PixelsConverter.getX(0.5f), PixelsConverter.getY(0.5f));
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    private void addMonitorSettings(@NonNull VisSelectBox<MonitorSetting> monitorSelectBox) {
        var monitors = Gdx.graphics.getMonitors();
        var monitorSettings = new MonitorSetting[monitors.length];
        for (int i = 0; i < monitors.length; i++) {
            monitorSettings[i] = new MonitorSetting(i, (Lwjgl3DisplayMode) Gdx.graphics.getDisplayMode(monitors[i]));
        }
        monitorSelectBox.setItems(monitorSettings);
        monitorSelectBox.setSelectedIndex(settings.getMonitorIndex());
    }

    private VisSlider createMasterVolumeSlider() {
        float masterVolume = settings.getMasterVolume() * 100f;
        var masterVolumeSlider = new VisSlider(0f, 100f, 1f, false);
        masterVolumeSlider.setValue(masterVolume);

        return masterVolumeSlider;
    }

    private static VisTable createMasterVolumeTable(@NonNull VisSlider masterVolumeSlider) {
        var masterVolumeLabel = new VisLabel("Master volume: ");
        var masterVolumeTable = new VisTable(true);
        var masterVolumePercentage = new VisLabel(String.format(" %3.0f%%", masterVolumeSlider.getValue()));
        masterVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                masterVolumePercentage.setText(String.format(" %3.0f%%", masterVolumeSlider.getValue()));
            }
        });

        masterVolumeTable.add(masterVolumeLabel).left();
        masterVolumeTable.add(masterVolumeSlider).grow().left();
        masterVolumeTable.add(masterVolumePercentage).left();
        return masterVolumeTable;
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(ScreenColors.BACKGROUND_COLOR_RED, ScreenColors.BACKGROUND_COLOR_GREEN,
                ScreenColors.BACKGROUND_COLOR_BLUE, ScreenColors.BACKGROUND_COLOR_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
}
