package me.lucanius.edge.nms.impl;

import com.comphenix.protocol.ProtocolLibrary;
import me.lucanius.edge.nms.NmsTools;
import me.lucanius.edge.tab.PlayerTab;
import me.lucanius.edge.tab.impl.PlayerTab_1_8;
import net.minecraft.server.v1_8_R3.ScoreboardServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.Scoreboard;
import protocolsupport.api.ProtocolSupportAPI;
import us.myles.ViaVersion.api.Via;

import java.lang.reflect.Constructor;
import java.util.function.Function;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
public class NmsTools_1_8 extends NmsTools {

    private final Function<Player, Integer> versionGetter;

    public NmsTools_1_8() {
        PluginManager manager = Bukkit.getPluginManager();
        if (manager.getPlugin("ViaVersion") != null) {
            versionGetter = player -> Via.getAPI().getPlayerVersion(player.getUniqueId());
        } else if (manager.getPlugin("ProtocolSupport") != null) {
            versionGetter = player -> ProtocolSupportAPI.getProtocolVersion(player).getId();
        } else if (manager.getPlugin("ProtocolLib") != null) {
            versionGetter = player -> ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
        } else {
            versionGetter = player -> 47;
        }
    }

    @Override
    public int getClientVersion(Player player) {
        return versionGetter.apply(player);
    }

    @Override
    public PlayerTab getPlayerTab(Player player) {
        return new PlayerTab_1_8(player);
    }

    @Override
    public Scoreboard newScoreboard() {
        Scoreboard scoreboard;

        try {
            Constructor<?> constructor = CraftScoreboard.class.getDeclaredConstructor(net.minecraft.server.v1_8_R3.Scoreboard.class);
            constructor.setAccessible(true);

            return (Scoreboard) constructor.newInstance(new ScoreboardServer(((CraftServer) Bukkit.getServer()).getServer()));
        } catch (Exception e) {
            scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        }

        return scoreboard;
    }
}
