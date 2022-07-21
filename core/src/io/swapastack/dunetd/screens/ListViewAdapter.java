package io.swapastack.dunetd.screens;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import io.swapastack.dunetd.savegame.SaveGame;
import lombok.NonNull;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayListAdapter<SaveGame, VisTable> {

    private final Drawable background;
    private final Drawable selection;

    public ListViewAdapter(@NonNull ArrayList<SaveGame> array, @NonNull Drawable background,
                           @NonNull Drawable selection) {
        super(array);

        this.background = background;
        this.selection = selection;

        setSelectionMode(SelectionMode.SINGLE);
    }

    @Override
    protected VisTable createView(SaveGame item) {
        var label = new VisLabel(item.getName());

        var table = new VisTable();
        table.setBackground(background);

        table.left();
        table.add(label);
        return table;
    }

    @Override
    protected void selectView(VisTable view) {
        view.setBackground(selection);
    }

    @Override
    protected void deselectView(VisTable view) {
        view.setBackground(background);
    }
}