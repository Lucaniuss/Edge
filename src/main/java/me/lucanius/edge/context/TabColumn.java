package me.lucanius.edge.context;

import lombok.Getter;
import me.lucanius.edge.version.ClientVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lucanius
 * @since May 30, 2022.
 * Edge - All Rights Reserved.
 */
@Getter
public enum TabColumn {

    LEFT(1, -2, 3),
    MIDDLE(21, -1, 3),
    RIGHT(41, 0, 3),
    FAR_RIGHT(61, 60, 1);

    private final int start;
    private final List<Integer> slots;

    TabColumn(int start, int raw, int increase) {
        this.start = start;
        this.slots = new ArrayList<>();

        for (int i = 1; i < 21; i++) {
            slots.add(raw + (i * increase));
        }
    }

    public int getSlot(ClientVersion version, int raw) {
        if (version != ClientVersion.v1_7) {
            return raw - start + 1;
        }

        int i = 0;
        for (int slot : slots) {
            i++;
            if (raw == slot) {
                return i;
            }
        }

        return i;
    }

    public static TabColumn get(ClientVersion version, int slot) {
        if (version == ClientVersion.v1_7) {
            return Arrays.stream(values()).filter(c -> c.getSlots().contains(slot)).findFirst().orElse(null);
        }

        return (slot >= 1 && slot <= 20) ? LEFT
                : (slot >= 21 && slot <= 40) ? MIDDLE
                : (slot >= 41 && slot <= 60) ? RIGHT
                : (slot >= 61 && slot <= 80) ? FAR_RIGHT
                : null;
    }
}
