package me.lucanius.edge.tab;

import com.mojang.authlib.GameProfile;
import me.lucanius.edge.entry.TabEntry;
import me.lucanius.edge.tools.CC;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
public interface PlayerTab {

    ChatColor[] colors = ChatColor.values();

    void set(TabEntry entry);

    void unSet();

    void buildPlayerInfo(GameProfile profile);

    void destroyPlayerInfo(GameProfile profile);

    void updateTeam(String name, String prefix, String suffix);

    void removePlayers();

    void setupTab(int i);

    Team getTeam(String name);

    default String getTeamName(int i) {
        return colors[i / 10].toString() + colors[i % 10].toString() + CC.RESET;
    }

}
