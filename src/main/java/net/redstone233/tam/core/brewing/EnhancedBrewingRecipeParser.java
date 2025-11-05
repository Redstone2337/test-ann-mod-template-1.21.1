package net.redstone233.tam.core.brewing;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.type.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * 增强的酿造配方解析器 - 支持两种格式
 * 1. 传统格式: BrewingRecipeEvent.create(e => { ... })
 * 2. 简化格式: brew({ ... })
 * 使用 Fabric API 注册酿造配方
 */
public class EnhancedBrewingRecipeParser {
    private static final Logger LOGGER = LoggerFactory.getLogger("EnhancedBrewingRecipeParser");

    private final String modId;
    private final Path configDir;
    private boolean initialized = false;
    private int registeredRecipeCount = 0;

    public EnhancedBrewingRecipeParser(String modId) {
        this.modId = modId;
        this.configDir = Path.of(FabricLoader.getInstance().getGameDir() + "/config/" + modId + "/brewing_recipes");
    }

    /**
     * 初始化解析器
     */
    public void initialize() {
        if (initialized) {
            return;
        }

        LOGGER.info("初始化增强酿造配方解析器 for mod: {}", modId);
        CustomPotionRegistry.initialize();
        initialized = true;
        LOGGER.info("增强酿造配方解析器初始化完成");
    }

    /**
     * 加载所有酿造配方
     */
    public void loadBrewingRecipes() {
        if (!initialized) {
            initialize();
        }

        registeredRecipeCount = 0;

        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
                createDualFormatExampleFile();
                LOGGER.info("创建酿造配方目录: {}", configDir);
                return;
            }

            FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(configDir, "*.js")) {
                    for (Path file : stream) {
                        parseDualFormatRecipeFile(file, builder);
                    }
                } catch (IOException e) {
                    LOGGER.error("读取酿造配方文件时出错: {}", e.getMessage());
                }
            });

            LOGGER.info("为模组 {} 加载了 {} 个酿造配方", modId, registeredRecipeCount);

        } catch (Exception e) {
            LOGGER.error("加载酿造配方时出错: {}", e.getMessage());
        }
    }

    private void createDualFormatExampleFile() throws IOException {
        Path exampleFile = configDir.resolve("example_recipes.js");
        String exampleContent =
                "// 双重格式酿造配方文件 - 模组: " + modId + "\n" +
                        "// 支持两种编写方式：传统格式和简化格式\n\n" +

                        "// ===== 传统格式 (BrewingRecipeEvent.create) =====\n" +
                        "// 幸运药水配方\n" +
                        "BrewingRecipeEvent.create(e => {\n" +
                        "    let {input, material, output} = e;\n" +
                        "    input.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.AWKWARD)\n" +
                        "        .toCreateBrewing();\n" +
                        "    material.add(Items.EMERALD).toCreateBrewing();\n" +
                        "    output.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, 'tam:luck_potion')\n" +
                        "        .toCreateBrewing();\n" +
                        "})\n\n" +

                        "// 力量药水 II 配方\n" +
                        "BrewingRecipeEvent.create(e => {\n" +
                        "    let {input, material, output} = e;\n" +
                        "    input.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.STRENGTH)\n" +
                        "        .toCreateBrewing();\n" +
                        "    material.add(Items.GLOWSTONE_DUST).toCreateBrewing();\n" +
                        "    output.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, 'tam:strength_potion_ii')\n" +
                        "        .toCreateBrewing();\n" +
                        "})\n\n" +

                        "// ===== 简化格式 (brew) =====\n" +
                        "// 抗性药水配方\n" +
                        "brew({\n" +
                        "  input: {item: 'potion', potion: 'awkward'},\n" +
                        "  material: 'obsidian',\n" +
                        "  output: {item: 'potion', potion: 'tam:resistance_potion'}\n" +
                        "})\n\n" +

                        "// 喷溅型药水示例\n" +
                        "brew({\n" +
                        "  input: {item: 'potion', potion: 'strength'},\n" +
                        "  material: 'gunpowder',\n" +
                        "  output: {item: 'splash_potion', potion: 'strength'}\n" +
                        "})\n\n" +

                        "// 延长版药水示例\n" +
                        "brew({\n" +
                        "  input: {item: 'potion', potion: 'regeneration'},\n" +
                        "  material: 'redstone',\n" +
                        "  output: {item: 'potion', potion: 'long_regeneration'}\n" +
                        "})\n\n" +

                        "// 混合使用两种格式也是可以的！";

        Files.writeString(exampleFile, exampleContent);
        LOGGER.info("已创建双重格式示例文件: {}", exampleFile);
    }

    /**
     * 解析双重格式的配方文件
     */
    private void parseDualFormatRecipeFile(Path file, FabricBrewingRecipeRegistryBuilder builder) {
        try {
            String content = Files.readString(file);
            LOGGER.info("解析双重格式酿造配方文件: {}", file.getFileName());

            int recipeCount = 0;

            // 先解析传统格式
            Pattern legacyPattern = Pattern.compile("BrewingRecipeEvent\\.create\\(e\\s*=>\\s*\\{([^}]+)}", Pattern.DOTALL);
            Matcher legacyMatcher = legacyPattern.matcher(content);

            while (legacyMatcher.find()) {
                recipeCount++;
                String recipeContent = legacyMatcher.group(1);
                parseLegacyRecipe(recipeContent, file.getFileName().toString(), recipeCount, builder);
            }

            // 再解析简化格式
            Pattern simplifiedPattern = Pattern.compile("brew\\(\\s*\\{([^}]+)}\\s*\\)", Pattern.DOTALL);
            Matcher simplifiedMatcher = simplifiedPattern.matcher(content);

            while (simplifiedMatcher.find()) {
                recipeCount++;
                String recipeContent = simplifiedMatcher.group(1);
                parseSimplifiedRecipe(recipeContent, file.getFileName().toString(), recipeCount, builder);
            }

            LOGGER.info("在文件 {} 中找到 {} 个配方", file.getFileName(), recipeCount);

        } catch (IOException e) {
            LOGGER.error("读取文件 {} 时出错: {}", file.getFileName(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error("解析文件 {} 时出错: {}", file.getFileName(), e.getMessage(), e);
        }
    }

    /**
     * 解析传统格式的单个配方
     */
    private void parseLegacyRecipe(String recipeContent, String fileName, int recipeNumber, FabricBrewingRecipeRegistryBuilder builder) {
        try {
            LegacyBrewingRecipe recipe = new LegacyBrewingRecipe();

            // 解析输入物品
            parseLegacyItemSection(recipeContent, "input", recipe.input);

            // 解析材料物品
            parseLegacyMaterialSection(recipeContent, recipe);

            // 解析输出物品
            parseLegacyItemSection(recipeContent, "output", recipe.output);

            if (recipe.isValid()) {
                registerLegacyBrewingRecipe(recipe, fileName, recipeNumber, builder);
            } else {
                LOGGER.warn("文件 {} 中的传统格式配方 #{} 不完整", fileName, recipeNumber);
            }

        } catch (Exception e) {
            LOGGER.error("解析文件 {} 中的传统格式配方 #{} 时出错: {}", fileName, recipeNumber, e.getMessage());
        }
    }

    /**
     * 解析传统格式的物品部分
     */
    private void parseLegacyItemSection(String content, String prefix, LegacyItemData itemData) {
        // 匹配 input.add(...).component(...).toCreateBrewing()
        Pattern pattern = Pattern.compile(
                prefix + "\\.add\\(([^)]+)\\)" +
                        "(.*?)\\.toCreateBrewing\\(\\)",
                Pattern.DOTALL
        );
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String itemStr = matcher.group(1);
            itemData.itemId = parseLegacyItemString(itemStr);

            String componentsStr = matcher.group(2);
            parseLegacyComponents(componentsStr, itemData);
        }
    }

    /**
     * 解析传统格式的材料部分
     */
    private void parseLegacyMaterialSection(String content, LegacyBrewingRecipe recipe) {
        Pattern materialPattern = Pattern.compile("material\\.add\\(([^)]+)\\)\\.toCreateBrewing\\(\\)");
        Matcher materialMatcher = materialPattern.matcher(content);

        if (materialMatcher.find()) {
            String itemStr = materialMatcher.group(1);
            recipe.materialItem = parseLegacyItemString(itemStr);
        }
    }

    /**
     * 解析传统格式的组件
     */
    private void parseLegacyComponents(String componentsStr, LegacyItemData itemData) {
        if (componentsStr == null || componentsStr.trim().isEmpty()) {
            return;
        }

        // 匹配 .component(ComponentType, value)
        Pattern componentPattern = Pattern.compile("\\.component\\(([^,]+),\\s*([^)]+)\\)", Pattern.DOTALL);
        Matcher componentMatcher = componentPattern.matcher(componentsStr);

        while (componentMatcher.find()) {
            String componentType = componentMatcher.group(1).trim();
            String componentValue = componentMatcher.group(2).trim();

            if ("DataComponentTypes.POTION_CONTENTS".equals(componentType)) {
                // 处理药水内容组件
                itemData.potionValue = componentValue;
            } else {
                // 其他组件存储到映射中
                itemData.components.put(componentType, componentValue);
            }
        }
    }

    /**
     * 解析传统格式的物品字符串
     */
    private String parseLegacyItemString(String itemString) {
        itemString = itemString.trim();

        if (itemString.startsWith("Items.")) {
            return itemString.substring(6).toLowerCase();
        }

        if (itemString.startsWith("\"") && itemString.endsWith("\"")) {
            return itemString.substring(1, itemString.length() - 1);
        }

        if (itemString.startsWith("'") && itemString.endsWith("'")) {
            return itemString.substring(1, itemString.length() - 1);
        }

        return itemString;
    }

    /**
     * 注册传统格式的酿造配方
     */
    private void registerLegacyBrewingRecipe(LegacyBrewingRecipe recipe, String fileName, int recipeNumber, FabricBrewingRecipeRegistryBuilder builder) {
        try {
            Item inputItem = getItemById(recipe.input.itemId);
            Item materialItem = getItemById(recipe.materialItem);
            Item outputItem = getItemById(recipe.output.itemId);

            if (inputItem != null && materialItem != null && outputItem != null) {
                RegistryEntry<Potion> inputPotion = recipe.input.potionValue != null ?
                        CustomPotionRegistry.getPotion(recipe.input.potionValue) : null;
                RegistryEntry<Potion> outputPotion = recipe.output.potionValue != null ?
                        CustomPotionRegistry.getPotion(recipe.output.potionValue) : null;

                Ingredient ingredient = Ingredient.ofItems(materialItem);

                if (isPotionConversionRecipe(inputItem, outputItem, inputPotion, outputPotion)) {
                    registerPotionConversionRecipe(builder, inputPotion, ingredient, outputPotion);
                } else if (isItemConversionRecipe(inputItem, outputItem)) {
                    registerItemConversionRecipe(builder, inputItem, ingredient, outputItem);
                } else {
                    LOGGER.warn("无法识别的传统配方类型: {} -> {}", recipe.input.itemId, recipe.output.itemId);
                    return;
                }

                registeredRecipeCount++;
                LOGGER.info("已注册传统酿造配方 #{}: {} + {} -> {} (来自 {})",
                        recipeNumber, recipe.input.itemId, recipe.materialItem, recipe.output.itemId, fileName);
            } else {
               legendaryRecipe(recipe);
            }

        } catch (Exception e) {
            LOGGER.error("注册传统酿造配方 #{} 时出错: {}", recipeNumber, e.getMessage(), e);
        }
    }

    /**
     * 解析简化格式的单个配方
     */
    private void parseSimplifiedRecipe(String recipeContent, String fileName, int recipeNumber, FabricBrewingRecipeRegistryBuilder builder) {
        try {
            SimplifiedBrewingRecipe recipe = new SimplifiedBrewingRecipe();

            // 解析输入
            parseSimplifiedItem(recipeContent, "input", recipe.input);

            // 解析材料
            parseSimplifiedMaterial(recipeContent, recipe);

            // 解析输出
            parseSimplifiedItem(recipeContent, "output", recipe.output);

            if (recipe.isValid()) {
                registerSimplifiedBrewingRecipe(recipe, fileName, recipeNumber, builder);
            } else {
                LOGGER.warn("文件 {} 中的简化格式配方 #{} 不完整", fileName, recipeNumber);
            }

        } catch (Exception e) {
            LOGGER.error("解析文件 {} 中的简化格式配方 #{} 时出错: {}", fileName, recipeNumber, e.getMessage());
        }
    }

    /**
     * 解析简化的物品部分
     */
    private void parseSimplifiedItem(String content, String fieldName, SimplifiedItemData itemData) {
        // 匹配 input: {item: 'potion', potion: 'awkward'}
        Pattern pattern = Pattern.compile(
                fieldName + ":\\s*\\{([^}]+)}",
                Pattern.DOTALL
        );
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String itemContent = matcher.group(1);

            // 解析 item 字段
            Pattern itemPattern = Pattern.compile("item:\\s*['\"]([^'\"]+)['\"]");
            Matcher itemMatcher = itemPattern.matcher(itemContent);
            if (itemMatcher.find()) {
                itemData.itemId = parseSimplifiedItemString(itemMatcher.group(1));
            }

            // 解析 potion 字段
            Pattern potionPattern = Pattern.compile("potion:\\s*['\"]([^'\"]+)['\"]");
            Matcher potionMatcher = potionPattern.matcher(itemContent);
            if (potionMatcher.find()) {
                itemData.potionValue = potionMatcher.group(1);
            }
        } else {
            // 尝试匹配简单字符串格式: input: 'potion'
            Pattern simplePattern = Pattern.compile(fieldName + ":\\s*['\"]([^'\"]+)['\"]");
            Matcher simpleMatcher = simplePattern.matcher(content);
            if (simpleMatcher.find()) {
                itemData.itemId = parseSimplifiedItemString(simpleMatcher.group(1));
            }
        }
    }

    /**
     * 解析简化格式的材料部分
     */
    private void parseSimplifiedMaterial(String content, SimplifiedBrewingRecipe recipe) {
        Pattern materialPattern = Pattern.compile("material:\\s*['\"]([^'\"]+)['\"]");
        Matcher materialMatcher = materialPattern.matcher(content);

        if (materialMatcher.find()) {
            recipe.materialItem = parseSimplifiedItemString(materialMatcher.group(1));
        }
    }

    /**
     * 解析简化格式的物品字符串
     */
    private String parseSimplifiedItemString(String itemString) {
        itemString = itemString.trim().toLowerCase();

        // 处理常见的物品名称映射
        return switch (itemString) {
            case "potion" -> "potion";
            case "splash_potion" -> "splash_potion";
            case "lingering_potion" -> "lingering_potion";
            case "gunpowder" -> "gunpowder";
            case "dragon_breath" -> "dragon_breath";
            case "emerald" -> "emerald";
            case "glowstone_dust" -> "glowstone_dust";
            case "redstone" -> "redstone";
            case "fermented_spider_eye" -> "fermented_spider_eye";
            case "nether_wart" -> "nether_wart";
            case "obsidian" -> "obsidian";
            default -> itemString;
        };
    }

    /**
     * 注册简化格式的酿造配方
     */
    private void registerSimplifiedBrewingRecipe(SimplifiedBrewingRecipe recipe, String fileName, int recipeNumber, FabricBrewingRecipeRegistryBuilder builder) {
        try {
            Item inputItem = getItemById(recipe.input.itemId);
            Item materialItem = getItemById(recipe.materialItem);
            Item outputItem = getItemById(recipe.output.itemId);

            if (inputItem != null && materialItem != null && outputItem != null) {
                RegistryEntry<Potion> inputPotion = recipe.input.potionValue != null ?
                        CustomPotionRegistry.getPotion(recipe.input.potionValue) : null;
                RegistryEntry<Potion> outputPotion = recipe.output.potionValue != null ?
                        CustomPotionRegistry.getPotion(recipe.output.potionValue) : null;

                Ingredient ingredient = Ingredient.ofItems(materialItem);

                if (isPotionConversionRecipe(inputItem, outputItem, inputPotion, outputPotion)) {
                    registerPotionConversionRecipe(builder, inputPotion, ingredient, outputPotion);
                } else if (isItemConversionRecipe(inputItem, outputItem)) {
                    registerItemConversionRecipe(builder, inputItem, ingredient, outputItem);
                } else {
                    LOGGER.warn("无法识别的简化配方类型: {} -> {}", recipe.input.itemId, recipe.output.itemId);
                    return;
                }

                registeredRecipeCount++;
                LOGGER.info("已注册简化酿造配方 #{}: {} + {} -> {} (来自 {})",
                        recipeNumber, recipe.input.itemId, recipe.materialItem, recipe.output.itemId, fileName);
            } else {
                simplifiedRecipe(recipe);
            }

        } catch (Exception e) {
            LOGGER.error("注册简化酿造配方 #{} 时出错: {}", recipeNumber, e.getMessage(), e);
        }
    }

    private static void simplifiedRecipe(SimplifiedBrewingRecipe recipe) {
        LOGGER.warn("无法找到物品: input={}, material={}, output={}",
                recipe.input.itemId, recipe.materialItem, recipe.output.itemId);
    }

    private static void legendaryRecipe(LegacyBrewingRecipe recipe) {
        TestAnnMod.LOGGER.warn("无法找到物品: input={}, material={}, output={}",
                recipe.input.itemId, recipe.materialItem, recipe.output.itemId);
    }



    /**
     * 根据ID获取物品
     */
    private Item getItemById(String itemId) {
        try {
            // 首先尝试原版物品
            if (itemId.contains(":")) {
                return Registries.ITEM.get(Identifier.of(itemId));
            } else {
                // 没有命名空间的默认为原版
                return Registries.ITEM.get(Identifier.of("minecraft", itemId));
            }
        } catch (Exception e) {
            LOGGER.warn("无法获取物品: {}", itemId);
            return null;
        }
    }

    // 配方类型判断方法
    private boolean isPotionConversionRecipe(Item inputItem, Item outputItem, RegistryEntry<Potion> inputPotion, RegistryEntry<Potion> outputPotion) {
        return inputItem == outputItem && inputPotion != null && outputPotion != null;
    }

    private boolean isItemConversionRecipe(Item inputItem, Item outputItem) {
        return inputItem != outputItem && isPotionItem(inputItem) && isPotionItem(outputItem);
    }

    private boolean isPotionItem(Item item) {
        return item == Items.POTION || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION;
    }

    // 配方注册方法
    private void registerPotionConversionRecipe(FabricBrewingRecipeRegistryBuilder builder,
                                                RegistryEntry<Potion> inputPotion,
                                                Ingredient ingredient,
                                                RegistryEntry<Potion> outputPotion) {
        try {
            builder.registerPotionRecipe(inputPotion, ingredient, outputPotion);
            LOGGER.debug("注册药水效果配方: {} + {} -> {}",
                    getPotionName(inputPotion), getIngredientName(ingredient), getPotionName(outputPotion));
        } catch (Exception e) {
            LOGGER.error("注册药水效果配方时出错: {}", e.getMessage());
        }
    }

    private void registerItemConversionRecipe(FabricBrewingRecipeRegistryBuilder builder,
                                              Item inputItem,
                                              Ingredient ingredient,
                                              Item outputItem) {
        try {
            builder.registerItemRecipe(inputItem, ingredient, outputItem);
            LOGGER.debug("注册物品转换配方: {} + {} -> {}",
                    Registries.ITEM.getId(inputItem), getIngredientName(ingredient), Registries.ITEM.getId(outputItem));
        } catch (Exception e) {
            LOGGER.error("注册物品转换配方时出错: {}", e.getMessage());
        }
    }

    // 辅助方法
    private String getPotionName(RegistryEntry<Potion> potion) {
        return potion.getKey().map(key -> key.getValue().toString()).orElse("unknown");
    }

    private String getIngredientName(Ingredient ingredient) {
        ItemStack[] stacks = ingredient.getMatchingStacks();
        if (stacks.length > 0) {
            return Registries.ITEM.getId(stacks[0].getItem()).toString();
        }
        return "unknown_ingredient";
    }

    public int getRecipeCount() {
        return registeredRecipeCount;
    }

    public void reloadBrewingRecipes() {
        LOGGER.info("重新加载酿造配方 for mod: {}", modId);
        loadBrewingRecipes();
    }

    // 传统格式的数据结构
    private static class LegacyBrewingRecipe {
        LegacyItemData input = new LegacyItemData();
        String materialItem;
        LegacyItemData output = new LegacyItemData();

        boolean isValid() {
            return input.itemId != null && materialItem != null && output.itemId != null;
        }
    }

    private static class LegacyItemData {
        String itemId;
        String potionValue;
        Map<String, String> components = new HashMap<>();
    }

    // 简化格式的数据结构
    private static class SimplifiedBrewingRecipe {
        SimplifiedItemData input = new SimplifiedItemData();
        String materialItem;
        SimplifiedItemData output = new SimplifiedItemData();

        boolean isValid() {
            return input.itemId != null && materialItem != null && output.itemId != null;
        }
    }

    private static class SimplifiedItemData {
        String itemId;
        String potionValue;
    }
}