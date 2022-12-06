package io.swapastack.dunetd.settings;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics.Lwjgl3DisplayMode;
import lombok.Getter;
import lombok.NonNull;

@SuppressWarnings("ClassCanBeRecord")
public final class MonitorSetting {

    @Getter
    private final int index;
    @Getter
    private final Lwjgl3DisplayMode displayMode;

    public MonitorSetting(int index, @NonNull Lwjgl3DisplayMode displayMode) {
        this.index = index;
        this.displayMode = displayMode;
    }

    @Override
    public String toString() {
        return String.format("Monitor %d (%dx%d, %dHz)", index, displayMode.width, displayMode.height,
                displayMode.refreshRate);
    }
}
