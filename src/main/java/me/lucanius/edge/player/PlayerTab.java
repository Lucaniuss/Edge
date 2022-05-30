package me.lucanius.edge.player;

import lombok.Getter;
import me.lucanius.edge.Edge;
import me.lucanius.edge.adapter.TabAdapter;
import me.lucanius.edge.context.TabColumn;
import me.lucanius.edge.entry.TabEntry;
import me.lucanius.edge.skin.Skin;
import me.lucanius.edge.tab.Tab;
import me.lucanius.edge.tab.impl.Tab_1_7;
import me.lucanius.edge.tools.LegacyTools;
import me.lucanius.edge.tools.Tools;
import me.lucanius.edge.tools.Voluntary;
import me.lucanius.edge.version.ClientVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Lucanius
 * @since May 30, 2022.
 * Edge - All Rights Reserved.
 */
@Getter
public class PlayerTab {

    private final static Edge instance = Edge.getInstance();

    private final Player player;
    private final ClientVersion version;
    private final Set<TabEntry> entries;

    private final Scoreboard scoreboard;

    private boolean headerFooter = false;

    public PlayerTab(Player player) {
        this.player = player;
        this.version = Tab.getInstance().getClientVersion(player);
        this.entries = new HashSet<>();

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this.scoreboard = player.getScoreboard() != manager.getMainScoreboard()
                ? player.getScoreboard()
                : manager.getNewScoreboard();

        player.setScoreboard(scoreboard);

        setup();
        try {
            teams();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void setup() {
        int slots = version == ClientVersion.v1_7 ? 61 : 81;
        for (int i = 0; i < slots; i++) {
            TabColumn column = TabColumn.get(version, i);
            if (column == null) {
                continue;
            }

            TabEntry entry = Tab.getInstance().create(this, "0" + (i > 9 ? i : "0" + i) + " - Tab", column, column.getSlot(version, i), i);
            if (version == ClientVersion.v1_7 || Tab.getInstance() instanceof Tab_1_7) {
                String name = LegacyTools.getNames().get(i);
                Team team = player.getScoreboard().getTeam(name);
                if (team != null) {
                    team.unregister();
                }

                team = player.getScoreboard().registerNewTeam(name);
                team.setPrefix("");
                team.setSuffix("");
                team.addEntry(LegacyTools.getEntries().get(i));
            }

            entries.add(entry);
        }
    }

    private void teams() {
        Team team = player.getScoreboard().getTeam("\\u000181");
        if (team == null) {
            team = player.getScoreboard().registerNewTeam("\\u000181");
        }
        team.addEntry(player.getName());

        for (Player online : instance.getOnline()) {
            Team onlineTeam = online.getScoreboard().getTeam("\\u000181");
            if (onlineTeam == null) {
                onlineTeam = online.getScoreboard().registerNewTeam("\\u000181");
            }

            onlineTeam.addEntry(player.getName());
            onlineTeam.addEntry(online.getName());

            team.addEntry(online.getName());
            team.addEntry(player.getName());
        }
    }

    public void update() {
        TabAdapter adapter = instance.getAdapter();
        Tab tab = Tab.getInstance();

        boolean v1_7 = version == ClientVersion.v1_7;
        if (!headerFooter && !v1_7) {
            tab.updateHeaderFooter(player, adapter.getHeader(player), adapter.getFooter(player));
            headerFooter = true;
            Tools.log("Updated header and footer for " + player.getName());
        }

        Set<TabEntry> oldEntries = new HashSet<>(entries);
        adapter.getEntries(player).forEach(data ->
                Voluntary.ofNull(entries.stream().filter(entry -> entry.getColumn() == data.getColumn() && entry.getSlot() == data.getSlot())
                        .findFirst().orElse(null)).ifPresent(entry -> {
                    oldEntries.remove(entry);
                    tab.updateFake(this, entry, data.getText());
                    tab.updateLatency(this, entry, data.getLatency());
                    if (!v1_7) {
                        tab.updateSkin(this, entry, data.getSkin());
                    }
                }));

        oldEntries.forEach(entry -> {
            tab.updateFake(this, entry, "");
            tab.updateLatency(this, entry, 0);
            if (!v1_7) {
                tab.updateSkin(this, entry, Skin.getEmpty());
            }
        });

        oldEntries.clear();

        Tools.log("Tab updated for " + player.getName());
    }
}
