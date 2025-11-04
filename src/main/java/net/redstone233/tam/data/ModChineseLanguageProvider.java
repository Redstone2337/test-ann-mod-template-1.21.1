// ModChineseLanguageProvider.java
package net.redstone233.tam.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.redstone233.tam.enchantment.ModEnchantments;
import net.redstone233.tam.item.ModItemGroups;
import net.redstone233.tam.item.ModItems;

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
        translationBuilder.add("category.tam.extend", "扩展设置");

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
        translationBuilder.add("option.tam.show_ponder", "显示思索界面");
        translationBuilder.add("option.tam.brewing_enabled", "启用酿造配方");

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
        translationBuilder.add("tooltip.tam.show_ponder", "是否为玩家显示思索界面");
        translationBuilder.add("tooltip.tam.brewing_enabled", "是否启用酿造配方");

        // 描述
        translationBuilder.add("description.tam.announcement_content", "每行一条公告内容，支持Minecraft颜色代码 (§)");

        // 消息
        translationBuilder.add("message.tam.config_saved", "§a公告模组配置已保存");
        translationBuilder.add("message.tam.config_reset", "§a配置已重置为默认值");

        // 键位绑定
        translationBuilder.add("key.tam.open_config", "打开公告模组配置界面");
        translationBuilder.add("key.tam.use_ability", "使用武器技能");
        translationBuilder.add("category.tam", "TAM模组");

        // 超级熔炉 - 使用正确的键名格式
        translationBuilder.add("tam.ponder.super_furnace.header", "超级熔炉建造指南");
        translationBuilder.add("tam.ponder.super_furnace.text_1", "超级熔炉：大幅提升熔炼效率的特殊结构");
        translationBuilder.add("tam.ponder.super_furnace.text_2", "开始建造第1层：3x3石头基底");
        translationBuilder.add("tam.ponder.super_furnace.text_3", "建造第2层：注意中间位置");
        translationBuilder.add("tam.ponder.super_furnace.text_4", "中间位置放置熔炉");
        translationBuilder.add("tam.ponder.super_furnace.text_5", "建造第3层：3x3石头顶盖");
        translationBuilder.add("tam.ponder.super_furnace.text_6", "完成！超级熔炉可以大幅提升熔炼速度");

        // 超级高炉 - 使用正确的键名格式
        translationBuilder.add("tam.ponder.super_blast_furnace.header", "超级高炉建造指南");
        translationBuilder.add("tam.ponder.super_blast_furnace.text_1", "超级高炉：高效冶炼矿石的特殊结构");
        translationBuilder.add("tam.ponder.super_blast_furnace.text_2", "第1层：3x3平滑石基底");
        translationBuilder.add("tam.ponder.super_blast_furnace.text_3", "第2层：铁块包围高炉");
        translationBuilder.add("tam.ponder.super_blast_furnace.text_4", "中心放置高炉");
        translationBuilder.add("tam.ponder.super_blast_furnace.text_5", "第3层：3x3铁块顶盖");
        translationBuilder.add("tam.ponder.super_blast_furnace.text_6", "与普通高炉对比，超级高炉效率更高");

        // 超级烟熏炉 - 使用正确的键名格式
        translationBuilder.add("tam.ponder.super_smoker.header", "超级烟熏炉建造指南");
        translationBuilder.add("tam.ponder.super_smoker.text_1", "超级烟熏炉：快速熏制食物的特殊结构");
        translationBuilder.add("tam.ponder.super_smoker.text_2", "第1层：煤炭块与木头十字交错排列");
        translationBuilder.add("tam.ponder.super_smoker.text_3", "第2层：保持十字结构，中心放置烟熏炉");
        translationBuilder.add("tam.ponder.super_smoker.text_4", "中心放置烟熏炉");
        translationBuilder.add("tam.ponder.super_smoker.text_5", "第3层：与第1层完全对称");
        translationBuilder.add("tam.ponder.super_smoker.text_6", "完成！超级烟熏炉大幅提升食物熏制速度");

        // 新增分类标签
        translationBuilder.add("tam.ponder.tag.super_furnace", "超级熔炉");
        translationBuilder.add("tam.ponder.tag.super_blast_furnace", "超级高炉");
        translationBuilder.add("tam.ponder.tag.super_smoker", "超级烟熏炉");
        translationBuilder.add("tam.ponder.tag.furnaces", "熔炉类");
        translationBuilder.add("tam.ponder.tag.furnaces.description", "高效熔炼设备的建造指南");

        translationBuilder.add(ModItemGroups.MOD_ITEMS, "测试模组 | 自定义物品");
        translationBuilder.add(ModItemGroups.MOD_WEAPONS, "测试模组 | 自定义武器");
        translationBuilder.add(ModItemGroups.MOD_ARMOR, "测试模组 | 自定义盔甲");

        translationBuilder.add("tooltip.ability_sword.display1","按住 [ ");
        translationBuilder.add("key.use_ability.item","%s");
        translationBuilder.add("tooltip.ability_sword.display2"," ] 使用武器技能");

        translationBuilder.add(ModItems.BLAZING_FLAME_SWORD, "§c§l火焰之剑");
        translationBuilder.add(ModItems.ICE_FREEZE_SWORD, "§b§l冰霜之剑");
        translationBuilder.add(ModItems.GUIDITE_HELMET, "§e§l可疑头盔");
        translationBuilder.add(ModItems.GUIDITE_CHESTPLATE, "§e§l可疑胸甲");
        translationBuilder.add(ModItems.GUIDITE_LEGGINGS, "§e§l可疑护腿");
        translationBuilder.add(ModItems.GUIDITE_BOOTS, "§e§l可疑靴子");
        translationBuilder.add(ModItems.SUSPICIOUS_SUBSTANCE, "§c§l可疑物质");

        translationBuilder.addEnchantment(ModEnchantments.VAMPIRISM, "§c§l吸血");
        translationBuilder.addEnchantment(ModEnchantments.FIRE_BLESSING, "§c§l火焰祝福");
        translationBuilder.addEnchantment(ModEnchantments.FROST_BLESSING, "§b§l冰霜祝福");
    }
}