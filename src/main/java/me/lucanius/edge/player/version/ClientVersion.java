package me.lucanius.edge.player.version;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Lucanius
 * @since May 30, 2022.
 * Edge - All Rights Reserved.
 */
@Getter @AllArgsConstructor
public enum ClientVersion {

    v1_7(Arrays.asList(4, 5)),
    v1_8(Collections.singletonList(47));

    private final List<Integer> rawVersions;

    public static ClientVersion get(int input) {
        return Arrays.stream(values()).filter(v -> v.getRawVersions().contains(input)).findFirst().orElse(v1_8);
    }
}
