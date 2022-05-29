package me.lucanius.edge.service;

import lombok.Getter;
import me.lucanius.edge.Edge;
import me.lucanius.edge.adapter.TabAdapter;
import me.lucanius.edge.nms.NmsTools;
import me.lucanius.edge.tab.PlayerTab;
import me.lucanius.edge.tab.impl.PlayerTab_1_8;
import me.lucanius.edge.thread.TabThread;
import me.lucanius.edge.tools.Executor;
import me.lucanius.edge.tools.Voluntary;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
@Getter
public class TabService implements Listener {

    private final Map<UUID, PlayerTab> tabs;
    private final TabAdapter adapter;
    private final TabThread thread;

    public TabService() {
        tabs = new ConcurrentHashMap<>();
        adapter = Edge.getInstance().getAdapter();
        (thread = new TabThread(this, adapter)).start();

        Bukkit.getPluginManager().registerEvents(this, Edge.getInstance().getPlugin());
    }

    public void build(Player player) {
        PlayerTab tab = NmsTools.getInstance().getPlayerTab(player);
        tabs.put(player.getUniqueId(), tab);
        if (tab instanceof PlayerTab_1_8) {
            ((PlayerTab_1_8) tab).outer(adapter.getHeader(player), adapter.getFooter(player));
        }
    }

    public void destroy(Player player) {
        Voluntary.ofNull(tabs.remove(player.getUniqueId())).ifPresent(PlayerTab::unSet);
    }

    public PlayerTab get(UUID uniqueId) {
        return tabs.get(uniqueId);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Executor.syncLater(() -> build(event.getPlayer()), 5L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Executor.async(() -> destroy(event.getPlayer()));
    }
}
