package io.swapastack.dunetd.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import io.swapastack.dunetd.config.Configuration;

public final class GameSettings {

    private final Preferences settings;
    private final int maxMonitorIndex;

    private static final int MIN_GRID_DIMENSION = 2;
    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");

    // Default settings
    private static final boolean DEFAULT_VSYNC = true;
    private static final int DEFAULT_MONITOR_INDEX = 0;
    private static final float DEFAULT_MASTER_VOLUME = 0.5f;
    private static final int DEFAULT_GRID_SIZE = 2;

    // Setting names
    private static final String VSYNC_SETTING_KEY = "vsync";
    private static final String MONITOR_SETTING_KEY = "monitor";
    private static final String MASTER_VOLUME_SETTING_KEY = "masterVolume";
    private static final String GRID_WIDTH_SETTING_KEY = "gridWidth";
    private static final String GRID_HEIGHT_SETTING_KEY = "gridHeight";

    public GameSettings() {
        settings = Gdx.app.getPreferences("io.swapastack.dunetd.settings");
        maxMonitorIndex = Gdx.graphics.getMonitors().length - 1;
    }

    public boolean getVSync() {
        return settings.getBoolean(VSYNC_SETTING_KEY, DEFAULT_VSYNC);
    }

    public void setVSync(boolean vSync) {
        settings.putBoolean(VSYNC_SETTING_KEY, vSync);
        settings.flush();
    }

    public int getMonitorIndex() {
        var value = settings.getInteger(MONITOR_SETTING_KEY, DEFAULT_MONITOR_INDEX);
        return MathUtils.clamp(value, 0, maxMonitorIndex);
    }

    public void setMonitorIndex(int monitorIndex) {
        settings.putInteger(MONITOR_SETTING_KEY, MathUtils.clamp(monitorIndex, 0, maxMonitorIndex));
        settings.flush();
    }

    public float getMasterVolume() {
        var value = settings.getFloat(MASTER_VOLUME_SETTING_KEY, DEFAULT_MASTER_VOLUME);
        return MathUtils.clamp(value, 0f, 1f);
    }

    public void setMasterVolume(float masterVolume) {
        settings.putFloat(MASTER_VOLUME_SETTING_KEY, MathUtils.clamp(masterVolume, 0f, 1f));
        settings.flush();
    }

    public int getGridWidth() {
        var value = settings.getInteger(GRID_WIDTH_SETTING_KEY, DEFAULT_GRID_SIZE);
        return MathUtils.clamp(value, MIN_GRID_DIMENSION, MAX_GRID_WIDTH);
    }

    public void setGridWidth(int gridWidth) {
        settings.putInteger(GRID_WIDTH_SETTING_KEY, MathUtils.clamp(gridWidth, 2, MAX_GRID_WIDTH));
        settings.flush();
    }

    public int getGridHeight() {
        var value = settings.getInteger(GRID_HEIGHT_SETTING_KEY, DEFAULT_GRID_SIZE);
        return MathUtils.clamp(value, MIN_GRID_DIMENSION, MAX_GRID_HEIGHT);
    }

    public void setGridHeight(int gridHeight) {
        settings.putInteger(GRID_HEIGHT_SETTING_KEY, MathUtils.clamp(gridHeight, 2, MAX_GRID_HEIGHT));
        settings.flush();
    }
}
