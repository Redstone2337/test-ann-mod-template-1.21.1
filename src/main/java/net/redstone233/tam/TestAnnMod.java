package net.redstone233.tam;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.Identifier;
import net.redstone233.tam.armor.ModArmorMaterials;
import net.redstone233.tam.commands.v1.BrewingRecipeCommand;
import net.redstone233.tam.commands.DebugCommands;
import net.redstone233.tam.config.ClientConfig;
import net.redstone233.tam.core.brewing.BrewingRecipeParser;
import net.redstone233.tam.core.brewing.EnhancedBrewingRecipeParser;
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

public class TestAnnMod implements ModInitializer {
	public static final String MOD_ID = "tam";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final String MOD_VERSION = "0.1+build.10-live.1";

    private EnhancedBrewingRecipeParser brewingParser;

    /**
 * 重写onInitialize方法，用于在Minecraft加载模组时进行初始化操作
 * 此方法会在Minecraft进入模组加载就绪状态时立即执行，但某些资源可能仍未完全初始化
 */
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
            BrewingRecipeCommand.register(dispatcher, brewingParser);
        });
        LOGGER.info("模组命令注册成功，总耗时 {}ms", System.currentTimeMillis() - startTime);

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
        brewingParser = new EnhancedBrewingRecipeParser(MOD_ID);
        LOGGER.info("增强的酿造配方解析器初始化完成，总耗时 {}ms", System.currentTimeMillis() - startTime);

        LOGGER.info("Test Announcement Mod initialized successfully");
        LOGGER.info("模组内容初始化完成，总耗时 {}ms", System.currentTimeMillis() - startTime);

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            brewingParser.loadBrewingRecipes();
        });
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    /**
     * 获取酿造配方解析器实例（用于命令等）
     */
    public EnhancedBrewingRecipeParser getBrewingParser() {
        return brewingParser;
    }
}