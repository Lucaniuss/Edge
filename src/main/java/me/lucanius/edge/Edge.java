package me.lucanius.edge;

import lombok.Getter;
import me.lucanius.edge.adapter.TabAdapter;
import me.lucanius.edge.player.PlayerTab;
import me.lucanius.edge.skin.Skin;
import me.lucanius.edge.tab.Tab;
import me.lucanius.edge.tab.impl.Tab_1_7;
import me.lucanius.edge.tab.impl.Tab_1_8;
import me.lucanius.edge.thread.TabThread;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
    private final Map<UUID, PlayerTab> registered;

    private final Tab tab;

    private final TabThread thread;

    public Edge(Plugin plugin, TabAdapter adapter) {
        instance = this;

        this.plugin = plugin;
        this.adapter = adapter;
        this.registered = new ConcurrentHashMap<>();

        this.tab = plugin.getServer().getClass().getPackage().getName().split("\\.")[3].contains("v1_7") ? new Tab_1_7() : new Tab_1_8();

        this.thread = new TabThread();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void destroy() {
        if (thread != null && thread.isAlive()) {
            thread.stop();
        }

        registered.clear();
    }

    public void register(Player player) {
        registered.put(player.getUniqueId(), new PlayerTab(player));
    }

    public void unregister(Player player) {
        Optional.ofNullable(player.getScoreboard().getTeam("\\u000181")).ifPresent(Team::unregister);
        registered.remove(player.getUniqueId());
    }

    public Collection<? extends Player> getOnline() {
        return plugin.getServer().getOnlinePlayers();
    }

    public Skin getSkin(UUID uniqueId) {
        return Skin.get(uniqueId);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        register(event.getPlayer());
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        unregister(event.getPlayer());
    }
}
