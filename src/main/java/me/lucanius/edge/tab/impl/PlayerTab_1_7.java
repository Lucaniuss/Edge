package me.lucanius.edge.tab.impl;

import com.mojang.authlib.GameProfile;
import me.lucanius.edge.entry.TabEntry;
import me.lucanius.edge.nms.NmsTools;
import me.lucanius.edge.reflection.Reflection_1_7;
import me.lucanius.edge.skin.Skin;
import me.lucanius.edge.tab.PlayerTab;
import me.lucanius.edge.tools.CC;
import me.lucanius.edge.tools.Voluntary;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
public class PlayerTab_1_7 implements PlayerTab {

    private final Player player;

    private final int version;
    private final Scoreboard scoreboard;

    private final List<GameProfile> profiles;
    private final String[] teams;
    private final String[] lines;

    public PlayerTab_1_7(Player player) {
        this.player = player;

        NmsTools nmsTools = NmsTools.getInstance();
        this.version = nmsTools.getClientVersion(player);
        this.scoreboard = nmsTools.getScoreboard(player);

        this.profiles = new ArrayList<>();
        this.teams = new String[80];
        this.lines = new String[80];

        removePlayers();
        if (version >= 47) {
            for (int i = 0; i < 80; i++) {
                setupTab(i);
            }
        } else {
            for (int i = 0; i < 20; i++) {
                for (int x = 0; x < 3; x++) {
                    setupTab((x * 20) + i);
                }
            }
        }
    }

    @Override
    public void set(TabEntry entry) {
        String line = entry.getLine();
        int slot = entry.getSlot();
        if (lines[slot] != null && lines[slot].equals(line)) {
            return;
        }
        if (slot >= 60 && !(version >= 47)) {
            return;
        }

        Team team = getTeam(teams[slot]);

        String prefix;
        String suffix;
        if (line.length() > 16) {
            int split = line.charAt(15) == CC.COLOR_CHAR ? 15 : 16;
            prefix = line.substring(0, split);
            suffix = ChatColor.getLastColors(prefix) + line.substring(split);
        } else {
            prefix = line;
            suffix = "";
        }

        updateTeam(team.getName(), prefix, suffix.length() > 16 ? suffix.substring(0, 16) : suffix);
        lines[slot] = line;
    }

    @Override
    public void unSet() {
        if (version >= 47) {
            profiles.forEach(this::destroyPlayerInfo);
        }
    }

    @Override
    public void buildPlayerInfo(GameProfile profile) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(Reflection_1_7.build(profile));
    }

    @Override
    public void destroyPlayerInfo(GameProfile profile) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(Reflection_1_7.destroy(profile));
    }

    @Override
    public void updateTeam(String name, String prefix, String suffix) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(Reflection_1_7.update(name, prefix, suffix));
    }

    @Override
    public void removePlayers() {
        CraftPlayer nmsPlayer = (CraftPlayer) player;
        Bukkit.getOnlinePlayers().forEach(online ->
                nmsPlayer.getHandle().playerConnection.sendPacket(PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer) online).getHandle()))
        );
    }

    @Override
    public void setupTab(int i) {
        String team = getTeamName(i);
        teams[i] = team;

        GameProfile profile = new GameProfile(UUID.randomUUID(), team);
        if (version >= 47) {
            profiles.add(profile);

            profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", Skin.EMPTY.toProperty());
        }

        buildPlayerInfo(profile);
        getTeam(team).addEntry(team);
    }

    @Override
    public Team getTeam(String name) {
        synchronized (scoreboard) {
            return Voluntary.ofNull(scoreboard.getTeam(name)).orElse(scoreboard.registerNewTeam(name));
        }
    }
}
