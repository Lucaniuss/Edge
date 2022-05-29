package me.lucanius.edge.tab.impl;

import com.mojang.authlib.GameProfile;
import lombok.SneakyThrows;
import me.lucanius.edge.entry.TabEntry;
import me.lucanius.edge.nms.NmsTools;
import me.lucanius.edge.reflection.Reflection_1_8;
import me.lucanius.edge.skin.Skin;
import me.lucanius.edge.tab.PlayerTab;
import me.lucanius.edge.tools.CC;
import me.lucanius.edge.tools.Executor;
import me.lucanius.edge.tools.ReflectionTools;
import me.lucanius.edge.tools.Voluntary;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
public class PlayerTab_1_8 implements PlayerTab {

    private final Player player;

    private final int version;
    private final Scoreboard scoreboard;

    private final GameProfile[] profiles;
    private final String[] teams;
    private final String[] lines;

    public PlayerTab_1_8(Player player) {
        this.player = player;

        NmsTools nmsTools = NmsTools.getInstance();
        this.version = nmsTools.getClientVersion(player);
        this.scoreboard = nmsTools.getScoreboard(player);

        this.profiles = new GameProfile[80];
        this.teams = new String[60];
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

        if (version >= 47) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(Reflection_1_8.updateTab(profiles[slot], line));
        } else {
            if (slot >= 60) {
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
        }

        lines[slot] = line;
    }

    @Override
    public void unSet() {
        if (version >= 47) {
            for (GameProfile profile : profiles) {
                destroyPlayerInfo(profile);
            }
        }
    }

    @Override
    public void buildPlayerInfo(GameProfile profile) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(Reflection_1_8.build(profile));
    }

    @Override
    public void destroyPlayerInfo(GameProfile profile) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(Reflection_1_8.destroy(profile));
    }

    @Override
    public void updateTeam(String name, String prefix, String suffix) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(Reflection_1_8.update(name, prefix, suffix));
    }

    @Override
    public void removePlayers() {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        List<PacketPlayOutPlayerInfo> packets = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(online -> {
            EntityPlayer nmsOnline = ((CraftPlayer) online).getHandle();

            entityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, nmsOnline));
            packets.add(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, nmsOnline));
        });

        Executor.asyncLater(() -> packets.forEach(entityPlayer.playerConnection::sendPacket), 5L);
    }

    @Override
    public void setupTab(int i) {
        String teamName = getTeamName(i);
        GameProfile profile = new GameProfile(UUID.randomUUID(), teamName);
        if (version >= 47) {
            profiles[i] = profile;

            profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", Skin.EMPTY.toProperty());

            buildPlayerInfo(profile);
        } else {
            teams[i] = teamName;

            buildPlayerInfo(profile);
            getTeam(teamName).addEntry(teamName);
        }
    }

    @Override
    public Team getTeam(String name) {
        synchronized (scoreboard) {
            return Voluntary.ofNull(scoreboard.getTeam(name)).orElse(scoreboard.registerNewTeam(name));
        }
    }

    @SneakyThrows
    public void outer(String head, String foot) {
        IChatBaseComponent header = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + head + "\"}");
        IChatBaseComponent footer = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + foot + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        Field top = ReflectionTools.setAccessibleAndGet(packet.getClass(), "a");
        top.set(packet, header);

        Field bottom = ReflectionTools.setAccessibleAndGet(packet.getClass(), "b");
        bottom.set(packet, footer);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
