package net.redstone233.tam.enchantment;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;
import net.redstone233.tam.enchantment.effect.FireBlessingEnchantmentEffect;
import net.redstone233.tam.enchantment.effect.FrostBlessingEnchantmentEffect;
import net.redstone233.tam.enchantment.effect.VampirismEnchantmentEffect;

public class ModEnchantmentEffects {

    public static final MapCodec<VampirismEnchantmentEffect> VAMPIRISM =
            register("vampirism", VampirismEnchantmentEffect.CODEC);

    public static final MapCodec<FireBlessingEnchantmentEffect> FIRE_BLESSING =
            register("fire_blessing", FireBlessingEnchantmentEffect.CODEC);

    public static final MapCodec<FrostBlessingEnchantmentEffect> FROST_BLESSING =
            register("frost_blessing", FrostBlessingEnchantmentEffect.CODEC);

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(TestAnnMod.MOD_ID, id), codec);
    }
    public static void registerModEnchantments() {
        TestAnnMod.LOGGER.info("Registering Mod Enchantment Types...");
    }

}
