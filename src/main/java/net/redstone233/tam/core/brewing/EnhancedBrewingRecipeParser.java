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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * 增强的酿造配方解析器
 * 使用 Fabric API 注册酿造配方
 */
public class EnhancedBrewingRecipeParser {
    private static final Logger LOGGER = LoggerFactory.getLogger("EnhancedBrewingRecipeParser");

    private final String modId;
    private final Path configDir;
    private boolean initialized = false;
    private int registeredRecipeCount = 0;

    /**
     * 构造函数
     */
    public EnhancedBrewingRecipeParser(String modId) {
        this.modId = modId;
        this.configDir = Path.of(FabricLoader.getInstance().getGameDir() + modId + "scripts/brewing_recipes");
    }

    /**
     * 初始化解析器
     */
    public void initialize() {
        if (initialized) {
            return;
        }

        LOGGER.info("初始化增强酿造配方解析器 for mod: {}", modId);

        // 初始化自定义药水
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

        // 重置计数器
        registeredRecipeCount = 0;

        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
                createExampleRecipeFile();
                LOGGER.info("创建酿造配方目录: {}", configDir);
                return;
            }

            // 使用 Fabric API 注册配方
            FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(configDir, "*.js")) {
                    for (Path file : stream) {
                        parseBrewingRecipeFile(file, builder);
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

    /**
     * 重新加载酿造配方
     */
    public void reloadBrewingRecipes() {
        LOGGER.info("重新加载酿造配方 for mod: {}", modId);
        loadBrewingRecipes();
    }

    private void createExampleRecipeFile() throws IOException {
        Path exampleFile = configDir.resolve("example_recipes.js");
        String exampleContent =
                "// 示例酿造配方文件 - 模组: " + modId + "\n" +
                        "// 使用 Fabric API 注册的酿造配方\n\n" +

                        "// 基础药水配方 - 水瓶 + 下界疣 -> 笨拙药水\n" +
                        "BrewingRecipeEvent.create(e => {\n" +
                        "    let {input, material, output} = e;\n" +
                        "    input.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.WATER)\n" +
                        "        .toCreateBrewing();\n" +
                        "    material.add(Items.NETHER_WART).toCreateBrewing();\n" +
                        "    output.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.AWKWARD)\n" +
                        "        .toCreateBrewing();\n" +
                        "})\n\n" +

                        "// 自定义药水配方 - 笨拙药水 + 绿宝石 -> 幸运药水\n" +
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

                        "// 喷溅型药水配方 - 力量药水 + 火药 -> 喷溅型力量药水\n" +
                        "BrewingRecipeEvent.create(e => {\n" +
                        "    let {input, material, output} = e;\n" +
                        "    input.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.STRENGTH)\n" +
                        "        .toCreateBrewing();\n" +
                        "    material.add(Items.GUNPOWDER).toCreateBrewing();\n" +
                        "    output.add(Items.SPLASH_POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.STRENGTH)\n" +
                        "        .toCreateBrewing();\n" +
                        "})\n\n" +

                        "// 延长版药水配方 - 再生药水 + 红石 -> 延长版再生药水\n" +
                        "BrewingRecipeEvent.create(e => {\n" +
                        "    let {input, material, output} = e;\n" +
                        "    input.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.REGENERATION)\n" +
                        "        .toCreateBrewing();\n" +
                        "    material.add(Items.REDSTONE).toCreateBrewing();\n" +
                        "    output.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, 'tam:long_regeneration_potion')\n" +
                        "        .toCreateBrewing();\n" +
                        "})";

        Files.writeString(exampleFile, exampleContent);
        LOGGER.info("已创建示例配方文件: {}", exampleFile);
    }

    private void parseBrewingRecipeFile(Path file, FabricBrewingRecipeRegistryBuilder builder) {
        try {
            String content = Files.readString(file);
            LOGGER.info("解析酿造配方文件: {}", file.getFileName());

            // 查找所有配方块
            Pattern recipePattern = Pattern.compile("BrewingRecipeEvent\\.create\\(e\\s*=>\\s*\\{([^}]+)}", Pattern.DOTALL);
            Matcher recipeMatcher = recipePattern.matcher(content);

            int recipeCount = 0;
            while (recipeMatcher.find()) {
                recipeCount++;
                String recipeContent = recipeMatcher.group(1);
                parseSingleRecipe(recipeContent, file.getFileName().toString(), recipeCount, builder);
            }

            LOGGER.info("在文件 {} 中找到 {} 个配方", file.getFileName(), recipeCount);

        } catch (IOException e) {
            LOGGER.error("读取文件 {} 时出错: {}", file.getFileName(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error("解析文件 {} 时出错: {}", file.getFileName(), e.getMessage(), e);
        }
    }

    private void parseSingleRecipe(String recipeContent, String fileName, int recipeNumber, FabricBrewingRecipeRegistryBuilder builder) {
        try {
            BrewingRecipe recipe = new BrewingRecipe();

            // 解析输入物品
            parseItemSection(recipeContent, "input", recipe.input);

            // 解析材料物品
            parseMaterialSection(recipeContent, recipe);

            // 解析输出物品
            parseItemSection(recipeContent, "output", recipe.output);

            if (recipe.isValid()) {
                registerBrewingRecipe(recipe, fileName, recipeNumber, builder);
            } else {
                LOGGER.warn("文件 {} 中的配方 #{} 不完整", fileName, recipeNumber);
            }

        } catch (Exception e) {
            LOGGER.error("解析文件 {} 中的配方 #{} 时出错: {}", fileName, recipeNumber, e.getMessage());
        }
    }

    private void parseItemSection(String content, String prefix, ItemData itemData) {
        // 匹配 input.add(...).component(...).component(...).toCreateBrewing()
        Pattern pattern = Pattern.compile(
                prefix + "\\.add\\(([^)]+)\\)" +
                        "(.*?)\\.toCreateBrewing\\(\\)",
                Pattern.DOTALL
        );
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String itemStr = matcher.group(1);
            itemData.itemId = parseItemString(itemStr);

            String componentsStr = matcher.group(2);
            parseComponents(componentsStr, itemData);
        }
    }

    private void parseMaterialSection(String content, BrewingRecipe recipe) {
        Pattern materialPattern = Pattern.compile("material\\.add\\(([^)]+)\\)\\.toCreateBrewing\\(\\)");
        Matcher materialMatcher = materialPattern.matcher(content);

        if (materialMatcher.find()) {
            String itemStr = materialMatcher.group(1);
            recipe.materialItem = parseItemString(itemStr);
        }
    }

    private void parseComponents(String componentsStr, ItemData itemData) {
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

    private String parseItemString(String itemString) {
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

    private void registerBrewingRecipe(BrewingRecipe recipe, String fileName, int recipeNumber, FabricBrewingRecipeRegistryBuilder builder) {
        try {
            Item inputItem = Registries.ITEM.get(Identifier.of(recipe.input.itemId));
            Item materialItem = Registries.ITEM.get(Identifier.of(recipe.materialItem));
            Item outputItem = Registries.ITEM.get(Identifier.of(recipe.output.itemId));

            if (inputItem != null && materialItem != null && outputItem != null) {
                // 获取输入和输出的药水效果
                RegistryEntry<Potion> inputPotion = recipe.input.potionValue != null ?
                        CustomPotionRegistry.getPotion(recipe.input.potionValue) : null;
                RegistryEntry<Potion> outputPotion = recipe.output.potionValue != null ?
                        CustomPotionRegistry.getPotion(recipe.output.potionValue) : null;

                // 创建 Ingredient
                Ingredient ingredient = Ingredient.ofItems(materialItem);

                // 判断配方类型并注册
                if (isPotionConversionRecipe(inputItem, outputItem, inputPotion, outputPotion)) {
                    // 药水效果转换配方
                    registerPotionConversionRecipe(builder, inputPotion, ingredient, outputPotion);
                } else if (isItemConversionRecipe(inputItem, outputItem)) {
                    // 物品类型转换配方（如普通药水→喷溅药水）
                    registerItemConversionRecipe(builder, inputItem, ingredient, outputItem);
                } else {
                    LOGGER.warn("无法识别的配方类型: {} -> {}", recipe.input.itemId, recipe.output.itemId);
                    return;
                }

                registeredRecipeCount++;
                LOGGER.info("已注册酿造配方 #{}: {} + {} -> {} (来自 {})",
                        recipeNumber, recipe.input.itemId, recipe.materialItem, recipe.output.itemId, fileName);
            } else {
                LOGGER.warn("无法找到物品: input={}, material={}, output={}",
                        recipe.input.itemId, recipe.materialItem, recipe.output.itemId);
            }

        } catch (Exception e) {
            LOGGER.error("注册酿造配方 #{} 时出错: {}", recipeNumber, e.getMessage(), e);
        }
    }

    /**
     * 判断是否为药水效果转换配方
     */
    private boolean isPotionConversionRecipe(Item inputItem, Item outputItem, RegistryEntry<Potion> inputPotion, RegistryEntry<Potion> outputPotion) {
        return inputItem == outputItem && inputPotion != null && outputPotion != null;
    }

    /**
     * 判断是否为物品类型转换配方
     */
    private boolean isItemConversionRecipe(Item inputItem, Item outputItem) {
        return inputItem != outputItem && isPotionItem(inputItem) && isPotionItem(outputItem);
    }

    /**
     * 判断是否为药水物品
     */
    private boolean isPotionItem(Item item) {
        return item == Items.POTION || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION;
    }

    /**
     * 注册药水效果转换配方
     */
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

    /**
     * 注册物品类型转换配方
     */
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

    /**
     * 获取药水名称
     */
    private String getPotionName(RegistryEntry<Potion> potion) {
        return potion.getKey().map(key -> key.getValue().toString()).orElse("unknown");
    }

    /**
     * 获取 Ingredient 名称（简化版，实际使用时可能需要更复杂的逻辑）
     */
    private String getIngredientName(Ingredient ingredient) {
        // 简化处理：获取第一个匹配的物品作为名称
        ItemStack[] stacks = ingredient.getMatchingStacks();
        if (stacks.length > 0) {
            return Registries.ITEM.getId(stacks[0].getItem()).toString();
        }
        return "unknown_ingredient";
    }

    /**
     * 获取已注册的配方数量
     */
    public int getRecipeCount() {
        return registeredRecipeCount;
    }

    // 内部类用于存储配方数据
    private static class BrewingRecipe {
        ItemData input = new ItemData();
        String materialItem;
        ItemData output = new ItemData();

        boolean isValid() {
            return input.itemId != null && materialItem != null && output.itemId != null;
        }
    }

    private static class ItemData {
        String itemId;
        String potionValue; // 药水效果值
        Map<String, String> components = new HashMap<>();
    }
}