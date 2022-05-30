package me.lucanius.edge.tab;

import lombok.Getter;
import me.lucanius.edge.column.TabColumn;
import me.lucanius.edge.entry.TabEntry;
import me.lucanius.edge.player.PlayerTab;
import me.lucanius.edge.player.version.ClientVersion;
import me.lucanius.edge.skin.Skin;
import me.lucanius.edge.tools.CC;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lucanius
 * @since May 30, 2022.
 * Edge - All Rights Reserved.
 */
public abstract class Tab {

    @Getter private static Tab instance;

    protected Tab() {
        instance = this;
        CC.log("Tab: " + getClass().getSimpleName() + " loaded.");
    }

    public abstract TabEntry create(PlayerTab tab, String string, TabColumn column, int slot, int rawSlot);

    public abstract void updateFake(PlayerTab tab, TabEntry entry, String string);

    public abstract void updateLatency(PlayerTab tab, TabEntry entry, int latency);

    public abstract void updateSkin(PlayerTab tab, TabEntry entry, Skin skin);

    public abstract void updateHeaderFooter(Player player, List<String> header, List<String> footer);

    public abstract ClientVersion getClientVersion(Player player);

    protected String fromList(List<String> lines) {
        StringBuilder builder = new StringBuilder();

        for (String line : lines) {
            builder.append(line).append("\n");
        }

        return builder.toString();
    }

    protected OfflinePlayer getFake(String string) {
        return new OfflinePlayer() {

            private final UUID uuid = UUID.randomUUID();

            @Override
            public boolean isOnline() {
                return true;
            }

            @Override
            public String getName() {
                return string;
            }

            @Override
            public UUID getUniqueId() {
                return uuid;
            }

            @Override
            public boolean isBanned() {
                return false;
            }

            @Override
            public void setBanned(boolean b) {}

            @Override
            public boolean isWhitelisted() {
                return false;
            }

            @Override
            public void setWhitelisted(boolean b) {}

            @Override
            public Player getPlayer() {
                return null;
            }

            @Override
            public long getFirstPlayed() {
                return 0;
            }

            @Override
            public long getLastPlayed() {
                return 0;
            }

            @Override
            public boolean hasPlayedBefore() {
                return false;
            }

            @Override
            public Location getBedSpawnLocation() {
                return null;
            }

            @Override
            public Map<String, Object> serialize() {
                return null;
            }

            @Override
            public boolean isOp() {
                return false;
            }

            @Override
            public void setOp(boolean b) {}
        };
    }
}
