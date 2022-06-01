package me.lucanius.edge;

import lombok.Getter;
import me.lucanius.edge.adapter.TabAdapter;
import me.lucanius.edge.service.TabService;
import me.lucanius.edge.service.impl.StandardTabService;
import me.lucanius.edge.skin.Skin;
import me.lucanius.edge.tab.Tab;
import me.lucanius.edge.tab.impl.Tab_1_7;
import me.lucanius.edge.tab.impl.Tab_1_8;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 30, 2022.
 * Edge - All Rights Reserved.
 */
@Getter
public class Edge implements Listener {

    @Getter private static Edge instance;

    private final Plugin plugin;
    private final TabAdapter adapter;
    private final Tab tab;
    private final TabService service;

    public Edge(Plugin plugin, TabAdapter adapter) {
        instance = this;

        this.plugin = plugin;
        this.adapter = adapter;
        this.tab = plugin.getServer().getClass().getPackage().getName().split("\\.")[3].contains("v1_7") ? new Tab_1_7() : new Tab_1_8();
        this.service = new StandardTabService();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public Collection<? extends Player> getOnline() {
        return plugin.getServer().getOnlinePlayers();
    }

    public Skin getSkin(UUID uniqueId) {
        return Skin.get(uniqueId);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        service.join(event);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        service.quit(event);
    }
}
