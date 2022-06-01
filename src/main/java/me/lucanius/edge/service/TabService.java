package me.lucanius.edge.service;

import me.lucanius.edge.player.PlayerTab;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

/**
 * @author Lucanius
 * @since June 01, 2022.
 * Edge - All Rights Reserved.
 */
public interface TabService {

    void destroy();

    void join(PlayerJoinEvent event);

    void quit(PlayerQuitEvent event);

    Map<UUID, PlayerTab> getRegistered();

}
