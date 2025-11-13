package net.redstone233.tam.config;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.redstone233.tam.TestAnnMod;

import java.util.Arrays;
import java.util.List;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    // ==================== 通用设置 ====================
    private static void setupGeneralSettings() {
        ClientConfig.BUILDER.push("general");

        SHOW_ON_WORLD_ENTER = ClientConfig.BUILDER
                .comment("是否在进入世界时显示公告")
                .define("showOnWorldEnter", true);

        DEBUG_MODE = ClientConfig.BUILDER
                .comment("启用调试模式（显示UI边界等辅助信息）")
                .define("debugMode", false);

        ClientConfig.BUILDER.pop();
    }

    // ==================== 扩展设置 ====================
    private static void setupExtendSettings() {
        ClientConfig.BUILDER.push("extend");

        IS_ON_PONDER = ClientConfig.BUILDER
                .comment("是否启用思索提醒")
                .define("isOnPonder", true);

        BREWING_ENABLED = ClientConfig.BUILDER
                .comment("是否启用解析器添加酿造配方")
                .define("customBrewingEnabled", true);

        BREWING_RECIPE_TO_DATAPACK = ClientConfig.BUILDER
                .comment("是否启用数据包添加配方")
                .define("brewingRecipeDatapack", true);

        ClientConfig.BUILDER.pop();
    }


    // ==================== 显示设置 ====================
    private static void setupDisplaySettings() {
        ClientConfig.BUILDER.push("display");

        MAIN_TITLE = ClientConfig.BUILDER
                .comment("主标题文本")
                .define("mainTitle", "服务器公告");

        SUB_TITLE = ClientConfig.BUILDER
                .comment("副标题文本")
                .define("subTitle", "最新通知");

        SCROLL_SPEED = ClientConfig.BUILDER
                .comment("文本滚动速度")
                .defineInRange("scrollSpeed", 1, 1, 10);

        ClientConfig.BUILDER.pop();
    }

    // ==================== 颜色设置 ====================
    private static void setupColorSettings() {
        ClientConfig.BUILDER.push("colors");

        USE_CUSTOM_RGB = ClientConfig.BUILDER
                .comment("是否使用自定义RGB颜色")
                .define("useCustomRGB", false);

        MAIN_TITLE_COLOR = ClientConfig.BUILDER
                .comment("主标题颜色 (RGB整数，十六进制格式如0xFFFFFF)")
                .defineInRange("mainTitleColor", 0xFFFFFF, 0x000000, 0xFFFFFF);

        SUB_TITLE_COLOR = ClientConfig.BUILDER
                .comment("副标题颜色 (RGB整数，十六进制格式如0xCCCCCC)")
                .defineInRange("subTitleColor", 0xCCCCCC, 0x000000, 0xFFFFFF);

        CONTENT_COLOR = ClientConfig.BUILDER
                .comment("公告内容颜色 (RGB整数，十六进制格式如0xFFFFFF)")
                .defineInRange("contentColor", 0xFFFFFF, 0x000000, 0xFFFFFF);

        ClientConfig.BUILDER.pop();
    }

    // ==================== 按钮设置 ====================
    private static void setupButtonSettings() {
        ClientConfig.BUILDER.push("buttons");

        CONFIRM_BUTTON_TEXT = ClientConfig.BUILDER
                .comment("确定按钮文本")
                .define("confirmButtonText", "确定");

        SUBMIT_BUTTON_TEXT = ClientConfig.BUILDER
                .comment("前往投递按钮文本")
                .define("submitButtonText", "前往投递");

        BUTTON_LINK = ClientConfig.BUILDER
                .comment("按钮点击后打开的链接")
                .define("buttonLink", "https://github.com/Redstone2337/TestMod-1.21.7-fabric-master/issues");

        ClientConfig.BUILDER.pop();
    }

    // ==================== 图标设置 ====================
    private static void setupIconSettings() {
        ClientConfig.BUILDER.push("icon");

        SHOW_ICON = ClientConfig.BUILDER
                .comment("是否显示公告图标")
                .define("showIcon", false);

        ICON_PATH = ClientConfig.BUILDER
                .comment("图标资源路径")
                .define("path", "announcement_mod:textures/gui/icon.png");

        ICON_WIDTH = ClientConfig.BUILDER
                .comment("图标宽度 (像素)")
                .defineInRange("width", 32, 16, 128);

        ICON_HEIGHT = ClientConfig.BUILDER
                .comment("图标高度 (像素)")
                .defineInRange("height", 32, 16, 128);

        ICON_TEXT_SPACING = ClientConfig.BUILDER
                .comment("图标与文本的间距 (像素)")
                .defineInRange("textSpacing", 10, 0, 50);

        ClientConfig.BUILDER.pop();
    }

    // ==================== 背景设置 ====================
    private static void setupBackgroundSettings() {
        ClientConfig.BUILDER.push("background");

        USE_CUSTOM_ANNOUNCEMENT_BACKGROUND = ClientConfig.BUILDER
                .comment("是否使用自定义公告背景图")
                .define("enabled", false);

        ANNOUNCEMENT_BACKGROUND_PATH = ClientConfig.BUILDER
                .comment("公告背景图路径")
                .define("path", "announcement_mod:textures/gui/background.png");

        ClientConfig.BUILDER.pop();
    }

    // ==================== 内容设置 ====================
    private static void setupContentSettings() {
        ClientConfig.BUILDER.push("content");

        ANNOUNCEMENT_CONTENT = ClientConfig.BUILDER
                .comment("公告内容（每行一条，支持多行）")
                .defineListAllowEmpty("lines",
                        Arrays.asList(
                                "§a欢迎游玩，我们团队做的模组！",
                                " ",
                                "§e一些提醒：",
                                "§f1. 模组仅限于1.21.7~1.21.8 NeoForge",
                                "§f2. 模组目前是半成品",
                                "§f3. 后面会继续更新",
                                " ",
                                "§b模组随缘更新",
                                "§c若发现bug可以向模组作者或者仓库反馈！"
                        ),
                        ClientConfig::validateString);

        LAST_DISPLAYED_HASH = ClientConfig.BUILDER
                .comment("上次显示公告的哈希值（用于检测公告是否已修改）")
                .define("lastDisplayedHash", "");

        ClientConfig.BUILDER.pop();
    }

    // 配置字段声明
    public static ModConfigSpec.BooleanValue SHOW_ON_WORLD_ENTER;
    public static ModConfigSpec.BooleanValue DEBUG_MODE;
    public static ModConfigSpec.BooleanValue IS_ON_PONDER;
    public static ModConfigSpec.ConfigValue<String> MAIN_TITLE;
    public static ModConfigSpec.ConfigValue<String> SUB_TITLE;
    public static ModConfigSpec.IntValue SCROLL_SPEED;
    public static ModConfigSpec.BooleanValue USE_CUSTOM_RGB;
    public static ModConfigSpec.IntValue MAIN_TITLE_COLOR;
    public static ModConfigSpec.IntValue SUB_TITLE_COLOR;
    public static ModConfigSpec.IntValue CONTENT_COLOR;
    public static ModConfigSpec.ConfigValue<String> CONFIRM_BUTTON_TEXT;
    public static ModConfigSpec.ConfigValue<String> SUBMIT_BUTTON_TEXT;
    public static ModConfigSpec.ConfigValue<String> BUTTON_LINK;
    public static ModConfigSpec.BooleanValue SHOW_ICON;
    public static ModConfigSpec.ConfigValue<String> ICON_PATH;
    public static ModConfigSpec.IntValue ICON_WIDTH;
    public static ModConfigSpec.IntValue ICON_HEIGHT;
    public static ModConfigSpec.IntValue ICON_TEXT_SPACING;
    public static ModConfigSpec.BooleanValue USE_CUSTOM_ANNOUNCEMENT_BACKGROUND;
    public static ModConfigSpec.ConfigValue<String> ANNOUNCEMENT_BACKGROUND_PATH;
    public static ModConfigSpec.ConfigValue<List<? extends String>> ANNOUNCEMENT_CONTENT;
    public static ModConfigSpec.ConfigValue<String> LAST_DISPLAYED_HASH;
    public static ModConfigSpec.BooleanValue BREWING_ENABLED;
    public static ModConfigSpec.BooleanValue BREWING_RECIPE_TO_DATAPACK;

    static {
        // 按顺序初始化各个配置节
        setupGeneralSettings();
        setupDisplaySettings();
        setupColorSettings();
        setupButtonSettings();
        setupIconSettings();
        setupBackgroundSettings();
        setupContentSettings();
        setupExtendSettings();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    // 验证方法
    private static boolean validateString(final Object obj) {
        return obj instanceof String;
    }

    // 初始化配置
    public static void init() {
        NeoForgeConfigRegistry.INSTANCE.register(TestAnnMod.MOD_ID, ModConfig.Type.CLIENT, SPEC, "tam_config.toml");
    }

    // 配置加载事件处理
    public static void onConfigLoad(final NeoForgeModConfigEvents.Loading configEvent) {
        validateConfig();
        System.out.println("Announcement Mod 配置加载完成");
    }

    public static void onConfigReload(final NeoForgeModConfigEvents.Reloading configEvent) {
        validateConfig();
        System.out.println("Announcement Mod 配置重新加载");
    }

    // 配置验证
    private static void validateConfig() {
        // 验证颜色值在合理范围内
        if (MAIN_TITLE_COLOR.get() < 0 || MAIN_TITLE_COLOR.get() > 0xFFFFFF) {
            System.err.println("主标题颜色值超出范围");
        }

        if (SUB_TITLE_COLOR.get() < 0 || SUB_TITLE_COLOR.get() > 0xFFFFFF) {
            System.err.println("副标题颜色值超出范围");
        }

        if (CONTENT_COLOR.get() < 0 || CONTENT_COLOR.get() > 0xFFFFFF) {
            System.err.println("内容颜色值超出范围");
        }

        // 验证滚动速度
        if (SCROLL_SPEED.get() < 1 || SCROLL_SPEED.get() > 10) {
            System.err.println("滚动速度超出范围");
        }
    }
}
