package net.redstone233.tam.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.redstone233.tam.config.AnnouncementConfig;
import net.redstone233.tam.config.ConfigManager;
import net.redstone233.tam.core.screen.AnnouncementScreen;

public class NetworkHandler {

    public static void register() {
        // 在通用端注册有效负载类型
        PayloadTypeRegistry.playS2C().register(AnnouncementPayload.ID, AnnouncementPayload.CODEC);
    }

    public static void registerClientReceivers() {
        // 客户端接收公告配置
        ClientPlayNetworking.registerGlobalReceiver(AnnouncementPayload.ID,
                (payload, context) -> {
                    AnnouncementConfig config = payload.config();
                    context.client().execute(() -> {
                        // 使用修正后的 AnnouncementScreen
                        context.client().setScreen(new AnnouncementScreen(config));
                    });
                });
    }

    // 发送公告配置给玩家
    public static void sendAnnouncementConfig(ServerPlayerEntity player) {
        AnnouncementConfig config = ConfigManager.createAnnouncementConfig();
        if (config.isValid()) {
            ServerPlayNetworking.send(player, new AnnouncementPayload(config));
        }
    }

    // 发送给所有在线玩家
    public static void sendToAllPlayers(MinecraftServer server) {
        AnnouncementConfig config = ConfigManager.createAnnouncementConfig();
        if (config.isValid()) {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(player, new AnnouncementPayload(config));
            }
        }
    }

    // 强制显示公告给特定玩家（用于调试命令）
    public static void forceShowAnnouncement(ServerPlayerEntity player) {
        sendAnnouncementConfig(player);
    }
}