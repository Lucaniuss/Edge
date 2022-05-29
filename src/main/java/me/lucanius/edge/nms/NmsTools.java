package me.lucanius.edge.nms;

import lombok.Getter;
import me.lucanius.edge.tab.PlayerTab;
import me.lucanius.edge.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
public abstract class NmsTools {

    @Getter private static NmsTools instance;

    protected NmsTools() {
        instance = this;
        Tools.log("NMS: " + getClass().getSimpleName() + " loaded.");
    }

    public abstract int getClientVersion(Player player);

    public abstract PlayerTab getPlayerTab(Player player);

    public abstract Scoreboard newScoreboard();

    public Scoreboard getScoreboard(Player player) {
        return player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard() ? newScoreboard() : player.getScoreboard();
    }
}
