package io.swapastack.dunetd.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import io.swapastack.dunetd.config.Configuration;

public final class GameSettings {
    
    private final Preferences settings;
    private final int monitorCount;
    
    private static final int MAX_GRID_WIDTH = Configuration.getInstance().getIntProperty("MAX_GRID_WIDTH");
    private static final int MAX_GRID_HEIGHT = Configuration.getInstance().getIntProperty("MAX_GRID_HEIGHT");
    
    // Default settings
    private static final boolean DEFAULT_VSYNC = true;
    private static final int DEFAULT_MONITOR_INDEX = 0;
    private static final float DEFAULT_MASTER_VOLUME = 1f;
    private static final int DEFAULT_GRID_SIZE = 2;
    
    // Setting names
    private static final String VSYNC_SETTING_KEY = "vsync";
    private static final String MONITOR_SETTING_KEY = "monitor";
    private static final String MASTER_VOLUME_SETTING_KEY = "masterVolume";
    private static final String GRID_WIDTH_SETTING_KEY = "gridWidth";
    private static final String GRID_HEIGHT_SETTING_KEY = "gridHeight";
    
    public GameSettings() {
        settings = Gdx.app.getPreferences("settings");
        monitorCount = Gdx.graphics.getMonitors().length;
    }
    
    public boolean getVSync() {
        return settings.getBoolean(VSYNC_SETTING_KEY, DEFAULT_VSYNC);
    }
    
    public void setVSync(boolean vSync) {
        settings.putBoolean(VSYNC_SETTING_KEY, vSync);
        settings.flush();
    }
    
    public int getMonitorIndex() {
        try {
            return MathUtils.clamp(settings.getInteger(MONITOR_SETTING_KEY, DEFAULT_MONITOR_INDEX), 0, monitorCount);
        } catch (Exception ignored) { }
        settings.putInteger(MONITOR_SETTING_KEY, DEFAULT_MONITOR_INDEX);
        settings.flush();
        return DEFAULT_MONITOR_INDEX;
    }
    
    public void setMonitorIndex(int monitorIndex) {
        settings.putInteger(MONITOR_SETTING_KEY, MathUtils.clamp(monitorIndex, 0, monitorCount));
        settings.flush();
    }
    
    public float getMasterVolume() {
        try {
            return MathUtils.clamp(settings.getFloat(MASTER_VOLUME_SETTING_KEY, DEFAULT_MASTER_VOLUME), 0f, 1f);
        } catch (NumberFormatException ignored) {
        }
        settings.putFloat(MASTER_VOLUME_SETTING_KEY, DEFAULT_MASTER_VOLUME);
        settings.flush();
        return DEFAULT_MASTER_VOLUME;
    }
    
    public void setMasterVolume(float masterVolume) {
        settings.putFloat(MASTER_VOLUME_SETTING_KEY, MathUtils.clamp(masterVolume, 0f, 1f));
        settings.flush();
    }
    
    public int getGridWidth() {
        try {
            return MathUtils.clamp(settings.getInteger(GRID_WIDTH_SETTING_KEY, DEFAULT_GRID_SIZE), 2, MAX_GRID_WIDTH);
        } catch (NumberFormatException ignored) {
        }
        settings.putInteger(GRID_WIDTH_SETTING_KEY, DEFAULT_GRID_SIZE);
        settings.flush();
        return DEFAULT_GRID_SIZE;
    }
    
    public void setGridWidth(int gridWidth) {
        settings.putInteger(GRID_WIDTH_SETTING_KEY, MathUtils.clamp(gridWidth, 2, MAX_GRID_WIDTH));
        settings.flush();
    }
    
    public int getGridHeight() {
        try {
            return MathUtils.clamp(settings.getInteger(GRID_HEIGHT_SETTING_KEY, DEFAULT_GRID_SIZE), 2, MAX_GRID_HEIGHT);
        } catch (NumberFormatException ignored) {
        }
        settings.putInteger(GRID_HEIGHT_SETTING_KEY, DEFAULT_GRID_SIZE);
        settings.flush();
        return DEFAULT_GRID_SIZE;
    }
    
    public void setGridHeight(int gridHeight) {
        settings.putInteger(GRID_HEIGHT_SETTING_KEY, MathUtils.clamp(gridHeight, 2, MAX_GRID_HEIGHT));
        settings.flush();
    }
}
