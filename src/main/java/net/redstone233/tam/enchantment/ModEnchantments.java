package net.redstone233.tam.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;

public class ModEnchantments {

    public static final RegistryKey<Enchantment> VAMPIRISM = of("vampirism");
    public static final RegistryKey<Enchantment> FIRE_BLESSING = of("fire_blessing");
    public static final RegistryKey<Enchantment> FROST_BLESSING = of("frost_blessing");

    public static void bootstrap(Registerable<Enchantment> registry) {
        RegistryEntryLookup<Enchantment> registryEntryLookup2 = registry.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        RegistryEntryLookup<Item> registryEntryLookup3 = registry.getRegistryLookup(RegistryKeys.ITEM);
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }

    private static RegistryKey<Enchantment> of(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(TestAnnMod.MOD_ID, id));
    }
}