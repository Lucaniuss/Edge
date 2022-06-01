package me.lucanius.edge.service.impl;

import me.lucanius.edge.player.PlayerTab;
import me.lucanius.edge.service.TabService;
import me.lucanius.edge.thread.TabThread;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lucanius
 * @since June 01, 2022.
 * Edge - All Rights Reserved.
 */
public class StandardTabService implements TabService {

    private final Map<UUID, PlayerTab> registered;
    private final TabThread thread;

    public StandardTabService() {
        this.registered = new ConcurrentHashMap<>();
        this.thread = new TabThread();
    }

    @Override
    public void destroy() {
        if (thread.isAlive()) {
            thread.stop();
        }

        registered.clear();
    }

    @Override
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        registered.put(player.getUniqueId(), new PlayerTab(player));
    }

    @Override
    public void quit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Optional.ofNullable(player.getScoreboard().getTeam("\\u000181")).ifPresent(Team::unregister);
        registered.remove(player.getUniqueId());
    }

    @Override
    public Map<UUID, PlayerTab> getRegistered() {
        return registered;
    }
}
