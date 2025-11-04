package net.redstone233.tam.config;

import java.util.List;
import java.util.Objects;

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
    public boolean brewingEnabled;

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
     * 获取稳定且可靠的配置哈希值
     * 只对实际影响显示的内容进行哈希计算，忽略无关字段
     */
    public String getStableConfigHash() {
        StringBuilder sb = new StringBuilder();

        // 只包含真正影响公告显示的内容
        if (mainTitle != null) {
            sb.append(normalizeString(mainTitle));
        }

        if (subTitle != null) {
            sb.append(normalizeString(subTitle));
        }

        // 处理公告内容 - 忽略空行和纯空格行
        if (announcementContent != null) {
            for (String line : announcementContent) {
                if (line != null && !normalizeString(line).isEmpty()) {
                    sb.append(normalizeString(line));
                }
            }
        }

        // 按钮文本也会影响显示
        if (confirmButtonText != null) {
            sb.append(normalizeString(confirmButtonText));
        }

        if (submitButtonText != null) {
            sb.append(normalizeString(submitButtonText));
        }

        // 图标和背景的显示状态会影响UI布局
        sb.append(showIcon);
        if (showIcon && iconPath != null) {
            sb.append(normalizeString(iconPath));
        }

        sb.append(useCustomAnnouncementBackground);
        if (useCustomAnnouncementBackground && announcementBackgroundPath != null) {
            sb.append(normalizeString(announcementBackgroundPath));
        }

        // 颜色设置会影响视觉效果
        if (useCustomRGB) {
            sb.append(mainTitleColor).append(subTitleColor).append(contentColor);
        }

        // 滚动速度和显示模式
        sb.append((int)(scrollSpeed * 10)); // 将小数转换为整数避免浮点精度问题
        // 扩展内容
        sb.append(showPonderScreen);
        sb.append(brewingEnabled);

        // 使用更可靠的哈希算法
        return Integer.toHexString(Objects.hash(sb.toString()));
    }

    /**
     * 标准化字符串：去除首尾空格，将连续多个空格替换为单个空格
     */
    private String normalizeString(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("\\s+", " ");
    }

    /**
     * 向后兼容的方法
     */
    public String getConfigHash() {
        return getStableConfigHash();
    }
}
