package net.redstone233.tam.core.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.redstone233.tam.manager.AnnouncementManager;

public class PlayerJoinEvent {
    public static void init() {
        // 注册玩家加入事件
        ServerPlayConnectionEvents.JOIN.register(
                AnnouncementManager::onPlayerJoin
        );
    }
}
