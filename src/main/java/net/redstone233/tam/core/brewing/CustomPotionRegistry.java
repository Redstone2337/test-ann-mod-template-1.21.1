package net.redstone233.tam.core.brewing;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义药水注册器
 * 负责注册自定义药水效果和提供药水注册条目的获取
 */
public class CustomPotionRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger("CustomPotionRegistry");

    // 自定义药水映射表
    private static final java.util.Map<String, RegistryEntry<Potion>> CUSTOM_POTIONS = new java.util.HashMap<>();

    // 原版药水映射表（复制自BrewingRecipeParser）
    private static final java.util.Map<String, RegistryEntry<Potion>> VANILLA_POTION_MAP = new java.util.HashMap<>();

    /**
     * 初始化并注册自定义药水
     */
    public static void initialize() {
        LOGGER.info("初始化自定义药水注册器");

        // 初始化原版药水映射
        initializeVanillaPotionMap();

        // 注册自定义药水
        registerCustomPotions();

        LOGGER.info("已注册 {} 个自定义药水", CUSTOM_POTIONS.size());
    }

    private static void initializeVanillaPotionMap() {
        // 注册所有原版药水 - 直接使用 Potions 类中的 RegistryEntry
        VANILLA_POTION_MAP.put("Potions.WATER", Potions.WATER);
        VANILLA_POTION_MAP.put("Potions.AWKWARD", Potions.AWKWARD);
        VANILLA_POTION_MAP.put("Potions.THICK", Potions.THICK);
        VANILLA_POTION_MAP.put("Potions.MUNDANE", Potions.MUNDANE);
        VANILLA_POTION_MAP.put("Potions.NIGHT_VISION", Potions.NIGHT_VISION);
        VANILLA_POTION_MAP.put("Potions.LONG_NIGHT_VISION", Potions.LONG_NIGHT_VISION);
        VANILLA_POTION_MAP.put("Potions.INVISIBILITY", Potions.INVISIBILITY);
        VANILLA_POTION_MAP.put("Potions.LONG_INVISIBILITY", Potions.LONG_INVISIBILITY);
        VANILLA_POTION_MAP.put("Potions.LEAPING", Potions.LEAPING);
        VANILLA_POTION_MAP.put("Potions.LONG_LEAPING", Potions.LONG_LEAPING);
        VANILLA_POTION_MAP.put("Potions.STRONG_LEAPING", Potions.STRONG_LEAPING);
        VANILLA_POTION_MAP.put("Potions.SWIFTNESS", Potions.SWIFTNESS);
        VANILLA_POTION_MAP.put("Potions.LONG_SWIFTNESS", Potions.LONG_SWIFTNESS);
        VANILLA_POTION_MAP.put("Potions.STRONG_SWIFTNESS", Potions.STRONG_SWIFTNESS);
        VANILLA_POTION_MAP.put("Potions.SLOWNESS", Potions.SLOWNESS);
        VANILLA_POTION_MAP.put("Potions.LONG_SLOWNESS", Potions.LONG_SLOWNESS);
        VANILLA_POTION_MAP.put("Potions.STRONG_SLOWNESS", Potions.STRONG_SLOWNESS);
        VANILLA_POTION_MAP.put("Potions.TURTLE_MASTER", Potions.TURTLE_MASTER);
        VANILLA_POTION_MAP.put("Potions.LONG_TURTLE_MASTER", Potions.LONG_TURTLE_MASTER);
        VANILLA_POTION_MAP.put("Potions.STRONG_TURTLE_MASTER", Potions.STRONG_TURTLE_MASTER);
        VANILLA_POTION_MAP.put("Potions.WATER_BREATHING", Potions.WATER_BREATHING);
        VANILLA_POTION_MAP.put("Potions.LONG_WATER_BREATHING", Potions.LONG_WATER_BREATHING);
        VANILLA_POTION_MAP.put("Potions.HEALING", Potions.HEALING);
        VANILLA_POTION_MAP.put("Potions.STRONG_HEALING", Potions.STRONG_HEALING);
        VANILLA_POTION_MAP.put("Potions.HARMING", Potions.HARMING);
        VANILLA_POTION_MAP.put("Potions.STRONG_HARMING", Potions.STRONG_HARMING);
        VANILLA_POTION_MAP.put("Potions.POISON", Potions.POISON);
        VANILLA_POTION_MAP.put("Potions.LONG_POISON", Potions.LONG_POISON);
        VANILLA_POTION_MAP.put("Potions.STRONG_POISON", Potions.STRONG_POISON);
        VANILLA_POTION_MAP.put("Potions.REGENERATION", Potions.REGENERATION);
        VANILLA_POTION_MAP.put("Potions.LONG_REGENERATION", Potions.LONG_REGENERATION);
        VANILLA_POTION_MAP.put("Potions.STRONG_REGENERATION", Potions.STRONG_REGENERATION);
        VANILLA_POTION_MAP.put("Potions.STRENGTH", Potions.STRENGTH);
        VANILLA_POTION_MAP.put("Potions.LONG_STRENGTH", Potions.LONG_STRENGTH);
        VANILLA_POTION_MAP.put("Potions.STRONG_STRENGTH", Potions.STRONG_STRENGTH);
        VANILLA_POTION_MAP.put("Potions.WEAKNESS", Potions.WEAKNESS);
        VANILLA_POTION_MAP.put("Potions.LONG_WEAKNESS", Potions.LONG_WEAKNESS);
        VANILLA_POTION_MAP.put("Potions.LUCK", Potions.LUCK);
        VANILLA_POTION_MAP.put("Potions.SLOW_FALLING", Potions.SLOW_FALLING);
        VANILLA_POTION_MAP.put("Potions.LONG_SLOW_FALLING", Potions.LONG_SLOW_FALLING);
        VANILLA_POTION_MAP.put("Potions.WIND_CHARGED", Potions.WIND_CHARGED);
        VANILLA_POTION_MAP.put("Potions.WEAVING", Potions.WEAVING);
        VANILLA_POTION_MAP.put("Potions.OOZING", Potions.OOZING);
        VANILLA_POTION_MAP.put("Potions.INFESTED", Potions.INFESTED);

        LOGGER.debug("已初始化 {} 种原版药水", VANILLA_POTION_MAP.size());
    }

    private static void registerCustomPotions() {
        // 示例：注册一些自定义药水
        // 你可以在实际使用中根据配置文件动态注册

        // 幸运药水 - 跳跃提升 + 速度
        registerPotion("luck_potion", new Potion(
                "luck",
                new StatusEffectInstance(StatusEffects.JUMP_BOOST, 3600, 1),
                new StatusEffectInstance(StatusEffects.SPEED, 3600, 1)
        ));

        // 力量药水 II - 更强的力量效果
        registerPotion("strength_potion_ii", new Potion(
                "strength_ii",
                new StatusEffectInstance(StatusEffects.STRENGTH, 1800, 2)
        ));

        // 再生药水延长版 - 更长的再生时间
        registerPotion("long_regeneration_potion", new Potion(
                "long_regeneration",
                new StatusEffectInstance(StatusEffects.REGENERATION, 1800, 1)
        ));

        // 抗性药水 - 抗性提升
        registerPotion("resistance_potion", new Potion(
                "resistance",
                new StatusEffectInstance(StatusEffects.RESISTANCE, 3600, 1)
        ));
    }

    /**
     * 注册自定义药水
     */
    private static void registerPotion(String name, Potion potion) {
        try {
            Identifier potionId = Identifier.of("tam", name);
            Potion registeredPotion = Registry.register(Registries.POTION, potionId, potion);
            RegistryEntry<Potion> potionEntry = Registries.POTION.getEntry(registeredPotion);

            if (potionEntry != null) {
                CUSTOM_POTIONS.put("tam:" + name, potionEntry);
                CUSTOM_POTIONS.put("Potions." + name.toUpperCase(), potionEntry);
                LOGGER.debug("已注册自定义药水: {}", potionId);
            }
        } catch (Exception e) {
            LOGGER.error("注册自定义药水 {} 时出错: {}", name, e.getMessage());
        }
    }

    /**
     * 获取药水注册条目
     */
    public static RegistryEntry<Potion> getPotion(String potionKey) {
        // 首先检查自定义药水
        if (CUSTOM_POTIONS.containsKey(potionKey)) {
            return CUSTOM_POTIONS.get(potionKey);
        }

        // 然后检查原版药水映射
        if (VANILLA_POTION_MAP.containsKey(potionKey)) {
            return VANILLA_POTION_MAP.get(potionKey);
        }

        // 最后尝试从注册表获取
        try {
            Identifier potionId;
            if (potionKey.startsWith("Potions.")) {
                // 处理 Potions.XXX 格式
                String path = potionKey.substring(8).toLowerCase();
                potionId = Identifier.of("minecraft", path);
            } else if (potionKey.contains(":")) {
                // 处理 namespace:path 格式
                String[] parts = potionKey.split(":");
                potionId = Identifier.of(parts[0], parts[1]);
            } else {
                // 默认使用 minecraft 命名空间
                potionId = Identifier.of("minecraft", potionKey.toLowerCase());
            }

            return Registries.POTION.getEntry(potionId).orElse(null);
        } catch (Exception e) {
            LOGGER.warn("无法获取药水: {}", potionKey);
            return null;
        }
    }

    /**
     * 获取所有自定义药水
     */
    public static java.util.Map<String, RegistryEntry<Potion>> getCustomPotions() {
        return new java.util.HashMap<>(CUSTOM_POTIONS);
    }

    /**
     * 获取所有原版药水映射
     */
    public static java.util.Map<String, RegistryEntry<Potion>> getVanillaPotionMap() {
        return new java.util.HashMap<>(VANILLA_POTION_MAP);
    }
}