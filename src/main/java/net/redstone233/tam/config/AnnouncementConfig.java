package net.redstone233.tam.config;

import java.util.List;

public class AnnouncementConfig {
    public String mainTitle;
    public String subTitle;
    public List<String> announcementContent;
    public String confirmButtonText;
    public String submitButtonText;
    public String buttonLink;
    public boolean showIcon;
    public String iconPath;
    public int iconWidth;
    public int iconHeight;
    public int iconTextSpacing;
    public boolean useCustomRGB;
    public int mainTitleColor;
    public int subTitleColor;
    public int contentColor;
    public double scrollSpeed;
    public boolean useCustomAnnouncementBackground;
    public String announcementBackgroundPath;
    public boolean showPonderScreen;

    public AnnouncementConfig() {
        // 空构造函数用于反序列化
    }

    /**
     * 从配置管理器创建配置实例
     */
    public static AnnouncementConfig fromConfigManager() {
        return ConfigManager.createAnnouncementConfig();
    }

    /**
     * 验证配置的完整性
     */
    public boolean isValid() {
        return mainTitle != null &&
                subTitle != null &&
                announcementContent != null &&
                // 移除了 !announcementContent.isEmpty() 检查，允许空内容
                confirmButtonText != null &&
                submitButtonText != null &&
                buttonLink != null;
    }

    /**
     * 获取配置的哈希值，用于检测配置是否改变
     */
    public String getConfigHash() {
        StringBuilder sb = new StringBuilder();
        sb.append(mainTitle).append(subTitle).append(confirmButtonText)
                .append(submitButtonText).append(buttonLink).append(showIcon)
                .append(iconPath).append(useCustomRGB).append(useCustomAnnouncementBackground)
                .append(announcementBackgroundPath).append(showPonderScreen);

        for (String line : announcementContent) {
            sb.append(line);
        }

        return Integer.toHexString(sb.toString().hashCode());
    }
}
