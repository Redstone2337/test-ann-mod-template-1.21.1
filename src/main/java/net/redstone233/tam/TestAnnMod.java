package net.redstone233.tam;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.MinecraftVersion;
import net.minecraft.util.Identifier;
import net.redstone233.tam.armor.ModArmorMaterials;
import net.redstone233.tam.commands.v1.BrewingRecipeCommand;
import net.redstone233.tam.commands.DebugCommands;
import net.redstone233.tam.config.ClientConfig;
import net.redstone233.tam.config.ConfigManager;
import net.redstone233.tam.core.brewing.EnhancedBrewingRecipeParser;
import net.redstone233.tam.core.brewing.RhinoBrewingRecipeParser;
import net.redstone233.tam.core.event.PlayerJoinEvent;
import net.redstone233.tam.core.mod.SuperFurnaceRegistration;
import net.redstone233.tam.enchantment.ModEnchantmentEffects;
import net.redstone233.tam.enchantment.ModEnchantments;
import net.redstone233.tam.item.ModItemGroups;
import net.redstone233.tam.item.ModItems;
import net.redstone233.tam.network.NetworkHandler;
import net.redstone233.tam.ponder.SuperBlastFurnaceScene;
import net.redstone233.tam.ponder.SuperFurnaceScene;
import net.redstone233.tam.ponder.SuperSmokerScene;
import net.redstone233.tam.ponder.tags.ModPonderTags;
import net.redstone233.tam.transaction.CustomTrades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TestAnnMod implements ModInitializer {
	public static final String MOD_ID = "tam";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static String MOD_VERSION;

    public static final String MC_VERSION = MinecraftVersion.create().getName();
    public static final String FABRIC_LOADER_VERSION = FabricLoader.getInstance().getModContainer("fabricloader")
            .orElseThrow().getMetadata().getVersion().getFriendlyString();
    public static final String FABRIC_API_VERSION = FabricLoader.getInstance().getModContainer("fabric-api")
            .orElseThrow().getMetadata().getVersion().toString();
    public static final String JS_VERSION = "1.0.0";
    public static final String SYNTAX = "ES2020";

    private EnhancedBrewingRecipeParser brewingParser;

    /**
 * 重写onInitialize方法，用于在Minecraft加载模组时进行初始化操作
 * 此方法会在Minecraft进入模组加载就绪状态时立即执行，但某些资源可能仍未完全初始化
 */
	@Override
	public void onInitialize() {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(MOD_ID);
        if (modContainer.isPresent()) {
            ModMetadata metadata = modContainer.get().getMetadata();
            String version = metadata.getVersion().getFriendlyString();
            // 现在你可以使用 version 变量了，例如输出到日志
            setModVersion(version);
        }
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        LOGGER.info("开始初始化公告模组内容...");
        long startTime = System.currentTimeMillis();
        // 初始化配置
        ClientConfig.init();
        LOGGER.info("模组配置初始化成功，总耗时 {}ms", System.currentTimeMillis() - startTime);
        // 初始化物品
        ModItems.registerItems();
        LOGGER.info("模组物品注册成功，总耗时 {}ms", System.currentTimeMillis() - startTime);
        // 初始化物品组
        ModItemGroups.register();
        LOGGER.info("模组物品组注册成功，总耗时 {}ms", System.currentTimeMillis() - startTime);
        // 注册模组附魔效果
        ModEnchantmentEffects.registerModEnchantments();
        LOGGER.info("模组附魔效果注册成功，总耗时 {}ms", System.currentTimeMillis() - startTime);
        // 注册模组附魔
        ModEnchantments.register();
        LOGGER.info("模组附魔注册成功，总耗时 {}ms", System.currentTimeMillis() - startTime);
        // 注册模组盔甲材料
        ModArmorMaterials.initialize();
        LOGGER.info("模组盔甲材料注册成功，总耗时 {}ms", System.currentTimeMillis() - startTime);
        // 初始化交易
        CustomTrades.init();
        LOGGER.info("模组交易初始化成功，总耗时 {}ms", System.currentTimeMillis() - startTime);

        // 注册网络处理器
        NetworkHandler.register();
        LOGGER.info("网络处理器注册成功，总耗时 {}ms", System.currentTimeMillis() - startTime);

        // 注册调试命令与配方重载命令
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            DebugCommands.register(dispatcher);
            LOGGER.info("模组调试命令注册成功，总耗时 {}ms", System.currentTimeMillis() - startTime);



            if (ConfigManager.isBrewingEnabled()) {
                BrewingRecipeCommand.register(dispatcher, brewingParser);
                LOGGER.info("模组自定义配方指令注册完成，总耗时 {}ms", System.currentTimeMillis() - startTime);
            } else {
                LOGGER.info("模组自定义配方指令未注册，因为酿造配方功能未启用，已自动进行跳过处理。");
            }
        });
        LOGGER.info("全部模组命令注册成功，总耗时 {}ms", System.currentTimeMillis() - startTime);

        // 注册人家加入事件
        PlayerJoinEvent.init();
        LOGGER.info("玩家加入事件初始化成功，总耗时 {}ms", System.currentTimeMillis() - startTime);

        SuperFurnaceScene.init();
        LOGGER.info("超级熔炉思索初始化完成，耗时{}ms", System.currentTimeMillis() - startTime);

        SuperBlastFurnaceScene.init();
        LOGGER.info("超级高炉思索初始化完成，耗时{}ms", System.currentTimeMillis() - startTime);

        SuperSmokerScene.init();
        LOGGER.info("超级烟熏炉思索初始化完成，耗时{}ms", System.currentTimeMillis() - startTime);

        ModPonderTags.init();
        LOGGER.info("思索标签初始化完成，耗时{}ms", System.currentTimeMillis() - startTime);

        SuperFurnaceRegistration.init();
        LOGGER.info("超级熔炼系统注册初始化完成，耗时{}ms", System.currentTimeMillis() - startTime);

        // 初始化增强的酿造配方解析器
        if (ConfigManager.isBrewingEnabled()) {
            brewingParser = new EnhancedBrewingRecipeParser(MOD_ID);
            LOGGER.info("增强的酿造配方解析器初始化完成，总耗时 {}ms", System.currentTimeMillis() - startTime);
        } else {
            LOGGER.info("增强的酿造配方解析器未初始化，因为酿造配方功能未启用，已自动进行跳过处理。");
        }

        RhinoBrewingRecipeParser.registerWithFabricToDataPack();
        LOGGER.info("酿造配方的数据包注册完成，总耗时 {}ms", System.currentTimeMillis() - startTime);

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            brewingParser.loadBrewingRecipes();
        });
        LOGGER.info("酿造指定路径配方加载完成，总耗时 {}ms", System.currentTimeMillis() - startTime);

        LOGGER.info("Test Announcement Mod initialized successfully");
        LOGGER.info("模组内容初始化完成，总耗时 {}ms", System.currentTimeMillis() - startTime);


    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static String getModVersion() {
        return MOD_VERSION;
    }

    public static void setModVersion(String modVersion) {
        MOD_VERSION = modVersion;
    }

    /**
     * 获取酿造配方解析器实例（用于命令等）
     */
    public EnhancedBrewingRecipeParser getBrewingParser() {
        return brewingParser;
    }
}