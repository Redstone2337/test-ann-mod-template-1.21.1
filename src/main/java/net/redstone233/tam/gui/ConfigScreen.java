package net.redstone233.tam.gui;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.redstone233.tam.TestAnnMod;
import net.redstone233.tam.config.ConfigManager;

import java.util.Arrays;
import java.util.List;

public class ConfigScreen {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.tam.config"))
                .setSavingRunnable(ConfigScreen::saveConfig);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        setupGeneralCategory(builder, entryBuilder);
        setupDisplayCategory(builder, entryBuilder);
        setupColorCategory(builder, entryBuilder);
        setupButtonCategory(builder, entryBuilder);
        setupIconCategory(builder, entryBuilder);
        setupBackgroundCategory(builder, entryBuilder);
        setupContentCategory(builder, entryBuilder);
        setupExtendCategory(builder, entryBuilder);
        setupSystemCategory(builder, entryBuilder);

        return builder.build();
    }

    private static void setupGeneralCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.tam.general"));

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.tam.show_on_world_enter"),
                        ConfigManager.shouldShowOnWorldEnter())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("tooltip.tam.show_on_world_enter"))
                .setSaveConsumer(ConfigManager::setShowOnWorldEnter)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.tam.debug_mode"),
                        ConfigManager.isDebugMode())
                .setDefaultValue(false)
                .setTooltip(Text.translatable("tooltip.tam.debug_mode"))
                .setSaveConsumer(ConfigManager::setDebugMode)
                .build());

    }

    private static void setupExtendCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory extend = builder.getOrCreateCategory(Text.translatable("category.tam.extend"));

        extend.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.tam.show_ponder"),
                        ConfigManager.showPonderScreen())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("tooltip.tam.show_ponder"))
                .setSaveConsumer(ConfigManager::setPonderScreen)
                .build());

        extend.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.tam.brewing_enabled"),
                        ConfigManager.isBrewingEnabled())
                .setDefaultValue(true)
                .setTooltip(Text.translatable("tooltip.tam.brewing_enabled"))
                .setSaveConsumer(ConfigManager::setBrewingRecipe)
                .build());
    }

    private static void setupSystemCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory system = builder.getOrCreateCategory(Text.translatable("category.tam.system"));
        system.addEntry(entryBuilder.startTextDescription(Text.translatable("description.tam.commands"))
                .build());
        system.addEntry(entryBuilder.startTextDescription(Text.literal(getStackSystemStatus())).build());

    }

    private static String getStackSystemStatus() {
        try {
            String ver = TestAnnMod.getModVersion();
            String mcv = MinecraftVersion.create().getName();
            String loaderVersion = FabricLoader.getInstance().getModContainer("fabricloader")
                    .orElseThrow().getMetadata().getVersion().getFriendlyString();
            String apiVersion = FabricLoader.getInstance().getModContainer("fabric-api")
                    .orElseThrow().getMetadata().getVersion().toString();

            return String.format("模组版本：%s, \n游戏版本：%s, \n运行器版本：%s, \nAPI版本：%s",
                    ver, mcv, loaderVersion, apiVersion);
        } catch (Exception e) {
            return "状态获取失败";
        }
    }

    private static void setupDisplayCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory display = builder.getOrCreateCategory(Text.translatable("category.tam.display"));

        display.addEntry(entryBuilder.startStrField(Text.translatable("option.tam.main_title"),
                        ConfigManager.getMainTitle())
                .setDefaultValue("服务器公告")
                .setTooltip(Text.translatable("tooltip.tam.main_title"))
                .setSaveConsumer(ConfigManager::setMainTitle)
                .build());

        display.addEntry(entryBuilder.startStrField(Text.translatable("option.tam.sub_title"),
                        ConfigManager.getSubTitle())
                .setDefaultValue("最新通知")
                .setTooltip(Text.translatable("tooltip.tam.sub_title"))
                .setSaveConsumer(ConfigManager::setSubTitle)
                .build());

        display.addEntry(entryBuilder.startIntSlider(Text.translatable("option.tam.scroll_speed"),
                        ConfigManager.getScrollSpeed(), 1, 10)
                .setDefaultValue(1)
                .setTooltip(Text.translatable("tooltip.tam.scroll_speed"))
                .setSaveConsumer(ConfigManager::setScrollSpeed)
                .build());
    }

    private static void setupColorCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory colors = builder.getOrCreateCategory(Text.translatable("category.tam.colors"));

        colors.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.tam.use_custom_rgb"),
                        ConfigManager.useCustomRGB())
                .setDefaultValue(false)
                .setTooltip(Text.translatable("tooltip.tam.use_custom_rgb"))
                .setSaveConsumer(ConfigManager::setUseCustomRGB)
                .build());

        colors.addEntry(entryBuilder.startColorField(Text.translatable("option.tam.main_title_color"),
                        ConfigManager.getMainTitleColor())
                .setDefaultValue(0xFFFFFF)
                .setTooltip(Text.translatable("tooltip.tam.main_title_color"))
                .setSaveConsumer(ConfigManager::setMainTitleColor)
                .build());

        colors.addEntry(entryBuilder.startColorField(Text.translatable("option.tam.sub_title_color"),
                        ConfigManager.getSubTitleColor())
                .setDefaultValue(0xCCCCCC)
                .setTooltip(Text.translatable("tooltip.tam.sub_title_color"))
                .setSaveConsumer(ConfigManager::setSubTitleColor)
                .build());

        colors.addEntry(entryBuilder.startColorField(Text.translatable("option.tam.content_color"),
                        ConfigManager.getContentColor())
                .setDefaultValue(0xFFFFFF)
                .setTooltip(Text.translatable("tooltip.tam.content_color"))
                .setSaveConsumer(ConfigManager::setContentColor)
                .build());
    }

    private static void setupButtonCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory buttons = builder.getOrCreateCategory(Text.translatable("category.tam.buttons"));

        buttons.addEntry(entryBuilder.startStrField(Text.translatable("option.tam.confirm_button_text"),
                        ConfigManager.getConfirmButtonText())
                .setDefaultValue("确定")
                .setTooltip(Text.translatable("tooltip.tam.confirm_button_text"))
                .setSaveConsumer(ConfigManager::setConfirmButtonText)
                .build());

        buttons.addEntry(entryBuilder.startStrField(Text.translatable("option.tam.submit_button_text"),
                        ConfigManager.getSubmitButtonText())
                .setDefaultValue("前往投递")
                .setTooltip(Text.translatable("tooltip.tam.submit_button_text"))
                .setSaveConsumer(ConfigManager::setSubmitButtonText)
                .build());

        buttons.addEntry(entryBuilder.startStrField(Text.translatable("option.tam.button_link"),
                        ConfigManager.getButtonLink())
                .setDefaultValue("https://github.com/Redstone2337/TestMod-1.21.7-fabric-master/issues")
                .setTooltip(Text.translatable("tooltip.tam.button_link"))
                .setSaveConsumer(ConfigManager::setButtonLink)
                .build());
    }

    private static void setupIconCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory icon = builder.getOrCreateCategory(Text.translatable("category.tam.icon"));

        icon.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.tam.show_icon"),
                        ConfigManager.shouldShowIcon())
                .setDefaultValue(false)
                .setTooltip(Text.translatable("tooltip.tam.show_icon"))
                .setSaveConsumer(ConfigManager::setShowIcon)
                .build());

        icon.addEntry(entryBuilder.startStrField(Text.translatable("option.tam.icon_path"),
                        ConfigManager.getIconPath())
                .setDefaultValue("announcement_mod:textures/gui/icon.png")
                .setTooltip(Text.translatable("tooltip.tam.icon_path"))
                .setSaveConsumer(ConfigManager::setIconPath)
                .build());

        icon.addEntry(entryBuilder.startIntField(Text.translatable("option.tam.icon_width"),
                        ConfigManager.getIconWidth())
                .setDefaultValue(32)
                .setMin(16)
                .setMax(128)
                .setTooltip(Text.translatable("tooltip.tam.icon_width"))
                .setSaveConsumer(ConfigManager::setIconWidth)
                .build());

        icon.addEntry(entryBuilder.startIntField(Text.translatable("option.tam.icon_height"),
                        ConfigManager.getIconHeight())
                .setDefaultValue(32)
                .setMin(16)
                .setMax(128)
                .setTooltip(Text.translatable("tooltip.tam.icon_height"))
                .setSaveConsumer(ConfigManager::setIconHeight)
                .build());

        icon.addEntry(entryBuilder.startIntField(Text.translatable("option.tam.icon_text_spacing"),
                        ConfigManager.getIconTextSpacing())
                .setDefaultValue(10)
                .setMin(0)
                .setMax(50)
                .setTooltip(Text.translatable("tooltip.tam.icon_text_spacing"))
                .setSaveConsumer(ConfigManager::setIconTextSpacing)
                .build());
    }

    private static void setupBackgroundCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory background = builder.getOrCreateCategory(Text.translatable("category.tam.background"));

        background.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.tam.use_custom_background"),
                        ConfigManager.useCustomAnnouncementBackground())
                .setDefaultValue(false)
                .setTooltip(Text.translatable("tooltip.tam.use_custom_background"))
                .setSaveConsumer(ConfigManager::setUseCustomAnnouncementBackground)
                .build());

        background.addEntry(entryBuilder.startStrField(Text.translatable("option.tam.background_path"),
                        ConfigManager.getAnnouncementBackgroundPath())
                .setDefaultValue("announcement_mod:textures/gui/background.png")
                .setTooltip(Text.translatable("tooltip.tam.background_path"))
                .setSaveConsumer(ConfigManager::setAnnouncementBackgroundPath)
                .build());
    }

    private static void setupContentCategory(ConfigBuilder builder, ConfigEntryBuilder entryBuilder) {
        ConfigCategory content = builder.getOrCreateCategory(Text.translatable("category.tam.content"));

        List<String> currentContent = ConfigManager.getAnnouncementContent();

        content.addEntry(entryBuilder.startTextDescription(
                Text.translatable("description.tam.announcement_content")).build());

        content.addEntry(entryBuilder.startStrList(Text.translatable("option.tam.announcement_content"),
                        currentContent)
                .setDefaultValue(Arrays.asList(
                        "§a欢迎游玩，我们团队做的模组！",
                        " ",
                        "§e一些提醒：",
                        "§f1. 模组仅限于1.21.7~1.21.8 NeoForge",
                        "§f2. 模组目前是半成品",
                        "§f3. 后面会继续更新",
                        " ",
                        "§b模组随缘更新",
                        "§c若发现bug可以向模组作者或者仓库反馈！"
                ))
                .setTooltip(Text.translatable("tooltip.tam.announcement_content"))
                .setSaveConsumer(ConfigManager::setAnnouncementContent)
                .setInsertButtonEnabled(true)
                .setDeleteButtonEnabled(true)
                .setExpanded(true)
                .build());
    }

    private static void saveConfig() {
        try {
            ConfigManager.saveConfig();
            TestAnnMod.LOGGER.info("公告模组配置已保存");
        } catch (Exception e) {
            TestAnnMod.LOGGER.error("保存配置时出错", e);
        }
    }
}