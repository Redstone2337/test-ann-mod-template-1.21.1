// AnnouncementManager.java (更新后)
package net.redstone233.tam.manager;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class AnnouncementManager {

    // 玩家加入世界时检查是否需要显示公告
    public static void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        // 委托给新的管理器处理
        PlayerAnnouncementManager.onPlayerJoin(handler, sender, server);
    }

    // 强制显示公告（用于调试命令）
    public static void forceShowToPlayer(ServerPlayerEntity player) {
        PlayerAnnouncementManager.forceShowToPlayer(player);
    }

    // 新增：重置玩家公告记录
    public static void resetPlayerAnnouncement(ServerPlayerEntity player) {
        PlayerAnnouncementManager.resetPlayerAnnouncement(player);
    }

    // 新增：获取调试信息
    public static String getDebugInfo(ServerPlayerEntity player) {
        return PlayerAnnouncementManager.getDebugInfo(player);
    }
}