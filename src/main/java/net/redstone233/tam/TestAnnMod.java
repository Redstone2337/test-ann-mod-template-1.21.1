package net.redstone233.tam;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.Identifier;
import net.redstone233.tam.commands.DebugCommands;
import net.redstone233.tam.config.ClientConfig;
import net.redstone233.tam.core.event.PlayerJoinEvent;
import net.redstone233.tam.network.AnnouncementPayload;
import net.redstone233.tam.network.NetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAnnMod implements ModInitializer {
	public static final String MOD_ID = "tam";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        LOGGER.info("开始初始化公告模组内容...");
        long startTime = System.currentTimeMillis();
        // 初始化配置
        ClientConfig.init();
        LOGGER.info("模组配置初始化成功，总耗时 {}ms", System.currentTimeMillis() - startTime);

        // 注册网络处理器
        NetworkHandler.register();
        LOGGER.info("网络处理器注册成功，总耗时 {}ms", System.currentTimeMillis() - startTime);

        // 注册调试命令
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            DebugCommands.register(dispatcher);
        });
        LOGGER.info("调试命令注册成功，总耗时 {}ms", System.currentTimeMillis() - startTime);

        // 注册人家加入事件
        PlayerJoinEvent.init();
        LOGGER.info("玩家加入事件初始化成功，总耗时 {}ms", System.currentTimeMillis() - startTime);

        LOGGER.info("Test Announcement Mod initialized successfully");
        LOGGER.info("模组内容初始化完成，总耗时 {}ms", System.currentTimeMillis() - startTime);
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}