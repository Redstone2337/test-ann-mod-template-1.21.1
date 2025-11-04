package net.redstone233.tam.core.brewing;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * 独立的酿造配方解析器
 * 可以集成到任何 Fabric 1.21.1 模组中
 */
public class BrewingRecipeParser {
    private static final Logger LOGGER = LoggerFactory.getLogger("BrewingRecipeParser");

    private static final Map<String, RegistryEntry<Potion>> POTION_MAP = new HashMap<>();
    private static final Map<String, ComponentType<?>> COMPONENT_MAP = new HashMap<>();
    private static final Map<String, RegistryEntry<StatusEffect>> STATUS_EFFECT_MAP = new HashMap<>();

    private final String modId;
    private final Path configDir;
    private boolean initialized = false;

    /**
     * 构造函数
     * @param modId 你的模组ID
     */
    public BrewingRecipeParser(String modId) {
        this.modId = modId;
        this.configDir = Path.of(FabricLoader.getInstance().getGameDir() + modId + "/brewing_recipes");
    }

    /**
     * 初始化解析器
     */
    public void initialize() {
        if (initialized) {
            return;
        }

        LOGGER.info("初始化酿造配方解析器 for mod: {}", modId);

        // 初始化映射
        initializePotionMap();
        initializeComponentMap();
        initializeStatusEffectMap();

        initialized = true;
        LOGGER.info("酿造配方解析器初始化完成");
    }

    /**
     * 加载所有酿造配方
     */
    public void loadBrewingRecipes() {
        if (!initialized) {
            initialize();
        }

        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
                createExampleRecipeFile();
                LOGGER.info("创建酿造配方目录: {}", configDir);
                return;
            }

            // 清空之前的配方
            CustomBrewingRecipeRegistry.clearRecipes();

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(configDir, "*.js")) {
                for (Path file : stream) {
                    parseBrewingRecipeFile(file);
                }
            }

            LOGGER.info("为模组 {} 加载了 {} 个自定义酿造配方", modId, CustomBrewingRecipeRegistry.getRecipeCount());

        } catch (IOException e) {
            LOGGER.error("读取酿造配方文件时出错: {}", e.getMessage());
        }
    }

    /**
     * 重新加载酿造配方
     */
    public void reloadBrewingRecipes() {
        LOGGER.info("重新加载酿造配方 for mod: {}", modId);
        loadBrewingRecipes();
    }

    private void initializePotionMap() {
        // 注册所有原版药水 - 直接使用 Potions 类中的 RegistryEntry
        POTION_MAP.put("Potions.WATER", Potions.WATER);
        POTION_MAP.put("Potions.AWKWARD", Potions.AWKWARD);
        POTION_MAP.put("Potions.THICK", Potions.THICK);
        POTION_MAP.put("Potions.MUNDANE", Potions.MUNDANE);
        POTION_MAP.put("Potions.NIGHT_VISION", Potions.NIGHT_VISION);
        POTION_MAP.put("Potions.LONG_NIGHT_VISION", Potions.LONG_NIGHT_VISION);
        POTION_MAP.put("Potions.INVISIBILITY", Potions.INVISIBILITY);
        POTION_MAP.put("Potions.LONG_INVISIBILITY", Potions.LONG_INVISIBILITY);
        POTION_MAP.put("Potions.LEAPING", Potions.LEAPING);
        POTION_MAP.put("Potions.LONG_LEAPING", Potions.LONG_LEAPING);
        POTION_MAP.put("Potions.STRONG_LEAPING", Potions.STRONG_LEAPING);
        POTION_MAP.put("Potions.SWIFTNESS", Potions.SWIFTNESS);
        POTION_MAP.put("Potions.LONG_SWIFTNESS", Potions.LONG_SWIFTNESS);
        POTION_MAP.put("Potions.STRONG_SWIFTNESS", Potions.STRONG_SWIFTNESS);
        POTION_MAP.put("Potions.SLOWNESS", Potions.SLOWNESS);
        POTION_MAP.put("Potions.LONG_SLOWNESS", Potions.LONG_SLOWNESS);
        POTION_MAP.put("Potions.STRONG_SLOWNESS", Potions.STRONG_SLOWNESS);
        POTION_MAP.put("Potions.TURTLE_MASTER", Potions.TURTLE_MASTER);
        POTION_MAP.put("Potions.LONG_TURTLE_MASTER", Potions.LONG_TURTLE_MASTER);
        POTION_MAP.put("Potions.STRONG_TURTLE_MASTER", Potions.STRONG_TURTLE_MASTER);
        POTION_MAP.put("Potions.WATER_BREATHING", Potions.WATER_BREATHING);
        POTION_MAP.put("Potions.LONG_WATER_BREATHING", Potions.LONG_WATER_BREATHING);
        POTION_MAP.put("Potions.HEALING", Potions.HEALING);
        POTION_MAP.put("Potions.STRONG_HEALING", Potions.STRONG_HEALING);
        POTION_MAP.put("Potions.HARMING", Potions.HARMING);
        POTION_MAP.put("Potions.STRONG_HARMING", Potions.STRONG_HARMING);
        POTION_MAP.put("Potions.POISON", Potions.POISON);
        POTION_MAP.put("Potions.LONG_POISON", Potions.LONG_POISON);
        POTION_MAP.put("Potions.STRONG_POISON", Potions.STRONG_POISON);
        POTION_MAP.put("Potions.REGENERATION", Potions.REGENERATION);
        POTION_MAP.put("Potions.LONG_REGENERATION", Potions.LONG_REGENERATION);
        POTION_MAP.put("Potions.STRONG_REGENERATION", Potions.STRONG_REGENERATION);
        POTION_MAP.put("Potions.STRENGTH", Potions.STRENGTH);
        POTION_MAP.put("Potions.LONG_STRENGTH", Potions.LONG_STRENGTH);
        POTION_MAP.put("Potions.STRONG_STRENGTH", Potions.STRONG_STRENGTH);
        POTION_MAP.put("Potions.WEAKNESS", Potions.WEAKNESS);
        POTION_MAP.put("Potions.LONG_WEAKNESS", Potions.LONG_WEAKNESS);
        POTION_MAP.put("Potions.LUCK", Potions.LUCK);
        POTION_MAP.put("Potions.SLOW_FALLING", Potions.SLOW_FALLING);
        POTION_MAP.put("Potions.LONG_SLOW_FALLING", Potions.LONG_SLOW_FALLING);
        POTION_MAP.put("Potions.WIND_CHARGED", Potions.WIND_CHARGED);
        POTION_MAP.put("Potions.WEAVING", Potions.WEAVING);
        POTION_MAP.put("Potions.OOZING", Potions.OOZING);
        POTION_MAP.put("Potions.INFESTED", Potions.INFESTED);

        LOGGER.debug("已初始化 {} 种药水", POTION_MAP.size());
    }

    private void initializeComponentMap() {
        COMPONENT_MAP.put("DataComponentTypes.POTION_CONTENTS", DataComponentTypes.POTION_CONTENTS);
        COMPONENT_MAP.put("DataComponentTypes.CUSTOM_NAME", DataComponentTypes.CUSTOM_NAME);
        COMPONENT_MAP.put("DataComponentTypes.ITEM_NAME", DataComponentTypes.ITEM_NAME);
        COMPONENT_MAP.put("DataComponentTypes.LORE", DataComponentTypes.LORE);
        COMPONENT_MAP.put("DataComponentTypes.CUSTOM_DATA", DataComponentTypes.CUSTOM_DATA);
        COMPONENT_MAP.put("DataComponentTypes.ENCHANTMENTS", DataComponentTypes.ENCHANTMENTS);
        COMPONENT_MAP.put("DataComponentTypes.REPAIR_COST", DataComponentTypes.REPAIR_COST);
        COMPONENT_MAP.put("DataComponentTypes.CUSTOM_MODEL_DATA", DataComponentTypes.CUSTOM_MODEL_DATA);
        COMPONENT_MAP.put("DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP", DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP);
        COMPONENT_MAP.put("DataComponentTypes.HIDE_TOOLTIP", DataComponentTypes.HIDE_TOOLTIP);
        COMPONENT_MAP.put("DataComponentTypes.UNBREAKABLE", DataComponentTypes.UNBREAKABLE);
        COMPONENT_MAP.put("DataComponentTypes.DAMAGE", DataComponentTypes.DAMAGE);
        COMPONENT_MAP.put("DataComponentTypes.MAX_DAMAGE", DataComponentTypes.MAX_DAMAGE);
        COMPONENT_MAP.put("DataComponentTypes.SUSPICIOUS_STEW_EFFECTS", DataComponentTypes.SUSPICIOUS_STEW_EFFECTS);
        COMPONENT_MAP.put("DataComponentTypes.FOOD", DataComponentTypes.FOOD);

        LOGGER.debug("已初始化 {} 种组件类型", COMPONENT_MAP.size());
    }

    private void initializeStatusEffectMap() {
        // 注册所有状态效果 - 直接使用 StatusEffects 类中的 RegistryEntry
        STATUS_EFFECT_MAP.put("StatusEffects.SPEED", StatusEffects.SPEED);
        STATUS_EFFECT_MAP.put("StatusEffects.SLOWNESS", StatusEffects.SLOWNESS);
        STATUS_EFFECT_MAP.put("StatusEffects.HASTE", StatusEffects.HASTE);
        STATUS_EFFECT_MAP.put("StatusEffects.MINING_FATIGUE", StatusEffects.MINING_FATIGUE);
        STATUS_EFFECT_MAP.put("StatusEffects.STRENGTH", StatusEffects.STRENGTH);
        STATUS_EFFECT_MAP.put("StatusEffects.INSTANT_HEALTH", StatusEffects.INSTANT_HEALTH);
        STATUS_EFFECT_MAP.put("StatusEffects.INSTANT_DAMAGE", StatusEffects.INSTANT_DAMAGE);
        STATUS_EFFECT_MAP.put("StatusEffects.JUMP_BOOST", StatusEffects.JUMP_BOOST);
        STATUS_EFFECT_MAP.put("StatusEffects.NAUSEA", StatusEffects.NAUSEA);
        STATUS_EFFECT_MAP.put("StatusEffects.REGENERATION", StatusEffects.REGENERATION);
        STATUS_EFFECT_MAP.put("StatusEffects.RESISTANCE", StatusEffects.RESISTANCE);
        STATUS_EFFECT_MAP.put("StatusEffects.FIRE_RESISTANCE", StatusEffects.FIRE_RESISTANCE);
        STATUS_EFFECT_MAP.put("StatusEffects.WATER_BREATHING", StatusEffects.WATER_BREATHING);
        STATUS_EFFECT_MAP.put("StatusEffects.INVISIBILITY", StatusEffects.INVISIBILITY);
        STATUS_EFFECT_MAP.put("StatusEffects.BLINDNESS", StatusEffects.BLINDNESS);
        STATUS_EFFECT_MAP.put("StatusEffects.NIGHT_VISION", StatusEffects.NIGHT_VISION);
        STATUS_EFFECT_MAP.put("StatusEffects.HUNGER", StatusEffects.HUNGER);
        STATUS_EFFECT_MAP.put("StatusEffects.WEAKNESS", StatusEffects.WEAKNESS);
        STATUS_EFFECT_MAP.put("StatusEffects.POISON", StatusEffects.POISON);
        STATUS_EFFECT_MAP.put("StatusEffects.WITHER", StatusEffects.WITHER);
        STATUS_EFFECT_MAP.put("StatusEffects.HEALTH_BOOST", StatusEffects.HEALTH_BOOST);
        STATUS_EFFECT_MAP.put("StatusEffects.ABSORPTION", StatusEffects.ABSORPTION);
        STATUS_EFFECT_MAP.put("StatusEffects.SATURATION", StatusEffects.SATURATION);
        STATUS_EFFECT_MAP.put("StatusEffects.GLOWING", StatusEffects.GLOWING);
        STATUS_EFFECT_MAP.put("StatusEffects.LEVITATION", StatusEffects.LEVITATION);
        STATUS_EFFECT_MAP.put("StatusEffects.LUCK", StatusEffects.LUCK);
        STATUS_EFFECT_MAP.put("StatusEffects.UNLUCK", StatusEffects.UNLUCK);
        STATUS_EFFECT_MAP.put("StatusEffects.SLOW_FALLING", StatusEffects.SLOW_FALLING);
        STATUS_EFFECT_MAP.put("StatusEffects.CONDUIT_POWER", StatusEffects.CONDUIT_POWER);
        STATUS_EFFECT_MAP.put("StatusEffects.DOLPHINS_GRACE", StatusEffects.DOLPHINS_GRACE);
        STATUS_EFFECT_MAP.put("StatusEffects.BAD_OMEN", StatusEffects.BAD_OMEN);
        STATUS_EFFECT_MAP.put("StatusEffects.HERO_OF_THE_VILLAGE", StatusEffects.HERO_OF_THE_VILLAGE);
        STATUS_EFFECT_MAP.put("StatusEffects.DARKNESS", StatusEffects.DARKNESS);
        STATUS_EFFECT_MAP.put("StatusEffects.TRIAL_OMEN", StatusEffects.TRIAL_OMEN);
        STATUS_EFFECT_MAP.put("StatusEffects.RAID_OMEN", StatusEffects.RAID_OMEN);
        STATUS_EFFECT_MAP.put("StatusEffects.WIND_CHARGED", StatusEffects.WIND_CHARGED);
        STATUS_EFFECT_MAP.put("StatusEffects.WEAVING", StatusEffects.WEAVING);
        STATUS_EFFECT_MAP.put("StatusEffects.OOZING", StatusEffects.OOZING);
        STATUS_EFFECT_MAP.put("StatusEffects.INFESTED", StatusEffects.INFESTED);

        LOGGER.debug("已初始化 {} 种状态效果", STATUS_EFFECT_MAP.size());
    }

    private void createExampleRecipeFile() throws IOException {
        Path exampleFile = configDir.resolve("example_recipes.js");
        String exampleContent =
                "// 示例酿造配方文件 - 模组: " + modId + "\n" +
                        "// 基础药水配方\n" +
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
                        "// 自定义效果药水\n" +
                        "BrewingRecipeEvent.create(e => {\n" +
                        "    let {input, material, output} = e;\n" +
                        "    input.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.AWKWARD)\n" +
                        "        .toCreateBrewing();\n" +
                        "    material.add(Items.GOLDEN_CARROT).toCreateBrewing();\n" +
                        "    output.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.NIGHT_VISION)\n" +
                        "        .toCreateBrewing();\n" +
                        "})\n\n" +
                        "// 自定义名称的药水\n" +
                        "BrewingRecipeEvent.create(e => {\n" +
                        "    let {input, material, output} = e;\n" +
                        "    input.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.AWKWARD)\n" +
                        "        .toCreateBrewing();\n" +
                        "    material.add(Items.EMERALD).toCreateBrewing();\n" +
                        "    output.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.SWIFTNESS)\n" +
                        "        .component(DataComponentTypes.CUSTOM_NAME, '幸运药水')\n" +
                        "        .component(DataComponentTypes.LORE, '喝下这瓶药水会带来好运！')\n" +
                        "        .toCreateBrewing();\n" +
                        "})\n\n" +
                        "// 喷溅型药水\n" +
                        "BrewingRecipeEvent.create(e => {\n" +
                        "    let {input, material, output} = e;\n" +
                        "    input.add(Items.POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.STRENGTH)\n" +
                        "        .toCreateBrewing();\n" +
                        "    material.add(Items.GUNPOWDER).toCreateBrewing();\n" +
                        "    output.add(Items.SPLASH_POTION)\n" +
                        "        .component(DataComponentTypes.POTION_CONTENTS, Potions.STRENGTH)\n" +
                        "        .toCreateBrewing();\n" +
                        "})";

        Files.writeString(exampleFile, exampleContent);
        LOGGER.info("已创建示例配方文件: {}", exampleFile);
    }

    private void parseBrewingRecipeFile(Path file) {
        try {
            String content = Files.readString(file);
            LOGGER.info("解析酿造配方文件: {}", file.getFileName());

            // 查找所有配方块
            Pattern recipePattern = Pattern.compile("BrewingRecipeEvent\\.create\\(e\\s*=>\\s*\\{([^}]+)\\}", Pattern.DOTALL);
            Matcher recipeMatcher = recipePattern.matcher(content);

            int recipeCount = 0;
            while (recipeMatcher.find()) {
                recipeCount++;
                String recipeContent = recipeMatcher.group(1);
                parseSingleRecipe(recipeContent, file.getFileName().toString(), recipeCount);
            }

            LOGGER.info("在文件 {} 中找到 {} 个配方", file.getFileName(), recipeCount);

        } catch (IOException e) {
            LOGGER.error("读取文件 {} 时出错: {}", file.getFileName(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error("解析文件 {} 时出错: {}", file.getFileName(), e.getMessage(), e);
        }
    }

    private void parseSingleRecipe(String recipeContent, String fileName, int recipeNumber) {
        try {
            BrewingRecipe recipe = new BrewingRecipe();

            // 解析输入物品
            parseItemSection(recipeContent, "input", recipe.input);

            // 解析材料物品
            parseMaterialSection(recipeContent, recipe);

            // 解析输出物品
            parseItemSection(recipeContent, "output", recipe.output);

            if (recipe.isValid()) {
                registerBrewingRecipe(recipe, fileName, recipeNumber);
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
            parseComponents(componentsStr, itemData.components);
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

    private void parseComponents(String componentsStr, Map<String, String> components) {
        if (componentsStr == null || componentsStr.trim().isEmpty()) {
            return;
        }

        // 匹配 .component(ComponentType, value)
        Pattern componentPattern = Pattern.compile("\\.component\\(([^,]+),\\s*([^)]+)\\)", Pattern.DOTALL);
        Matcher componentMatcher = componentPattern.matcher(componentsStr);

        while (componentMatcher.find()) {
            String componentType = componentMatcher.group(1).trim();
            String componentValue = componentMatcher.group(2).trim();
            components.put(componentType, componentValue);
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

    private void registerBrewingRecipe(BrewingRecipe recipe, String fileName, int recipeNumber) {
        try {
            Item inputItem = Registries.ITEM.get(Identifier.of(recipe.input.itemId));
            Item materialItem = Registries.ITEM.get(Identifier.of(recipe.materialItem));
            Item outputItem = Registries.ITEM.get(Identifier.of(recipe.output.itemId));

            if (inputItem != null && materialItem != null && outputItem != null) {
                // 创建带有组件的物品堆栈
                ItemStack inputStack = createItemStackWithComponents(inputItem, recipe.input.components);
                ItemStack outputStack = createItemStackWithComponents(outputItem, recipe.output.components);

                // 注册酿造配方
                CustomBrewingRecipeRegistry.registerBrewingRecipe(inputStack, materialItem, outputStack);

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

    private ItemStack createItemStackWithComponents(Item item, Map<String, String> components) {
        ItemStack stack = new ItemStack(item);

        for (Map.Entry<String, String> entry : components.entrySet()) {
            applyComponentToStack(stack, entry.getKey(), entry.getValue());
        }

        return stack;
    }

    private void applyComponentToStack(ItemStack stack, String componentType, String componentValue) {
        try {
            if (COMPONENT_MAP.containsKey(componentType)) {
                ComponentType<?> type = COMPONENT_MAP.get(componentType);

                if (type == DataComponentTypes.POTION_CONTENTS) {
                    RegistryEntry<Potion> potion = parsePotionValue(componentValue);
                    if (potion != null) {
                        stack.set(DataComponentTypes.POTION_CONTENTS,
                                new PotionContentsComponent(potion));
                    }
                }
                else if (type == DataComponentTypes.CUSTOM_NAME) {
                    // 处理自定义名称
                    String name = parseStringValue(componentValue);
                    stack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(name));
                }
                else if (type == DataComponentTypes.LORE) {
                    // 处理Lore
                    String loreText = parseStringValue(componentValue);
                    LoreComponent lore = new LoreComponent(List.of(Text.literal(loreText)));
                    stack.set(DataComponentTypes.LORE, lore);
                }
                else if (type == DataComponentTypes.CUSTOM_MODEL_DATA) {
                    // 处理自定义模型数据
                    int customModelData = Integer.parseInt(componentValue);
                    stack.set(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(customModelData));
                }
                else if (type == DataComponentTypes.DAMAGE) {
                    // 处理损坏值
                    int damage = Integer.parseInt(componentValue);
                    stack.set(DataComponentTypes.DAMAGE, damage);
                }
                // 可以在这里添加更多组件类型的支持
            }
        } catch (Exception e) {
            LOGGER.warn("应用组件 {} 时出错: {}", componentType, e.getMessage());
        }
    }

    private RegistryEntry<Potion> parsePotionValue(String potionValue) {
        potionValue = potionValue.trim();

        // 检查预定义的药水映射
        if (POTION_MAP.containsKey(potionValue)) {
            return POTION_MAP.get(potionValue);
        }

        // 尝试从注册表获取
        try {
            Identifier potionId = Identifier.of(potionValue.toLowerCase());
            return Registries.POTION.getEntry(RegistryKey.of(RegistryKeys.POTION, potionId)).orElse(null);
        } catch (Exception e) {
            LOGGER.warn("无法解析药水: {}", potionValue);
            return null;
        }
    }

    private String parseStringValue(String value) {
        value = value.trim();

        // 移除引号
        if ((value.startsWith("\"") && value.endsWith("\"")) ||
                (value.startsWith("'") && value.endsWith("'"))) {
            return value.substring(1, value.length() - 1);
        }

        return value;
    }

    /**
     * 获取已注册的配方数量
     */
    public int getRecipeCount() {
        return CustomBrewingRecipeRegistry.getRecipeCount();
    }

    /**
     * 获取所有已注册的配方
     */
    public List<CustomBrewingRecipeRegistry.CustomBrewingRecipe> getRecipes() {
        return CustomBrewingRecipeRegistry.getCustomRecipes();
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
        Map<String, String> components = new HashMap<>();
    }
}