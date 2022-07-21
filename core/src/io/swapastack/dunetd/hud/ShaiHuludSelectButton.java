package io.swapastack.dunetd.hud;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import lombok.NonNull;

@SuppressWarnings("squid:S110")
public class ShaiHuludSelectButton extends VisTable {

    private final VisImageButton imageButton;

    public ShaiHuludSelectButton(@NonNull Drawable imageUp) {
        super(true);

        imageButton = new VisImageButton(imageUp, null, null, "toggle");
        imageButton.setFocusBorderEnabled(false);

        var shaiHuludLabel = new VisLabel("Shai Hulud");

        add(imageButton).center().row();
        add(shaiHuludLabel).center();
    }

    public void setChecked(boolean checked) {
        imageButton.setChecked(checked);
    }

    public void setDisabled(boolean disabled) {
        imageButton.setDisabled(disabled);
    }

    public float getCenterX() {
        return getX() + imageButton.getX() + imageButton.getWidth() / 2f;
    }

    public float getCenterY() {
        return getY() + imageButton.getY() + imageButton.getHeight() / 2f;
    }

    public float getButtonRadius() {
        return Math.min(imageButton.getWidth(), imageButton.getHeight());
    }
}