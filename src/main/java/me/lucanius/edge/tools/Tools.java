package me.lucanius.edge.tools;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
@UtilityClass
public class Tools {

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&9[Edge] &e" + message));
    }
}
