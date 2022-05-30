package me.lucanius.edge.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.lucanius.edge.column.TabColumn;
import me.lucanius.edge.skin.Skin;

/**
 * @author Lucanius
 * @since May 30, 2022.
 * Edge - All Rights Reserved.
 */
@Data @AllArgsConstructor
public class TabData {

    private TabColumn column;
    private int slot;
    private String text;
    private int latency;
    private Skin skin;

    public TabData(TabColumn column, int slot) {
        this(column, slot, "");
    }

    public TabData(TabColumn column, int slot, String text) {
        this(column, slot, text, 1);
    }

    public TabData(TabColumn column, int slot, String text, int latency) {
        this(column, slot, text, latency, Skin.getEmpty());
    }

    public TabData(TabColumn column, int slot, String text, Skin skin) {
        this(column, slot, text, 1, skin);
    }
}
