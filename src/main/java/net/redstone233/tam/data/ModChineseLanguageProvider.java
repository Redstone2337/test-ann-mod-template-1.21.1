package net.redstone233.tam.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModChineseLanguageProvider extends FabricLanguageProvider {
    public ModChineseLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "zh_cn",registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        // 标题
        translationBuilder.add("title.tam.config", "公告模组配置");

        // 分类
        translationBuilder.add("category.tam.general", "通用设置");
        translationBuilder.add("category.tam.display", "显示设置");
        translationBuilder.add("category.tam.colors", "颜色设置");
        translationBuilder.add("category.tam.buttons", "按钮设置");
        translationBuilder.add("category.tam.icon", "图标设置");
        translationBuilder.add("category.tam.background", "背景设置");
        translationBuilder.add("category.tam.content", "内容设置");

        // 选项
        translationBuilder.add("option.tam.show_on_world_enter", "进入世界时显示公告");
        translationBuilder.add("option.tam.debug_mode", "调试模式");
        translationBuilder.add("option.tam.main_title", "主标题文本");
        translationBuilder.add("option.tam.sub_title", "副标题文本");
        translationBuilder.add("option.tam.scroll_speed", "文本滚动速度");
        translationBuilder.add("option.tam.use_custom_rgb", "使用自定义RGB颜色");
        translationBuilder.add("option.tam.main_title_color", "主标题颜色");
        translationBuilder.add("option.tam.sub_title_color", "副标题颜色");
        translationBuilder.add("option.tam.content_color", "内容颜色");
        translationBuilder.add("option.tam.confirm_button_text", "确定按钮文本");
        translationBuilder.add("option.tam.submit_button_text", "提交按钮文本");
        translationBuilder.add("option.tam.button_link", "按钮链接");
        translationBuilder.add("option.tam.show_icon", "显示图标");
        translationBuilder.add("option.tam.icon_path", "图标路径");
        translationBuilder.add("option.tam.icon_width", "图标宽度");
        translationBuilder.add("option.tam.icon_height", "图标高度");
        translationBuilder.add("option.tam.icon_text_spacing", "图标文本间距");
        translationBuilder.add("option.tam.use_custom_background", "使用自定义背景");
        translationBuilder.add("option.tam.background_path", "背景路径");
        translationBuilder.add("option.tam.announcement_content", "公告内容");

        // 工具提示
        translationBuilder.add("tooltip.tam.show_on_world_enter", "是否在玩家首次进入世界时显示公告");
        translationBuilder.add("tooltip.tam.debug_mode", "启用调试模式，显示UI边界等辅助信息");
        translationBuilder.add("tooltip.tam.main_title", "公告的主标题文本");
        translationBuilder.add("tooltip.tam.sub_title", "公告的副标题文本");
        translationBuilder.add("tooltip.tam.scroll_speed", "公告内容的滚动速度 (1-10)");
        translationBuilder.add("tooltip.tam.use_custom_rgb", "是否使用自定义RGB颜色代替默认颜色");
        translationBuilder.add("tooltip.tam.main_title_color", "主标题的RGB颜色值");
        translationBuilder.add("tooltip.tam.sub_title_color", "副标题的RGB颜色值");
        translationBuilder.add("tooltip.tam.content_color", "公告内容的RGB颜色值");
        translationBuilder.add("tooltip.tam.confirm_button_text", "确定按钮显示的文本");
        translationBuilder.add("tooltip.tam.submit_button_text", "提交按钮显示的文本");
        translationBuilder.add("tooltip.tam.button_link", "按钮点击后打开的链接地址");
        translationBuilder.add("tooltip.tam.show_icon", "是否在公告中显示图标");
        translationBuilder.add("tooltip.tam.icon_path", "图标资源的路径 (格式: modid:textures/path/to/icon.png)");
        translationBuilder.add("tooltip.tam.icon_width", "图标的宽度 (像素)");
        translationBuilder.add("tooltip.tam.icon_height", "图标的高度 (像素)");
        translationBuilder.add("tooltip.tam.icon_text_spacing", "图标与文本之间的间距 (像素)");
        translationBuilder.add("tooltip.tam.use_custom_background", "是否使用自定义背景图片");
        translationBuilder.add("tooltip.tam.background_path", "背景图片资源的路径");
        translationBuilder.add("tooltip.tam.announcement_content", "公告的具体内容，支持多行和颜色代码");

        // 描述
        translationBuilder.add("description.tam.announcement_content", "每行一条公告内容，支持Minecraft颜色代码 (§)");

        // 消息
        translationBuilder.add("message.tam.config_saved", "§a公告模组配置已保存");
        translationBuilder.add("message.tam.config_reset", "§a配置已重置为默认值");

        // 键位绑定
        translationBuilder.add("key.tam.open_config", "打开公告模组配置界面");
        translationBuilder.add("category.tam", "TAM模组");
    }
}