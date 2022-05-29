package me.lucanius.edge.entry;

import lombok.Getter;
import lombok.Setter;
import me.lucanius.edge.skin.Skin;
import me.lucanius.edge.tools.CC;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
@Getter @Setter
public class TabEntry {

    private final String line;
    private final int slot;
    private final int latency;
    private final Skin skin;

    public TabEntry(String line, int slot, int latency, Skin skin) {
        this.line = CC.translate(line);
        this.slot = slot;
        this.latency = latency;
        this.skin = skin;
    }

    public TabEntry(String line, int slot, int latency) {
        this(line, slot, latency, Skin.EMPTY);
    }

    public TabEntry(String line, int slot, Skin skin) {
        this(line, slot, -1, skin);
    }

    public TabEntry(String line, int slot) {
        this(line, slot, -1, Skin.EMPTY);
    }
}
