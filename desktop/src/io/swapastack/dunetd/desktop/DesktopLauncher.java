package io.swapastack.dunetd.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.swapastack.dunetd.DuneTD;

public final class DesktopLauncher {

    public static void main(String[] arg) {
        // Set new config
        var config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("Dune-TD - Sopra 2021 / 2022");
        config.useVsync(false); // first disable vsync for loading screen, later turn it on
        config.setResizable(true);
        config.setWindowIcon("icons/icon.png");

        var displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
        config.setFullscreenMode(displayMode);

        new Lwjgl3Application(new DuneTD(), config);
    }
}