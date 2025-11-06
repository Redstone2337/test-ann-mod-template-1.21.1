// PlayerJoinEvent.java (更新后)
package net.redstone233.tam.core.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.redstone233.tam.manager.PlayerAnnouncementManager;

public class PlayerJoinEvent {
    public static void init() {
        // 注册玩家加入事件，使用新的管理器
//        ServerPlayConnectionEvents.JOIN.register(
//                PlayerAnnouncementManager::onPlayerJoin
//        );

        ServerPlayConnectionEvents.JOIN.register(
                PlayerAnnouncementManager::onPlayerJoin
        );

        // 注册维度切换事件（从PlayerAnnouncementManager迁移过来）
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) ->
                PlayerAnnouncementManager.onPlayerJoin(player)
        );
    }
}