// PlayerAnnouncementManager.java (修复版本)
package net.redstone233.tam.manager;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.redstone233.tam.config.ConfigManager;
import net.redstone233.tam.network.NetworkHandler;

public class PlayerAnnouncementManager {

    private static final String ANNOUNCEMENT_NBT_KEY = "TamLastSeenAnnouncementHash";

    /**
     * 玩家加入世界时的处理
     */
    public static void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();

        if (!ConfigManager.shouldShowOnWorldEnter()) {
            return;
        }

        String currentHash = ConfigManager.getConfigHash();
        String playerLastHash = getPlayerAnnouncementHash(player);

        System.out.println("玩家 " + player.getGameProfile().getName() + " 公告检查 - 当前哈希: " + currentHash + ", 玩家上次哈希: " + playerLastHash);

        // 只有当哈希值不同时才显示公告
        if (!currentHash.equals(playerLastHash)) {
            System.out.println("显示公告给玩家: " + player.getGameProfile().getName());
            NetworkHandler.sendAnnouncementConfig(player);
            setPlayerAnnouncementHash(player, currentHash);
        } else {
            System.out.println("玩家 " + player.getGameProfile().getName() + " 已经看过当前公告");
        }
    }

    /**
     * 获取玩家存储的公告哈希值 - 使用 NBT 数据
     */
    private static String getPlayerAnnouncementHash(ServerPlayerEntity player) {
        // 从玩家的自定义 NBT 数据中获取
        NbtCompound nbt = player.writeNbt(new NbtCompound());

        if (nbt.contains("custom_data", 10)) {
            NbtCompound customData = nbt.getCompound("custom_data");
            if (customData.contains(ANNOUNCEMENT_NBT_KEY, 8)) {
                return customData.getString(ANNOUNCEMENT_NBT_KEY);
            }
        }
        return ""; // 如果玩家没有记录，返回空字符串
    }

    /**
     * 设置玩家的公告哈希值 - 使用 NBT 数据
     */
    private static void setPlayerAnnouncementHash(ServerPlayerEntity player, String hash) {
        // 读取玩家当前的 NBT 数据
        NbtCompound nbt = player.writeNbt(new NbtCompound());

        // 获取或创建自定义数据部分
        NbtCompound customData;
        if (nbt.contains("custom_data", 10)) {
            customData = nbt.getCompound("custom_data");
        } else {
            customData = new NbtCompound();
        }

        // 设置公告哈希值
        customData.putString(ANNOUNCEMENT_NBT_KEY, hash);
        nbt.put("custom_data", customData);

        // 将修改后的 NBT 数据写回玩家
        player.readNbt(nbt);

        // 标记玩家数据需要保存（通过同步统计数据来触发保存）
        player.getStatHandler().updateStatSet();

        System.out.println("已为玩家 " + player.getGameProfile().getName() + " 设置公告哈希: " + hash);
    }

    /**
     * 强制显示公告给玩家（忽略哈希检查）
     */
    public static void forceShowToPlayer(ServerPlayerEntity player) {
        NetworkHandler.forceShowAnnouncement(player);
    }

    /**
     * 重置玩家的公告观看记录
     */
    public static void resetPlayerAnnouncement(ServerPlayerEntity player) {
        setPlayerAnnouncementHash(player, "");
        System.out.println("已重置玩家 " + player.getGameProfile().getName() + " 的公告观看记录");
    }

    /**
     * 获取调试信息
     */
    public static String getDebugInfo(ServerPlayerEntity player) {
        String currentHash = ConfigManager.getConfigHash();
        String playerHash = getPlayerAnnouncementHash(player);
        return String.format("当前公告哈希: %s, 玩家记录哈希: %s, 是否显示: %s",
                currentHash, playerHash, !currentHash.equals(playerHash));
    }
}