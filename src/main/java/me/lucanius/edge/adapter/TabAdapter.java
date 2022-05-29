package me.lucanius.edge.adapter;

import me.lucanius.edge.entry.TabEntry;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
public interface TabAdapter {

    String getHeader(Player player);

    String getFooter(Player player);

    Set<TabEntry> getEntries(Player player);

}
