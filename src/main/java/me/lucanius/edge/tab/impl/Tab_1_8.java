package me.lucanius.edge.tab.impl;

import com.comphenix.protocol.ProtocolLibrary;
import com.mojang.authlib.GameProfile;
import me.lucanius.edge.context.TabColumn;
import me.lucanius.edge.entry.TabEntry;
import me.lucanius.edge.player.PlayerTab;
import me.lucanius.edge.skin.Skin;
import me.lucanius.edge.tab.Tab;
import me.lucanius.edge.tools.CC;
import me.lucanius.edge.tools.LegacyTools;
import me.lucanius.edge.tools.Reflections;
import me.lucanius.edge.version.ClientVersion;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.Team;
import protocolsupport.api.ProtocolSupportAPI;
import us.myles.ViaVersion.api.Via;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author Lucanius
 * @since May 30, 2022.
 * Edge - All Rights Reserved.
 */
public class Tab_1_8 extends Tab {

    private final Function<Player, Integer> versionGetter;

    public Tab_1_8() {
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
    public TabEntry create(PlayerTab tab, String string, TabColumn column, int slot, int rawSlot) {
        OfflinePlayer fake = getFake(string);

        ClientVersion version = tab.getVersion();
        GameProfile profile = new GameProfile(fake.getUniqueId(), version != ClientVersion.v1_7 ? string : LegacyTools.getEntries().get(rawSlot));
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        PacketPlayOutPlayerInfo.PlayerInfoData info = packet.new PlayerInfoData(profile, 1, WorldSettings.EnumGamemode.NOT_SET, new ChatComponentText(version != ClientVersion.v1_7 ? "" : profile.getName()));

        if (version != ClientVersion.v1_7) {
            info.a().getProperties().put("textures", Skin.getEmpty().toAuthProperty());
        }

        Reflections.setAndGet(packet, "b", Collections.singletonList(info));

        ((CraftPlayer) tab.getPlayer()).getHandle().playerConnection.sendPacket(packet);

        return new TabEntry(string, fake, "", tab, Skin.getEmpty(), column, slot, rawSlot, 0);
    }

    @Override
    public void updateFake(PlayerTab tab, TabEntry entry, String string) {
        if (entry.getText().equals(string)) {
            return;
        }

        Player player = tab.getPlayer();
        String[] strings = CC.splitStrings(string, entry.getRawSlot());
        ClientVersion version = tab.getVersion();
        if (version == ClientVersion.v1_7) {
            String teamName = LegacyTools.getNames().get(entry.getRawSlot());

            Team team = player.getScoreboard().getTeam(teamName);
            if (team == null) {
                team = player.getScoreboard().registerNewTeam(teamName);
            }

            team.setPrefix(strings[0]);
            if (strings.length > 1) {
                team.setSuffix(strings[1]);
            } else {
                team.setSuffix("");
            }
        } else {
            GameProfile profile = new GameProfile(entry.getFakePlayer().getUniqueId(), entry.getIdentifier());
            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
            PacketPlayOutPlayerInfo.PlayerInfoData info = packet.new PlayerInfoData(profile, 1, WorldSettings.EnumGamemode.NOT_SET, new ChatComponentText(strings.length > 1 ? strings[0] + strings[1] : strings[0]));

            Reflections.setAndGet(packet, "b", Collections.singletonList(info));

            ((CraftPlayer) tab.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        }

        entry.setText(string);
    }

    @Override
    public void updateLatency(PlayerTab tab, TabEntry entry, int latency) {
        if (entry.getLatency() == latency) {
            return;
        }

        GameProfile profile = new GameProfile(entry.getFakePlayer().getUniqueId(), entry.getIdentifier());
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY);
        PacketPlayOutPlayerInfo.PlayerInfoData info = packet.new PlayerInfoData(profile, 1, WorldSettings.EnumGamemode.NOT_SET, new ChatComponentText(entry.getText()));

        Reflections.setAndGet(packet, "b", Collections.singletonList(info));

        ((CraftPlayer) tab.getPlayer()).getHandle().playerConnection.sendPacket(packet);

        entry.setLatency(latency);
    }

    @Override
    public void updateSkin(PlayerTab tab, TabEntry entry, Skin skin) {
        if (skin == null || entry.getSkin() == skin || tab.getVersion() == ClientVersion.v1_7) {
            return;
        }

        GameProfile profile = new GameProfile(entry.getFakePlayer().getUniqueId(), entry.getIdentifier());
        PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        PacketPlayOutPlayerInfo add = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        PacketPlayOutPlayerInfo.PlayerInfoData info = remove.new PlayerInfoData(profile, 1, WorldSettings.EnumGamemode.NOT_SET, new ChatComponentText(entry.getText()));

        info.a().getProperties().put("textures", skin.toAuthProperty());

        Reflections.setAndGet(remove, "b", Collections.singletonList(info));
        Reflections.setAndGet(add, "b", Collections.singletonList(info));

        PlayerConnection connection = ((CraftPlayer) tab.getPlayer()).getHandle().playerConnection;
        connection.sendPacket(remove);
        connection.sendPacket(add);

        entry.setSkin(skin);
    }

    @Override
    public void updateHeaderFooter(Player player, List<String> header, List<String> footer) {
        IChatBaseComponent head = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + CC.translate(fromList(header)) + "\"}");
        IChatBaseComponent foot = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + CC.translate(fromList(footer)) + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        Reflections.setAndGet(packet, "a", head);
        Reflections.setAndGet(packet, "b", foot);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public ClientVersion getClientVersion(Player player) {
        return ClientVersion.get(versionGetter.apply(player));
    }
}
