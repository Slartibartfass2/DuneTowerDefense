package io.swapastack.dunetd.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import io.swapastack.dunetd.config.Configuration;
import io.swapastack.dunetd.entities.towers.TowerEnum;
import io.swapastack.dunetd.game.GameHandler;
import io.swapastack.dunetd.math.PixelsConverter;
import io.swapastack.dunetd.screens.listeners.ClickInputListener;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import static io.swapastack.dunetd.entities.towers.TowerEnum.*;

@SuppressWarnings("squid:S110")
public final class ToolBar extends VisTable {

    // Tower constants
    private static final float GUARD_TOWER_RANGE = Configuration.getInstance().getFloatProperty("GUARD_TOWER_RANGE");
    private static final int GUARD_TOWER_BUILD_COST = Configuration.getInstance().getIntProperty("GUARD_TOWER_BUILD_COST");
    private static final int GUARD_TOWER_DAMAGE = Configuration.getInstance().getIntProperty("GUARD_TOWER_DAMAGE");
    private static final int GUARD_TOWER_RELOAD_TIME_IN_MS = Configuration.getInstance().getIntProperty("GUARD_TOWER_RELOAD_TIME_IN_MS");

    private static final float BOMB_TOWER_RANGE = Configuration.getInstance().getFloatProperty("BOMB_TOWER_RANGE");
    private static final int BOMB_TOWER_BUILD_COST = Configuration.getInstance().getIntProperty("BOMB_TOWER_BUILD_COST");
    private static final int BOMB_TOWER_DAMAGE = Configuration.getInstance().getIntProperty("BOMB_TOWER_DAMAGE");
    private static final int BOMB_TOWER_RELOAD_TIME_IN_MS = Configuration.getInstance().getIntProperty("BOMB_TOWER_RELOAD_TIME_IN_MS");

    private static final float SOUND_TOWER_RANGE = Configuration.getInstance().getFloatProperty("SOUND_TOWER_RANGE");
    private static final int SOUND_TOWER_BUILD_COST = Configuration.getInstance().getIntProperty("SOUND_TOWER_BUILD_COST");
    private static final int SOUND_TOWER_RELOAD_TIME_IN_MS = Configuration.getInstance().getIntProperty("SOUND_TOWER_RELOAD_TIME_IN_MS");
    private static final float SLOWING_EFFECT_MULTIPLIER = Configuration.getInstance().getFloatProperty("SOUND_TOWER_SLOWING_EFFECT_MULTIPLIER");

    private static final float BORDER_WIDTH = 125f;
    private static final float BORDER_HEIGHT = 21.5f;
    private static final float BUTTON_PADDING_PERCENTAGE = 0.01f;
    private static final float TOOLBAR_Y_OFFSET_PERCENTAGE = 0.01f;

    private final GameHandler gameHandler;

    // Toolbar actors
    private final VisLabel spiceLabel;
    private final VisLabel healthLabel;
    private final TowerSelectButton guardTowerButton;
    private final TowerSelectButton bombTowerButton;
    private final TowerSelectButton soundTowerButton;
    @Getter
    private final ShaiHuludSelectButton shaiHuludButton;

    @Getter
    private TowerEnum selectedTower;
    @Getter
    private boolean shaiHuludSelected;
    @Setter
    private boolean frozenInput;

    public ToolBar(@NonNull GameHandler gameHandler, @NonNull Drawable background, @NonNull Drawable guardTowerDrawable,
                   @NonNull Drawable bombTowerDrawable, @NonNull Drawable soundTowerDrawable,
                   @NonNull Drawable shaiHuludDrawable) {
        this.gameHandler = gameHandler;
        selectedTower = null;
        frozenInput = false;

        // Spice and health info
        spiceLabel = new VisLabel();
        healthLabel = new VisLabel();
        var playerInfoTable = new VisTable(true);
        playerInfoTable.add(spiceLabel).left().row();
        playerInfoTable.add(healthLabel).left();

        // Tower selection
        guardTowerButton = new TowerSelectButton("Guard Tower", GUARD_TOWER_BUILD_COST, GUARD_TOWER_RANGE,
                GUARD_TOWER_RELOAD_TIME_IN_MS, GUARD_TOWER_DAMAGE, guardTowerDrawable);
        guardTowerButton.addListener(new ClickInputListener(() -> setSelectedTower(GUARD_TOWER)));
        bombTowerButton = new TowerSelectButton("Bomb Tower", BOMB_TOWER_BUILD_COST, BOMB_TOWER_RANGE,
                BOMB_TOWER_RELOAD_TIME_IN_MS, BOMB_TOWER_DAMAGE, bombTowerDrawable);
        bombTowerButton.addListener(new ClickInputListener(() -> setSelectedTower(BOMB_TOWER)));
        soundTowerButton = new TowerSelectButton("Sound Tower", SOUND_TOWER_BUILD_COST, SOUND_TOWER_RANGE,
                SOUND_TOWER_RELOAD_TIME_IN_MS, SLOWING_EFFECT_MULTIPLIER, soundTowerDrawable);
        soundTowerButton.addListener(new ClickInputListener(() -> setSelectedTower(SOUND_TOWER)));

        // Shai hulud selection
        shaiHuludButton = new ShaiHuludSelectButton(shaiHuludDrawable);
        shaiHuludButton.addListener(new ClickInputListener(this::selectShaiHulud));

        var padX = PixelsConverter.getX(BUTTON_PADDING_PERCENTAGE);

        add(playerInfoTable).top().padRight(padX);
        add(guardTowerButton).top().padLeft(padX).padRight(padX);
        add(bombTowerButton).top().padLeft(padX).padRight(padX);
        add(soundTowerButton).top().padLeft(padX).padRight(padX);
        add(shaiHuludButton).top().padLeft(padX);
        pad(BORDER_HEIGHT, BORDER_WIDTH, BORDER_HEIGHT, BORDER_WIDTH);
        setBackground(background);
        pack();

        var yOffset = PixelsConverter.getY(TOOLBAR_Y_OFFSET_PERCENTAGE);
        var xCenter = Gdx.graphics.getWidth() / 2f - getWidth() / 2f;
        setPosition(xCenter, yOffset);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        spiceLabel.setText("Spice: " + gameHandler.getPlayerSpice());
        healthLabel.setText("Health: " + gameHandler.getPlayerHealth());

        guardTowerButton.setDisabled(frozenInput);
        bombTowerButton.setDisabled(frozenInput);
        soundTowerButton.setDisabled(frozenInput);
        shaiHuludButton.setDisabled(frozenInput);
    }

    public void setSelectedTower(TowerEnum towerEnum) {
        if (frozenInput) {
            return;
        }

        if (selectedTower == towerEnum || towerEnum == null) {
            selectedTower = null;
            guardTowerButton.setChecked(false);
            bombTowerButton.setChecked(false);
            soundTowerButton.setChecked(false);
            return;
        }

        selectedTower = towerEnum;
        switch (towerEnum) {
            case GUARD_TOWER -> {
                guardTowerButton.setChecked(true);
                bombTowerButton.setChecked(false);
                soundTowerButton.setChecked(false);
                shaiHuludButton.setChecked(false);
            }
            case BOMB_TOWER -> {
                guardTowerButton.setChecked(false);
                bombTowerButton.setChecked(true);
                soundTowerButton.setChecked(false);
                shaiHuludButton.setChecked(false);
            }
            case SOUND_TOWER -> {
                guardTowerButton.setChecked(false);
                bombTowerButton.setChecked(false);
                soundTowerButton.setChecked(true);
                shaiHuludButton.setChecked(false);
            }
        }
        shaiHuludSelected = false;
    }

    public void selectShaiHulud() {
        if (frozenInput) {
            return;
        }

        shaiHuludSelected ^= true;

        shaiHuludButton.setChecked(shaiHuludSelected);

        if (shaiHuludSelected) {
            guardTowerButton.setChecked(false);
            bombTowerButton.setChecked(false);
            soundTowerButton.setChecked(false);
            selectedTower = null;
        }
    }

    public boolean isMouseOnToolBar() {
        int x = Gdx.graphics.getWidth() - Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();
        return x >= getX() && x <= getX() + getWidth() && y >= getY() && y <= getY() + getHeight();
    }
}
