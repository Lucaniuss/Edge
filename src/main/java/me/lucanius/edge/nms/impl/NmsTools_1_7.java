package me.lucanius.edge.nms.impl;

import me.lucanius.edge.nms.NmsTools;
import me.lucanius.edge.tab.PlayerTab;
import me.lucanius.edge.tab.impl.PlayerTab_1_7;
import net.minecraft.server.v1_7_R4.ScoreboardServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Constructor;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
public class NmsTools_1_7 extends NmsTools {

    @Override
    public int getClientVersion(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion();
    }

    @Override
    public PlayerTab getPlayerTab(Player player) {
        return new PlayerTab_1_7(player);
    }

    @Override
    public Scoreboard newScoreboard() {
        Scoreboard scoreboard;

        try {
            Constructor<?> constructor = CraftScoreboard.class.getDeclaredConstructor(net.minecraft.server.v1_7_R4.Scoreboard.class);
            constructor.setAccessible(true);

            return (Scoreboard) constructor.newInstance(new ScoreboardServer(((CraftServer) Bukkit.getServer()).getServer()));
        } catch (Exception e) {
            scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        }

        return scoreboard;
    }
}
