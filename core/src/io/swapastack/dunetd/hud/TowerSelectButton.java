package io.swapastack.dunetd.hud;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.widget.Tooltip;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import lombok.NonNull;

@SuppressWarnings("squid:S110")
public class TowerSelectButton extends VisTable {

    private VisImageButton imageButton;

    public TowerSelectButton(@NonNull String towerName, int buildCost, float range, int reloadTimeInMs, int damage,
                             @NonNull Drawable imageUp) {
        super(true);
        var description = String.format("Build cost: %d Spice%nRange: %1.1f%nReload time: %dms%nDamage: %d",
                buildCost, range, reloadTimeInMs, damage);
        createTowerSelectButton(imageUp, description, towerName);
    }

    public TowerSelectButton(@NonNull String towerName, int buildCost, float range, int reloadTimeInMs,
                             float slowingEffect, @NonNull Drawable imageUp) {
        super(true);
        var description = String.format("Build cost: %d Spice%nRange: %1.1f%nReload time: %dms%nSlowing effect: %3" +
                ".1f%%", buildCost, range, reloadTimeInMs, slowingEffect * 100);
        createTowerSelectButton(imageUp, description, towerName);
    }

    private void createTowerSelectButton(@NonNull Drawable image, @NonNull String description, @NonNull String name) {
        imageButton = new VisImageButton(image, null, null, "toggle");
        imageButton.setFocusBorderEnabled(false);
        var toolTip = new Tooltip.Builder(description).target(imageButton).build();
        toolTip.setAppearDelayTime(0.1f);
        toolTip.setFadeTime(0.05f);

        var towerLabel = new VisLabel(name);

        add(imageButton).center().row();
        add(towerLabel).center();
    }

    public void setChecked(boolean checked) {
        imageButton.setChecked(checked);
    }

    public void setDisabled(boolean disabled) {
        imageButton.setDisabled(disabled);
    }
}
