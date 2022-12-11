package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import io.swapastack.dunetd.DuneTD;

import lombok.NonNull;

/**
 * Source:
 * <a href="https://github.com/Matsemann/libgdx-loading-screen/blob/libgdx-1.9.9-LaurenceWarne/core/src/com/matsemann/libgdxloadingscreen/screen/LoadingScreen.java">https://github.com/Matsemann/libgdx-loading-screen/blob/libgdx-1.9.9-LaurenceWarne/core/src/com/matsemann/libgdxloadingscreen/screen/LoadingScreen.java</a>
 *
 * @author Mats Svensson & Laurence Warne
 */
public final class LoadingScreen extends AbstractScreen {

    private static final String LOADING_SCREEN_PACK_PATH = "loading_screen/loading.pack";

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image screenBg;
    private Image loadingBg;

    private float startX;
    private float endX;
    private float percent;

    private Actor loadingBar;

    private final AssetManager assetManager;

    public LoadingScreen(@NonNull DuneTD game) {
        super(game, ScreenColor.MAIN_BACKGROUND);
        this.assetManager = game.getAssetLoader().getAssetManager();
    }

    /**
     * Called when this screen becomes the current screen for a {@link DuneTD}.
     */
    @Override
    public void showScreen() {
        // Load assets for loading screen and wait until its finished
        assetManager.load(LOADING_SCREEN_PACK_PATH, TextureAtlas.class);
        assetManager.finishLoading();

        // Get the texture atlas from the manager
        var atlas = assetManager.get(LOADING_SCREEN_PACK_PATH, TextureAtlas.class);

        // Grab the regions from the atlas and create some images
        logo = new Image(atlas.findRegion("libgdx-logo"));
        loadingFrame = new Image(atlas.findRegion("loading-frame"));
        loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
        screenBg = new Image(atlas.findRegion("screen-bg"));
        loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

        // Add the loading bar animation
        var anim = new Animation<TextureRegion>(0.05f, atlas.findRegions("loading-bar-anim"));
        anim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        loadingBar = new LoadingBar(anim);

        // Add all the actors to the stage
        var stage = getStage();
        stage.addActor(screenBg);
        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);

        // Load all game assets
        getGame().getAssetLoader().loadGameAssets();
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void renderScreen(float delta) {
        // If config file was read and the game assets are done loading show MainMenuScreen
        if (assetManager.update()) {
            getGame().getAssetLoader().createSceneAssets();
            // Turn VSync on now that all assets are loaded
            Gdx.graphics.setVSync(getSettings().getVSync());

            // Change to MainMenuScreen
            getGame().changeScreen(ScreenType.MENU);
        }

        // Interpolate the percentage to make it more smooth
        percent = Interpolation.linear.apply(percent, assetManager.getProgress(), 0.1f);

        // Update positions (and size) to match the percentage
        loadingBarHidden.setX(startX + endX * percent);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setWidth(450 - 450 * percent);
        loadingBg.invalidate();
    }

    /**
     * Called when the {@link DuneTD} is resized. This can happen at any point during a non-paused state but will never
     * happen before a call to {@link DuneTD#create()}.
     *
     * @param width  the new width in pixels
     * @param height the new height in pixels
     */
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        // Make the background fill the screen
        screenBg.setSize(getStageWidth(), getStageHeight());

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((getStageWidth() - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((getStageHeight() - loadingFrame.getHeight()) / 2);

        // Place the loading bar at the same spot as the frame, adjusted a few px
        loadingBar.setX(loadingFrame.getX() + 15);
        loadingBar.setY(loadingFrame.getY() + 5);

        // Place the logo in the middle of the screen
        logo.setX((getStageWidth() - logo.getWidth()) / 2);
        logo.setY(loadingFrame.getY() + loadingFrame.getHeight() + 15);

        // Place the image that will hide the bar on top of the bar, adjusted a few px
        loadingBarHidden.setX(loadingBar.getX() + 35);
        loadingBarHidden.setY(loadingBar.getY() - 3);
        // The start position and how far to move the hidden loading bar
        startX = loadingBarHidden.getX();
        endX = 440;

        // The rest of the hidden bar
        loadingBg.setSize(450, 50);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setY(loadingBarHidden.getY() + 3);
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        super.dispose();
        // Dispose the loading assets as we no longer need them
        assetManager.unload(LOADING_SCREEN_PACK_PATH);
    }
}
