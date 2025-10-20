package net.redstone233.tam.manager;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.redstone233.tam.config.ConfigManager;
import net.redstone233.tam.network.NetworkHandler;

public class PlayerAnnouncementManager {

    private static final String ANNOUNCEMENT_NBT_KEY = "TamLastSeenAnnouncementHash";

    // 移除了 init() 方法，内容已迁移到 PlayerJoinEvent

    /**
     * 统一的公告检查逻辑（ServerPlayerEntity 版本）
     */
    public static void onPlayerJoin(ServerPlayerEntity player) {
        if (!ConfigManager.shouldShowOnWorldEnter()) {
            return;
        }

        String currentHash = ConfigManager.getConfigHash();
        String playerLastHash = getPlayerAnnouncementHash(player);

        System.out.println("玩家 " + player.getGameProfile().getName() + " 公告检查 - 当前哈希: " + currentHash + ", 玩家上次哈希: " + playerLastHash);

        if (!currentHash.equals(playerLastHash)) {
            System.out.println("显示公告给玩家: " + player.getGameProfile().getName());
            NetworkHandler.sendAnnouncementConfig(player);
            setPlayerAnnouncementHash(player, currentHash);
        } else {
            System.out.println("玩家 " + player.getGameProfile().getName() + " 已经看过当前公告");
        }
    }

    /**
     * 适配 ServerPlayConnectionEvents.JOIN 事件的方法
     */
    public static void onPlayerJoin(ServerPlayNetworkHandler handler, net.fabricmc.fabric.api.networking.v1.PacketSender sender, net.minecraft.server.MinecraftServer server) {
        onPlayerJoin(handler.getPlayer());
    }

    /**
     * 从玩家的持久化 NBT 中读取公告哈希
     */
    private static String getPlayerAnnouncementHash(ServerPlayerEntity player) {
        NbtCompound nbt = new NbtCompound();
        player.writeCustomDataToNbt(nbt);
        if (nbt.contains(ANNOUNCEMENT_NBT_KEY, NbtCompound.STRING_TYPE)) {
            return nbt.getString(ANNOUNCEMENT_NBT_KEY);
        }
        return "";
    }

    /**
     * 向玩家的持久化 NBT 中写入公告哈希
     */
    private static void setPlayerAnnouncementHash(ServerPlayerEntity player, String hash) {
        NbtCompound nbt = new NbtCompound();
        player.writeCustomDataToNbt(nbt);
        nbt.putString(ANNOUNCEMENT_NBT_KEY, hash);
        player.readCustomDataFromNbt(nbt);
        System.out.println("已为玩家 " + player.getGameProfile().getName() + " 设置公告哈希: " + hash);
    }

    public static void forceShowToPlayer(ServerPlayerEntity player) {
        NetworkHandler.forceShowAnnouncement(player);
    }

    public static void resetPlayerAnnouncement(ServerPlayerEntity player) {
        setPlayerAnnouncementHash(player, "");
        System.out.println("已重置玩家 " + player.getGameProfile().getName() + " 的公告观看记录");
    }

    public static String getDebugInfo(ServerPlayerEntity player) {
        String currentHash = ConfigManager.getConfigHash();
        String playerHash = getPlayerAnnouncementHash(player);
        return String.format("当前公告哈希: %s, 玩家记录哈希: %s, 是否显示: %s",
                currentHash, playerHash, !currentHash.equals(playerHash));
    }
}