// PlayerJoinEvent.java (更新后)
package net.redstone233.tam.core.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.redstone233.tam.manager.PlayerAnnouncementManager;

public class PlayerJoinEvent {
    public static void init() {
        // 注册玩家加入事件，使用新的管理器
        ServerPlayConnectionEvents.JOIN.register(
                PlayerAnnouncementManager::onPlayerJoin
        );
    }
}