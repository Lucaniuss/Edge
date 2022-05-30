package me.lucanius.edge.tools;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucanius
 * @since May 30, 2022.
 * Edge - All Rights Reserved.
 */
@UtilityClass
public class LegacyTools {

    @Getter private final List<String> entries;
    @Getter private final List<String> names;

    static {
        entries = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            String entry = ChatColor.values()[i].toString();
            entries.add(ChatColor.RED + entry);
            entries.add(ChatColor.GREEN + entry);
            entries.add(ChatColor.DARK_RED + entry);
            entries.add(ChatColor.DARK_GREEN + entry);
            entries.add(ChatColor.BLUE + entry);
            entries.add(ChatColor.DARK_BLUE + entry);
        }

        names = new ArrayList<>();
        for (int i = 0; i < 80; ++i) {
            names.add(((i < 10) ? "\\u00010" : "\\u0001") + i);
        }
    }
}
