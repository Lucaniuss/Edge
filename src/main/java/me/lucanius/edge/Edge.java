package me.lucanius.edge;

import lombok.Getter;
import me.lucanius.edge.adapter.TabAdapter;
import me.lucanius.edge.nms.impl.NmsTools_1_7;
import me.lucanius.edge.nms.impl.NmsTools_1_8;
import me.lucanius.edge.service.TabService;
import org.bukkit.plugin.Plugin;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
@Getter
public final class Edge {

    @Getter private static Edge instance;

    private final Plugin plugin;
    private final TabAdapter adapter;
    private final TabService service;

    public Edge(Plugin plugin, TabAdapter adapter) {
        instance = this;

        this.plugin = plugin;
        this.adapter = adapter;

        if (plugin.getServer().getClass().getPackage().getName().split("\\.")[3].contains("v1_7")) {
            new NmsTools_1_7();
        } else {
            new NmsTools_1_8();
        }

        this.service = new TabService();
    }
}
