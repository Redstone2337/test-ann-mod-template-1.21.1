// ModEnglishLanguageProvider.java
package net.redstone233.tam.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.redstone233.tam.enchantment.ModEnchantments;
import net.redstone233.tam.item.ModItemGroups;
import net.redstone233.tam.item.ModItems;

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
        translationBuilder.add("category.tam.extend", "Extend Settings");

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
        translationBuilder.add("option.tam.show_ponder", "Display Ponder Screen");
        translationBuilder.add("option.tam.brewing_enabled", "Enable Brewing Recipe");

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
        translationBuilder.add("tooltip.tam.show_ponder", "Whether to display the Ponder screen when the announcement is shown");
        translationBuilder.add("tooltip.tam.brewing_enabled", "Whether to enable the brewing recipe for the TAM item");

        // 描述
        translationBuilder.add("description.tam.announcement_content", "One announcement content per line, supports Minecraft color codes (§)");

        // 消息（可选添加，用于未来扩展）
        translationBuilder.add("message.tam.config_saved", "§aAnnouncement Mod configuration saved");
        translationBuilder.add("message.tam.config_reset", "§aConfiguration reset to default values");

        // 按键绑定（如果使用）
        translationBuilder.add("key.tam.open_config", "Open TAM Config");
        translationBuilder.add("key.tam.use_ability", "Use Ability For Sword");
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

        // Super Furnace - 使用正确的键名格式
        translationBuilder.add("tam.ponder.super_furnace.header", "Super Furnace Construction Guide");
        translationBuilder.add("tam.ponder.super_furnace.text_1", "Super Furnace: A special structure that greatly improves smelting efficiency");
        translationBuilder.add("tam.ponder.super_furnace.text_2", "Build Layer 1: 3x3 stone base");
        translationBuilder.add("tam.ponder.super_furnace.text_3", "Build Layer 2: Note the center position");
        translationBuilder.add("tam.ponder.super_furnace.text_4", "Place a furnace in the center");
        translationBuilder.add("tam.ponder.super_furnace.text_5", "Build Layer 3: 3x3 stone top cover");
        translationBuilder.add("tam.ponder.super_furnace.text_6", "Done! The Super Furnace significantly boosts smelting speed");

        // Super Blast Furnace - 使用正确的键名格式
        translationBuilder.add("tam.ponder.super_blast_furnace.header", "Super Blast Furnace Construction Guide");
        translationBuilder.add("tam.ponder.super_blast_furnace.text_1", "Super Blast Furnace: A special structure for highly efficient ore smelting");
        translationBuilder.add("tam.ponder.super_blast_furnace.text_2", "Layer 1: 3x3 smooth stone base");
        translationBuilder.add("tam.ponder.super_blast_furnace.text_3", "Layer 2: Surround the blast furnace with iron blocks");
        translationBuilder.add("tam.ponder.super_blast_furnace.text_4", "Place a blast furnace in the center");
        translationBuilder.add("tam.ponder.super_blast_furnace.text_5", "Layer 3: 3x3 block of iron top cover");
        translationBuilder.add("tam.ponder.super_blast_furnace.text_6", "Compared to a regular blast furnace, the Super Blast Furnace is far more efficient");

        // Super Smoker - 使用正确的键名格式
        translationBuilder.add("tam.ponder.super_smoker.header", "Super Smoker Construction Guide");
        translationBuilder.add("tam.ponder.super_smoker.text_1", "Super Smoker: A special structure for quickly smoking food");
        translationBuilder.add("tam.ponder.super_smoker.text_2", "Layer 1: Alternate coal blocks and logs in a cross pattern");
        translationBuilder.add("tam.ponder.super_smoker.text_3", "Layer 2: Keep the cross pattern, place a smoker in the center");
        translationBuilder.add("tam.ponder.super_smoker.text_4", "Place a smoker in the center");
        translationBuilder.add("tam.ponder.super_smoker.text_5", "Layer 3: Mirror Layer 1 exactly");
        translationBuilder.add("tam.ponder.super_smoker.text_6", "Done! The Super Smoker greatly increases food smoking speed");

        // New category tags - 使用正确的键名格式
        translationBuilder.add("tam.ponder.tag.super_furnace", "Super Furnace");
        translationBuilder.add("tam.ponder.tag.super_blast_furnace", "Super Blast Furnace");
        translationBuilder.add("tam.ponder.tag.super_smoker", "Super Smoker");
        translationBuilder.add("tam.ponder.tag.furnaces", "Furnaces");
        translationBuilder.add("tam.ponder.tag.furnaces.description", "Construction guides for high-efficiency smelting setups");

        translationBuilder.add(ModItems.ICE_FREEZE_SWORD, "Ice Freeze Sword");
        translationBuilder.add(ModItems.BLAZING_FLAME_SWORD, "Blazing Flame Sword");
        translationBuilder.add(ModItems.GUIDITE_HELMET, "Guidite Helmet");
        translationBuilder.add(ModItems.GUIDITE_CHESTPLATE, "Guidite Chestplate");
        translationBuilder.add(ModItems.GUIDITE_LEGGINGS, "Guidite Leggings");
        translationBuilder.add(ModItems.GUIDITE_BOOTS, "Guidite Boots");
        translationBuilder.add(ModItems.SUSPICIOUS_SUBSTANCE, "Suspicious Substance");

        translationBuilder.addEnchantment(ModEnchantments.VAMPIRISM, "Vampirism");
        translationBuilder.addEnchantment(ModEnchantments.FIRE_BLESSING, "Fire Blessing");
        translationBuilder.addEnchantment(ModEnchantments.FROST_BLESSING, "Ice Blessing");

        translationBuilder.add(ModItemGroups.MOD_ITEMS, "Test Mod | Customization Items");
        translationBuilder.add(ModItemGroups.MOD_WEAPONS, "Test Mod | Customization Weapons");
        translationBuilder.add(ModItemGroups.MOD_ARMOR, "Test Mod | Customization Armors");

        translationBuilder.add("tooltip.ability_sword.display1","Hold [ ");
        translationBuilder.add("key.use_ability.item","%s");
        translationBuilder.add("tooltip.ability_sword.display2"," ] for Ability to use");
    }
}