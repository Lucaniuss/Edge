package me.lucanius.edge.thread;

import me.lucanius.edge.adapter.TabAdapter;
import me.lucanius.edge.service.TabService;
import me.lucanius.edge.tools.Voluntary;
import org.bukkit.Bukkit;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
public class TabThread extends Thread {

    private final long sleepTime;
    private final TabService service;
    private final TabAdapter adapter;

    public TabThread(TabService service, TabAdapter adapter) {
        this.sleepTime = 20L * 50L;
        this.service = service;
        this.adapter = adapter;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Bukkit.getOnlinePlayers().forEach(player ->
                        Voluntary.ofNull(service.get(player.getUniqueId())).ifPresent(tab ->
                                adapter.getEntries(player).forEach(tab::set)
                        )
                );
                sleep(sleepTime);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
