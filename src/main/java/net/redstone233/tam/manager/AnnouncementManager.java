package net.redstone233.tam.manager;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.redstone233.tam.config.ConfigManager;
import net.redstone233.tam.network.NetworkHandler;

public class AnnouncementManager {

    // 玩家加入世界时检查是否需要显示公告
    public static void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();

        if (!ConfigManager.shouldShowOnWorldEnter()) {
            return;
        }

        // 检查配置是否发生变化
        if (ConfigManager.hasConfigChanged()) {
            NetworkHandler.sendAnnouncementConfig(player);

            // 更新最后一次显示的哈希值
            updateLastDisplayedHash();
        }
    }

    private static void updateLastDisplayedHash() {
        // 这里需要实现更新配置文件中哈希值的逻辑
        String currentHash = ConfigManager.getConfigHash();
        // 在实际实现中，这里应该保存 currentHash 到配置文件
        // 由于配置系统的限制，可能需要直接操作配置文件
    }

    // 强制显示公告（用于调试命令）
    public static void forceShowToPlayer(ServerPlayerEntity player) {
        NetworkHandler.forceShowAnnouncement(player);
    }
}