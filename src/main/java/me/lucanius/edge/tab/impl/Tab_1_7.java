package me.lucanius.edge.tab.impl;

import me.lucanius.edge.context.TabColumn;
import me.lucanius.edge.entry.TabEntry;
import me.lucanius.edge.player.PlayerTab;
import me.lucanius.edge.skin.Skin;
import me.lucanius.edge.tab.Tab;
import me.lucanius.edge.tools.CC;
import me.lucanius.edge.tools.LegacyTools;
import me.lucanius.edge.player.version.ClientVersion;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.spigotmc.ProtocolInjector;

import java.util.List;

/**
 * @author Lucanius
 * @since May 30, 2022.
 * Edge - All Rights Reserved.
 */
public class Tab_1_7 extends Tab {

    private final MinecraftServer server;
    private final WorldServer world;
    private final PlayerInteractManager manager;

    public Tab_1_7() {
        this.server = MinecraftServer.getServer();
        this.world = server.getWorldServer(0);
        this.manager = new PlayerInteractManager(world);
    }

    @Override
    public TabEntry create(PlayerTab tab, String string, TabColumn column, int slot, int rawSlot) {
        OfflinePlayer fake = getFake(string);

        GameProfile profile = new GameProfile(fake.getUniqueId(), LegacyTools.getEntries().get(rawSlot));
        EntityPlayer entity = new EntityPlayer(server, world, profile, manager);
        if (tab.getVersion() != ClientVersion.v1_7) {
            profile.getProperties().put("textures", Skin.getEmpty().toProperty());
        }

        entity.ping = 1;

        ((CraftPlayer) tab.getPlayer()).getHandle().playerConnection.sendPacket(PacketPlayOutPlayerInfo.addPlayer(entity));

        return new TabEntry(string, fake, "", tab, Skin.getEmpty(), column, slot, rawSlot, 0);
    }

    @Override
    public void updateFake(PlayerTab tab, TabEntry entry, String string) {
        if (entry.getText().equals(string)) {
            return;
        }

        Player player = tab.getPlayer();
        String[] strings = CC.splitStrings(string, entry.getRawSlot());
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

        entry.setText(string);
    }

    @Override
    public void updateLatency(PlayerTab tab, TabEntry entry, int latency) {
        if (entry.getLatency() == latency) {
            return;
        }

        GameProfile profile = new GameProfile(entry.getFakePlayer().getUniqueId(), LegacyTools.getEntries().get(entry.getRawSlot()));
        EntityPlayer entity = new EntityPlayer(server, world, profile, manager);
        entity.ping = latency;

        ((CraftPlayer) tab.getPlayer()).getHandle().playerConnection.sendPacket(PacketPlayOutPlayerInfo.updatePing(entity));

        entry.setLatency(latency);
    }

    @Override
    public void updateSkin(PlayerTab tab, TabEntry entry, Skin skin) {
        if (skin == null || entry.getSkin() == skin || tab.getVersion() == ClientVersion.v1_7) {
            return;
        }

        GameProfile profile = new GameProfile(entry.getFakePlayer().getUniqueId(), LegacyTools.getEntries().get(entry.getRawSlot()));
        EntityPlayer entity = new EntityPlayer(server, world, profile, manager);

        profile.getProperties().put("textures", skin.toProperty());

        PlayerConnection connection = ((CraftPlayer) tab.getPlayer()).getHandle().playerConnection;
        connection.sendPacket(PacketPlayOutPlayerInfo.removePlayer(entity));
        connection.sendPacket(PacketPlayOutPlayerInfo.addPlayer(entity));

        entry.setSkin(skin);
    }

    @Override
    public void updateHeaderFooter(Player player, List<String> header, List<String> footer) {
        IChatBaseComponent head = ChatSerializer.a("{text:\"" + CC.translate(fromList(header)) + "\"}");
        IChatBaseComponent foot = ChatSerializer.a("{text:\"" + CC.translate(fromList(footer)) + "\"}");

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new ProtocolInjector.PacketTabHeader(head, foot));
    }

    @Override
    public ClientVersion getClientVersion(Player player) {
        return ClientVersion.get(((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion());
    }
}
