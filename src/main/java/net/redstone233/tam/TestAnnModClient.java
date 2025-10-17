package net.redstone233.tam;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.text.Text;
import net.redstone233.tam.core.keys.ModKeys;
import net.redstone233.tam.gui.ConfigScreen;
import net.redstone233.tam.network.NetworkHandler;

public class TestAnnModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TestAnnMod.LOGGER.info("Hello Fabric world from client!");
        long start = System.currentTimeMillis();
        // 注册键位绑定
        ModKeys.register();
        TestAnnMod.LOGGER.info("Register keys in {}ms", System.currentTimeMillis() - start);
        // 注册客户端网络处理器
        NetworkHandler.registerClientReceivers();
        TestAnnMod.LOGGER.info("Register network handlers in {}ms", System.currentTimeMillis() - start);
        // 数据通信
        registerKeys();
        TestAnnMod.LOGGER.info("客户端部分注册完成，耗时 {}ms", System.currentTimeMillis() - start);
    }


    private static void registerKeys() {
        // 在 KeyInputHandler.java 中修改按键处理部分
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ModKeys.isAnnouncementKeyPressed()) {
                if (client.player != null && client.player.hasPermissionLevel(2)) {
                    // 打开配置屏幕
                    client.setScreen(ConfigScreen.create(client.currentScreen));
                } else {
                    if (client.player != null) {
                        client.player.sendMessage(
                                Text.literal("§c你需要OP权限才能打开配置界面!"),
                                false
                        );
                    }
                }
            }
        });
    }
}
