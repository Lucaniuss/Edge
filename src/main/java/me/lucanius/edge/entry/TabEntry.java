package me.lucanius.edge.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.lucanius.edge.column.TabColumn;
import me.lucanius.edge.player.PlayerTab;
import me.lucanius.edge.skin.Skin;
import org.bukkit.OfflinePlayer;

/**
 * @author Lucanius
 * @since May 30, 2022.
 * Edge - All Rights Reserved.
 */
@Data @AllArgsConstructor
public class TabEntry {

    private String identifier;
    private OfflinePlayer fakePlayer;
    private String text;
    private PlayerTab tab;
    private Skin skin;
    private TabColumn column;
    private int slot;
    private int rawSlot;
    private int latency;

}
