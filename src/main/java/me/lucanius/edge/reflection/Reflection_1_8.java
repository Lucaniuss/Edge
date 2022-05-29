package me.lucanius.edge.reflection;

import com.mojang.authlib.GameProfile;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import me.lucanius.edge.tools.ReflectionTools;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.WorldSettings;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Collections;

/**
 * @author Lucanius
 * @since May 29, 2022.
 * Edge - All Rights Reserved.
 */
@UtilityClass
public class Reflection_1_8 {

    private MethodHandle TAB_INFO;
    private MethodHandle TAB_ACTION;

    private MethodHandle SCOREBOARD_NAME;
    private MethodHandle SCOREBOARD_DISPLAY_NAME;
    private MethodHandle SCOREBOARD_PREFIX;
    private MethodHandle SCOREBOARD_SUFFIX;
    private MethodHandle SCOREBOARD_ACTION;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            TAB_INFO = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "b"));
            TAB_ACTION = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutPlayerInfo.class, "a"));

            SCOREBOARD_NAME = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "a"));
            SCOREBOARD_DISPLAY_NAME = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "b"));
            SCOREBOARD_PREFIX = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "c"));
            SCOREBOARD_SUFFIX = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "d"));
            SCOREBOARD_ACTION = lookup.unreflectSetter(ReflectionTools.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "h"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public PacketPlayOutPlayerInfo build(GameProfile profile) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData info = packet.new PlayerInfoData(
                profile,
                0,
                WorldSettings.EnumGamemode.NOT_SET,
                new ChatComponentText(profile.getName())
        );

        TAB_INFO.invokeExact(packet, Collections.singletonList(info));
        TAB_ACTION.invokeExact(packet, PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);

        return packet;
    }

    @SneakyThrows
    public PacketPlayOutPlayerInfo destroy(GameProfile profile) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData info = packet.new PlayerInfoData(
                profile,
                0,
                WorldSettings.EnumGamemode.NOT_SET,
                new ChatComponentText(profile.getName())
        );

        TAB_INFO.invokeExact(packet, Collections.singletonList(info));
        TAB_ACTION.invokeExact(packet, PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);

        return packet;
    }

    @SneakyThrows
    public PacketPlayOutPlayerInfo updateTab(GameProfile profile, String name) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData info = packet.new PlayerInfoData(
                profile,
                0,
                WorldSettings.EnumGamemode.NOT_SET,
                new ChatComponentText(name)
        );

        TAB_INFO.invokeExact(packet, Collections.singletonList(info));
        TAB_ACTION.invokeExact(packet, PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);

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
