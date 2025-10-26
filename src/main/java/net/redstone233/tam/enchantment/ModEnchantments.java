package net.redstone233.tam.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;

public class ModEnchantments {

    public static final RegistryKey<Enchantment> VAMPIRISM = of("vampirism");
    public static final RegistryKey<Enchantment> FIRE_BLESSING = of("fire_blessing");
    public static final RegistryKey<Enchantment> FROST_BLESSING = of("frost_blessing");


    private static RegistryKey<Enchantment> of(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(TestAnnMod.MOD_ID, id));
    }

    public static void register() {
        TestAnnMod.LOGGER.info("Enchantments registered");
    }
}