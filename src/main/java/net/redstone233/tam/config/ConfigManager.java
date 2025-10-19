package net.redstone233.tam.config;

import net.minecraft.util.Formatting;
import net.redstone233.tam.TestAnnMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {

    // ==================== 通用设置 ====================
    public static boolean shouldShowOnWorldEnter() {
        return ClientConfig.SHOW_ON_WORLD_ENTER.get();
    }

    public static boolean isDebugMode() {
        return ClientConfig.DEBUG_MODE.get();
    }

    public static boolean showPonderScreen() {
        return ClientConfig.IS_ON_PONDER.get();
    }

    // ==================== 显示设置 ====================
    public static String getMainTitle() {
        return ClientConfig.MAIN_TITLE.get();
    }

    public static String getSubTitle() {
        return ClientConfig.SUB_TITLE.get();
    }

    public static int getScrollSpeed() {
        return ClientConfig.SCROLL_SPEED.get();
    }

    // ==================== 颜色设置 ====================
    public static boolean useCustomRGB() {
        return ClientConfig.USE_CUSTOM_RGB.get();
    }

    public static int getMainTitleColor() {
        return ClientConfig.MAIN_TITLE_COLOR.get();
    }

    public static int getSubTitleColor() {
        return ClientConfig.SUB_TITLE_COLOR.get();
    }

    public static int getContentColor() {
        return ClientConfig.CONTENT_COLOR.get();
    }

    // ==================== 按钮设置 ====================
    public static String getConfirmButtonText() {
        return ClientConfig.CONFIRM_BUTTON_TEXT.get();
    }

    public static String getSubmitButtonText() {
        return ClientConfig.SUBMIT_BUTTON_TEXT.get();
    }

    public static String getButtonLink() {
        return ClientConfig.BUTTON_LINK.get();
    }

    // ==================== 图标设置 ====================
    public static boolean shouldShowIcon() {
        return ClientConfig.SHOW_ICON.get();
    }

    public static String getIconPath() {
        return ClientConfig.ICON_PATH.get();
    }

    public static int getIconWidth() {
        return ClientConfig.ICON_WIDTH.get();
    }

    public static int getIconHeight() {
        return ClientConfig.ICON_HEIGHT.get();
    }

    public static int getIconTextSpacing() {
        return ClientConfig.ICON_TEXT_SPACING.get();
    }

    // ==================== 背景设置 ====================
    public static boolean useCustomAnnouncementBackground() {
        return ClientConfig.USE_CUSTOM_ANNOUNCEMENT_BACKGROUND.get();
    }

    public static String getAnnouncementBackgroundPath() {
        return ClientConfig.ANNOUNCEMENT_BACKGROUND_PATH.get();
    }

    // ==================== 内容设置 ====================
    public static List<String> getAnnouncementContent() {
        try {
            // 安全地转换类型
            List<?> rawList = ClientConfig.ANNOUNCEMENT_CONTENT.get();
            List<String> result = new ArrayList<>();

            for (Object item : rawList) {
                if (item instanceof String) {
                    result.add((String) item);
                }
            }

            // 如果列表为空，返回默认内容
            if (result.isEmpty()) {
                return getDefaultAnnouncementContent();
            }

            return result;
        } catch (Exception e) {
            TestAnnMod.LOGGER.warn("获取公告内容时出错，使用默认值", e);
            return getDefaultAnnouncementContent();
        }
    }

    private static List<String> getDefaultAnnouncementContent() {
        return List.of(
                "§a欢迎游玩，我们团队做的模组！",
                " ",
                "§e一些提醒：",
                "§f1. 模组仅限于1.21.7~1.21.8 NeoForge",
                "§f2. 模组目前是半成品",
                "§f3. 后面会继续更新",
                " ",
                "§b模组随缘更新",
                "§c若发现bug可以向模组作者或者仓库反馈！"
        );
    }

    public static String getLastDisplayedHash() {
        return ClientConfig.LAST_DISPLAYED_HASH.get();
    }

//    public static void setLastDisplayedHash(String hash) {
//        // 注意：在NeoForge中直接修改配置值比较复杂
//        // 通常需要通过配置文件系统来修改，这里暂时留空
//    }

    // ==================== 辅助方法 ====================

    // 辅助方法：从颜色值获取Formatting枚举
    public static Formatting getFormattingFromColor(int color) {
        int rgbColor = color & 0xFFFFFF;

        for (Formatting formatting : Formatting.values()) {
            if (formatting.isColor() && formatting.getColorValue() != null) {
                int formattingColor = formatting.getColorValue();
                if ((formattingColor & 0xFFFFFF) == rgbColor) {
                    return formatting;
                }
            }
        }
        return Formatting.WHITE;
    }

    // 辅助方法：从Formatting枚举获取颜色值
    public static int getColorFromFormatting(Formatting formatting) {
        return formatting.getColorValue() != null ? formatting.getColorValue() : 0xFFFFFF;
    }

    /**
     * 创建公告配置对象（用于网络传输和客户端显示）
     */
    public static AnnouncementConfig createAnnouncementConfig() {
        AnnouncementConfig config = new AnnouncementConfig();

        config.mainTitle = getMainTitle();
        config.subTitle = getSubTitle();
        config.announcementContent = getAnnouncementContent();
        config.confirmButtonText = getConfirmButtonText();
        config.submitButtonText = getSubmitButtonText();
        config.buttonLink = getButtonLink();
        config.showIcon = shouldShowIcon();
        config.iconPath = getIconPath();
        config.iconWidth = getIconWidth();
        config.iconHeight = getIconHeight();
        config.iconTextSpacing = getIconTextSpacing();
        config.useCustomRGB = useCustomRGB();
        config.mainTitleColor = getMainTitleColor();
        config.subTitleColor = getSubTitleColor();
        config.contentColor = getContentColor();
        config.scrollSpeed = getScrollSpeed();
        config.useCustomAnnouncementBackground = useCustomAnnouncementBackground();
        config.announcementBackgroundPath = getAnnouncementBackgroundPath();
        config.showPonderScreen = showPonderScreen();

        return config;
    }

    /**
     * 验证配置的完整性
     */
    public static boolean validateConfiguration() {
        try {
            // 测试获取所有配置值
            getMainTitle();
            getSubTitle();
            getAnnouncementContent();
            getMainTitleColor();
            getSubTitleColor();
            getContentColor();
            shouldShowOnWorldEnter();
            isDebugMode();
            shouldShowIcon();
            getIconPath();
            getIconWidth();
            getIconHeight();
            getIconTextSpacing();
            getScrollSpeed();
            getConfirmButtonText();
            getSubmitButtonText();
            getButtonLink();
            useCustomRGB();
            useCustomAnnouncementBackground();
            getAnnouncementBackgroundPath();
            getLastDisplayedHash();
            showPonderScreen();

            return true;
        } catch (Exception e) {
            TestAnnMod.LOGGER.error("配置验证失败", e);
            return false;
        }
    }

    /**
     * 获取配置的哈希值，用于检测配置是否发生变化
     */
    public static String getConfigHash() {
        AnnouncementConfig config = createAnnouncementConfig();
        return config.getConfigHash();
    }

    /**
     * 检查配置是否已更改
     */
    public static boolean hasConfigChanged() {
        String currentHash = getConfigHash();
        String lastHash = getLastDisplayedHash();
        return !currentHash.equals(lastHash);
    }

    /**
     * 重新加载配置并返回新的配置对象
     */
    public static AnnouncementConfig reloadConfig() {
        // 在NeoForge中，配置会自动重新加载
        // 这里主要是为了返回最新的配置对象
        return createAnnouncementConfig();
    }

    // ==================== 设置方法 ====================

    public static void setShowOnWorldEnter(boolean value) {
        ClientConfig.SHOW_ON_WORLD_ENTER.set(value);
    }

    public static void setDebugMode(boolean value) {
        ClientConfig.DEBUG_MODE.set(value);
    }

    public static void setMainTitle(String value) {
        ClientConfig.MAIN_TITLE.set(value);
    }

    public static void setSubTitle(String value) {
        ClientConfig.SUB_TITLE.set(value);
    }

    public static void setScrollSpeed(int value) {
        ClientConfig.SCROLL_SPEED.set(value);
    }

    public static void setUseCustomRGB(boolean value) {
        ClientConfig.USE_CUSTOM_RGB.set(value);
    }

    public static void setMainTitleColor(int value) {
        ClientConfig.MAIN_TITLE_COLOR.set(value);
    }

    public static void setSubTitleColor(int value) {
        ClientConfig.SUB_TITLE_COLOR.set(value);
    }

    public static void setContentColor(int value) {
        ClientConfig.CONTENT_COLOR.set(value);
    }

    public static void setConfirmButtonText(String value) {
        ClientConfig.CONFIRM_BUTTON_TEXT.set(value);
    }

    public static void setSubmitButtonText(String value) {
        ClientConfig.SUBMIT_BUTTON_TEXT.set(value);
    }

    public static void setButtonLink(String value) {
        ClientConfig.BUTTON_LINK.set(value);
    }

    public static void setShowIcon(boolean value) {
        ClientConfig.SHOW_ICON.set(value);
    }

    public static void setIconPath(String value) {
        ClientConfig.ICON_PATH.set(value);
    }

    public static void setIconWidth(int value) {
        ClientConfig.ICON_WIDTH.set(value);
    }

    public static void setIconHeight(int value) {
        ClientConfig.ICON_HEIGHT.set(value);
    }

    public static void setIconTextSpacing(int value) {
        ClientConfig.ICON_TEXT_SPACING.set(value);
    }

    public static void setUseCustomAnnouncementBackground(boolean value) {
        ClientConfig.USE_CUSTOM_ANNOUNCEMENT_BACKGROUND.set(value);
    }

    public static void setAnnouncementBackgroundPath(String value) {
        ClientConfig.ANNOUNCEMENT_BACKGROUND_PATH.set(value);
    }

    public static void setAnnouncementContent(List<String> content) {
        // 这里需要将 List<String> 转换为 ConfigSpec 接受的类型
        ClientConfig.ANNOUNCEMENT_CONTENT.set(content);
    }

    public static void setLastDisplayedHash(String hash) {
        ClientConfig.LAST_DISPLAYED_HASH.set(hash);
    }

    public static void setPonderScreen(boolean value) {
        ClientConfig.IS_ON_PONDER.set(value);
    }


    /**
     * 保存配置到文件
     */
    public static void saveConfig() {
        // 在 NeoForge 中，配置会自动保存
        // 这里可以添加额外的保存逻辑
        if (ClientConfig.SPEC.isLoaded()) {
            // 触发配置保存
            ClientConfig.SPEC.save();
        }
    }

    /**
     * 重置为默认值
     */
    public static void resetToDefaults() {
        // 重置所有配置值为默认值
        setShowOnWorldEnter(true);
        setDebugMode(false);
        setMainTitle("服务器公告");
        setSubTitle("最新通知");
        setScrollSpeed(1);
        setUseCustomRGB(false);
        setMainTitleColor(0xFFFFFF);
        setSubTitleColor(0xCCCCCC);
        setContentColor(0xFFFFFF);
        setConfirmButtonText("确定");
        setSubmitButtonText("前往投递");
        setButtonLink("https://github.com/Redstone2337/TestMod-1.21.7-fabric-master/issues");
        setShowIcon(false);
        setIconPath("announcement_mod:textures/gui/icon.png");
        setIconWidth(32);
        setIconHeight(32);
        setIconTextSpacing(10);
        setUseCustomAnnouncementBackground(false);
        setAnnouncementBackgroundPath("announcement_mod:textures/gui/background.png");
        setPonderScreen(true);

        // 重置公告内容
        setAnnouncementContent(Arrays.asList(
                "§a欢迎游玩，我们团队做的模组！",
                " ",
                "§e一些提醒：",
                "§f1. 模组仅限于1.21.7~1.21.8 NeoForge",
                "§f2. 模组目前是半成品",
                "§f3. 后面会继续更新",
                " ",
                "§b模组随缘更新",
                "§c若发现bug可以向模组作者或者仓库反馈！"
        ));

        setLastDisplayedHash("");
    }
}
