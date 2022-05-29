package me.lucanius.edge.reflection;

import com.mojang.authlib.GameProfile;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import me.lucanius.edge.tools.ReflectionTools;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
@UtilityClass
public class Reflection_1_7 {

    private MethodHandle TAB_PLAYER;
    private MethodHandle TAB_USERNAME;
    private MethodHandle TAB_ACTION;

    private MethodHandle SCOREBOARD_NAME;
    private MethodHandle SCOREBOARD_DISPLAY_NAME;
    private MethodHandle SCOREBOARD_PREFIX;
    private MethodHandle SCOREBOARD_SUFFIX;
    private MethodHandle SCOREBOARD_ACTION;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            TAB_PLAYER = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "player"));
            TAB_USERNAME = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "username"));
            TAB_ACTION = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "action"));

            SCOREBOARD_NAME = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "a"));
            SCOREBOARD_DISPLAY_NAME = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "b"));
            SCOREBOARD_PREFIX = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "c"));
            SCOREBOARD_SUFFIX = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "d"));
            SCOREBOARD_ACTION = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "f"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public PacketPlayOutPlayerInfo build(GameProfile gameProfile) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();

        TAB_PLAYER.invokeExact(packet, gameProfile);
        TAB_USERNAME.invokeExact(packet, gameProfile.getName());
        TAB_ACTION.invokeExact(packet, 0);

        return packet;
    }

    @SneakyThrows
    public PacketPlayOutPlayerInfo destroy(GameProfile gameProfile) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();

        TAB_PLAYER.invokeExact(packet, gameProfile);
        TAB_USERNAME.invokeExact(packet, gameProfile.getName());
        TAB_ACTION.invokeExact(packet, 4);

        return packet;
    }

    @SneakyThrows
    public PacketPlayOutScoreboardTeam update(String name, String prefix, String suffix) {
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

        SCOREBOARD_NAME.invokeExact(packet, name);
        SCOREBOARD_DISPLAY_NAME.invokeExact(packet, name);
        SCOREBOARD_PREFIX.invokeExact(packet, prefix);
        SCOREBOARD_SUFFIX.invokeExact(packet, suffix);
        SCOREBOARD_ACTION.invokeExact(packet, 2);

        return packet;
    }
}
