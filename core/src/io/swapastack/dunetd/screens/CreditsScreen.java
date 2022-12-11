package io.swapastack.dunetd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.LinkLabel;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.assets.AssetLoader;
import io.swapastack.dunetd.screens.listeners.ChangeScreenInputListener;

import lombok.NonNull;

public class CreditsScreen extends AbstractScreen {

    public CreditsScreen(@NonNull DuneTD game) {
        super(game);

        var background = game.getAssetLoader().getDrawable(AssetLoader.DRAWABLE_BACKGROUND_NAME);

        var table = new VisTable(true);
        table.setFillParent(true);
        var stack = new Stack();

        // Adding Image to the stack
        var backgroundImage = new Image(game.getAssetLoader().getMainMenuBackgroundImage());
        backgroundImage.setScaling(Scaling.fill);
        stack.add(backgroundImage);

        var creditsTable = new VisTable(true);

        // Credits for infantry
        var infantryTable = getCreditsBox("Infantry:", "Low poly Cute Cyborg",
                "https://sketchfab.com/3d-models/low-poly-cute-cyborg-d2046c1fa5c141c287971d6de62dc796",
                "Shums", "https://sketchfab.com/starshums", background);
        creditsTable.add(infantryTable).padBottom(10).row();

        // Credits for boss unit
        var bossUnitTable = getCreditsBox("Boss Unit:", "Faceted Character (Locomotion Animation)",
                "https://sketchfab.com/3d-models/faceted-character-locomotion-animation-cf74a482394043c281efd56ad5484df4",
                "Fabian Orrego", "https://sketchfab.com/fabian_orrego", background);
        creditsTable.add(bossUnitTable).padBottom(10).row();

        // Credits for harvester
        var harvesterTable = getCreditsBox("Harvester:", "Spaceship : Orion",
                "https://sketchfab.com/3d-models/spaceship-orion-41de9d0d0eb74650b5fd98175d72fe71",
                "Rishav Gupta", "https://sketchfab.com/Rishav_Gupta", background);
        creditsTable.add(harvesterTable).padBottom(10).row();

        var kenneyTable = createKenneyTable(background);
        creditsTable.add(kenneyTable).padBottom(10).row();

        var kayKitTable = createKayKitTable(background);
        creditsTable.add(kayKitTable).padBottom(10).row();

        // Button to get back to the main menu
        var backToMainMenuButton = new VisTextButton("Back");
        backToMainMenuButton.addListener(new ChangeScreenInputListener(game, ScreenEnum.MENU));
        creditsTable.add(backToMainMenuButton).padBottom(10);

        stack.add(creditsTable);
        table.add(stack);
        stage.addActor(table);
    }

    private VisTable createKenneyTable(@NonNull Drawable background) {
        var kenneyTable = new VisTable(true);
        kenneyTable.setBackground(background);
        var kenneyLabel = new VisLabel("Tower assets by ");
        var kenneyLinkLabel = new LinkLabel("Kenney", "https://www.kenney.nl/assets/tower-defense-top-down");
        kenneyTable.add(kenneyLabel);
        kenneyTable.add(kenneyLinkLabel);
        return kenneyTable;
    }

    private VisTable createKayKitTable(@NonNull Drawable background) {
        var kayKitTable = new VisTable(true);
        kayKitTable.setBackground(background);
        var kayKitLabel = new VisLabel("Ground tile assets by ");
        var kayKitLinkLabel = new LinkLabel("KayKit", "https://kaylousberg.itch.io/kaykit-medieval-builder-pack");
        kayKitTable.add(kayKitLabel);
        kayKitTable.add(kayKitLinkLabel);
        return kayKitTable;
    }

    /**
     * Called when this screen becomes the current screen for a {@link DuneTD}.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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

    private VisTable getCreditsBox(@NonNull String title, @NonNull String originalName, @NonNull String urlToModel,
                                   @NonNull String creator, @NonNull String urlToProfile,
                                   @NonNull Drawable background) {
        var creditsTable = new VisTable(true);
        creditsTable.setBackground(background);

        var creditsTitle = new VisLabel(title);
        creditsTable.add(creditsTitle).row();

        var creditsLabel1 = new VisLabel("This work is based on ");
        var creditsLinkLabel1 = new LinkLabel("\"" + originalName + "\"", urlToModel);
        var creditsTable1 = new VisTable(true);
        creditsTable1.add(creditsLabel1);
        creditsTable1.add(creditsLinkLabel1);
        creditsTable.add(creditsTable1).row();

        var creditsLabel2 = new VisLabel(" by ");
        var creditsLinkLabel2 = new LinkLabel(creator, urlToProfile);
        var creditsTable2 = new VisTable(true);
        creditsTable2.add(creditsLabel2);
        creditsTable2.add(creditsLinkLabel2);
        creditsTable.add(creditsTable2).row();

        var creditsLabel3 = new VisLabel("licensed under ");
        var creditsLinkLabel3 = new LinkLabel("CC-BY-4.0", " https://creativecommons.org/licenses/by/4.0/");
        var creditsTable3 = new VisTable(true);
        creditsTable3.add(creditsLabel3);
        creditsTable3.add(creditsLinkLabel3);
        creditsTable.add(creditsTable3);
        return creditsTable;
    }
}
