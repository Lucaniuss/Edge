package me.lucanius.edge.adapter;

import me.lucanius.edge.entry.TabData;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * @author Lucanius
 * @since May 30, 2022.
 * Edge - All Rights Reserved.
 */
public interface TabAdapter {

    List<String> getHeader(Player player);

    List<String> getFooter(Player player);

    Set<TabData> getEntries(Player player);

}
