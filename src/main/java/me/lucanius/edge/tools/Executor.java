package me.lucanius.edge.tools;

import lombok.experimental.UtilityClass;
import me.lucanius.edge.Edge;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
@UtilityClass
public final class Executor {

    private final Plugin plugin = Edge.getInstance().getPlugin();

    public BukkitTask sync(Runnable runnable) {
        return plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    public BukkitTask async(Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public BukkitTask syncLater(Runnable runnable, long delay) {
        return plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public BukkitTask asyncLater(Runnable runnable, long delay) {
        return plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    public BukkitTask syncTimer(Runnable runnable, long delay, long interval) {
        return plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, interval);
    }

    public BukkitTask asyncTimer(Runnable runnable, long delay, long interval) {
        return plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, interval);
    }
}
