package net.redstone233.tam.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLanguageProvider extends FabricLanguageProvider {
    public ModEnglishLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        // 标题
        translationBuilder.add("title.tam.config", "TAM Configuration");

        // 分类
        translationBuilder.add("category.tam.general", "General Settings");
        translationBuilder.add("category.tam.display", "Display Settings");
        translationBuilder.add("category.tam.colors", "Color Settings");
        translationBuilder.add("category.tam.buttons", "Button Settings");
        translationBuilder.add("category.tam.icon", "Icon Settings");
        translationBuilder.add("category.tam.background", "Background Settings");
        translationBuilder.add("category.tam.content", "Content Settings");

        // 选项
        translationBuilder.add("option.tam.show_on_world_enter", "Show on World Enter");
        translationBuilder.add("option.tam.debug_mode", "Debug Mode");
        translationBuilder.add("option.tam.main_title", "Main Title Text");
        translationBuilder.add("option.tam.sub_title", "Sub Title Text");
        translationBuilder.add("option.tam.scroll_speed", "Text Scroll Speed");
        translationBuilder.add("option.tam.use_custom_rgb", "Use Custom RGB Colors");
        translationBuilder.add("option.tam.main_title_color", "Main Title Color");
        translationBuilder.add("option.tam.sub_title_color", "Sub Title Color");
        translationBuilder.add("option.tam.content_color", "Content Color");
        translationBuilder.add("option.tam.confirm_button_text", "Confirm Button Text");
        translationBuilder.add("option.tam.submit_button_text", "Submit Button Text");
        translationBuilder.add("option.tam.button_link", "Button Link");
        translationBuilder.add("option.tam.show_icon", "Show Icon");
        translationBuilder.add("option.tam.icon_path", "Icon Path");
        translationBuilder.add("option.tam.icon_width", "Icon Width");
        translationBuilder.add("option.tam.icon_height", "Icon Height");
        translationBuilder.add("option.tam.icon_text_spacing", "Icon Text Spacing");
        translationBuilder.add("option.tam.use_custom_background", "Use Custom Background");
        translationBuilder.add("option.tam.background_path", "Background Path");
        translationBuilder.add("option.tam.announcement_content", "Announcement Content");

        // 工具提示
        translationBuilder.add("tooltip.tam.show_on_world_enter", "Whether to show announcement when player first enters the world");
        translationBuilder.add("tooltip.tam.debug_mode", "Enable debug mode to show UI boundaries and other auxiliary information");
        translationBuilder.add("tooltip.tam.main_title", "The main title text of the announcement");
        translationBuilder.add("tooltip.tam.sub_title", "The sub title text of the announcement");
        translationBuilder.add("tooltip.tam.scroll_speed", "Scroll speed of announcement content (1-10)");
        translationBuilder.add("tooltip.tam.use_custom_rgb", "Whether to use custom RGB colors instead of default colors");
        translationBuilder.add("tooltip.tam.main_title_color", "RGB color value for the main title");
        translationBuilder.add("tooltip.tam.sub_title_color", "RGB color value for the sub title");
        translationBuilder.add("tooltip.tam.content_color", "RGB color value for the announcement content");
        translationBuilder.add("tooltip.tam.confirm_button_text", "Text displayed on the confirm button");
        translationBuilder.add("tooltip.tam.submit_button_text", "Text displayed on the submit button");
        translationBuilder.add("tooltip.tam.button_link", "Link address opened after clicking the button");
        translationBuilder.add("tooltip.tam.show_icon", "Whether to show icon in the announcement");
        translationBuilder.add("tooltip.tam.icon_path", "Path to the icon resource (format: modid:textures/path/to/icon.png)");
        translationBuilder.add("tooltip.tam.icon_width", "Width of the icon (pixels)");
        translationBuilder.add("tooltip.tam.icon_height", "Height of the icon (pixels)");
        translationBuilder.add("tooltip.tam.icon_text_spacing", "Spacing between icon and text (pixels)");
        translationBuilder.add("tooltip.tam.use_custom_background", "Whether to use custom background image");
        translationBuilder.add("tooltip.tam.background_path", "Path to the background image resource");
        translationBuilder.add("tooltip.tam.announcement_content", "Specific content of the announcement, supports multiple lines and color codes");

        // 描述
        translationBuilder.add("description.tam.announcement_content", "One announcement content per line, supports Minecraft color codes (§)");

        // 消息（可选添加，用于未来扩展）
        translationBuilder.add("message.tam.config_saved", "§aAnnouncement Mod configuration saved");
        translationBuilder.add("message.tam.config_reset", "§aConfiguration reset to default values");

        // 按键绑定（如果使用）
        translationBuilder.add("key.tam.open_config", "Open TAM Config");
        translationBuilder.add("category.tam", "TAM");

        // 命令反馈消息
        translationBuilder.add("commands.tam.announcement.show.success", "§aAnnouncement displayed to you");
        translationBuilder.add("commands.tam.announcement.show.all.success", "§aAnnouncement displayed to all online players");
        translationBuilder.add("commands.tam.announcement.reload.success", "§aConfiguration reloaded");
        translationBuilder.add("commands.tam.announcement.reset.success", "§aConfiguration reset to default values");
        translationBuilder.add("commands.tam.announcement.permission.denied", "§cYou need OP permissions to use this command");
        translationBuilder.add("commands.tam.announcement.player.only", "§cThis command can only be executed by players");

        // 调试命令相关
        translationBuilder.add("commands.tam.debug.title.set", "§aMain title set to: %s");
        translationBuilder.add("commands.tam.debug.subtitle.set", "§aSub title set to: %s");
        translationBuilder.add("commands.tam.debug.content.added", "§aAnnouncement content added: %s");
        translationBuilder.add("commands.tam.debug.content.cleared", "§aAnnouncement content cleared");
        translationBuilder.add("commands.tam.debug.buttons.set", "§aButton text set - Confirm: %s, Submit: %s");
        translationBuilder.add("commands.tam.debug.scroll_speed.set", "§aScroll speed set to: %s");
        translationBuilder.add("commands.tam.debug.icon.toggled", "§aIcon display %s");
        translationBuilder.add("commands.tam.debug.background.toggled", "§aCustom background %s");
        translationBuilder.add("commands.tam.debug.icon.enabled", "enabled");
        translationBuilder.add("commands.tam.debug.icon.disabled", "disabled");
        translationBuilder.add("commands.tam.debug.background.enabled", "enabled");
        translationBuilder.add("commands.tam.debug.background.disabled", "disabled");
    }
}