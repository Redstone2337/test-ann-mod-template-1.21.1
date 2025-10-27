package net.redstone233.tam.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;

public class ModItemGroups {

    public static final RegistryKey<ItemGroup> MOD_ITEMS = register("mod_items");

    public static final RegistryKey<ItemGroup> MOD_WEAPONS = register("mod_weapons");

    public static final RegistryKey<ItemGroup> MOD_ARMOR = register("mod_armor");

    private static RegistryKey<ItemGroup> register(String id) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(TestAnnMod.MOD_ID,id));
    }


    public static void register() {
        Registry.register(Registries.ITEM_GROUP,ModItemGroups.MOD_ITEMS,
                ItemGroup.create(
                                ItemGroup.Row.TOP, 0)
                        .displayName(Text.translatable("itemGroup.tam.mod_items"))
                        .icon(() -> new ItemStack(ModItems.BLAZING_FLAME_SWORD)).entries((displayContext, entries) -> {
                            entries.add(ModItems.BLAZING_FLAME_SWORD);
                            entries.add(ModItems.ICE_FREEZE_SWORD);
                            entries.add(ModItems.GUIDITE_HELMET);
                            entries.add(ModItems.GUIDITE_CHESTPLATE);
                            entries.add(ModItems.GUIDITE_LEGGINGS);
                            entries.add(ModItems.GUIDITE_BOOTS);
                            entries.add(ModItems.SUSPICIOUS_SUBSTANCE);
                        })
                        .build()
        );

        Registry.register(Registries.ITEM_GROUP,ModItemGroups.MOD_WEAPONS,
                ItemGroup.create(
                                ItemGroup.Row.BOTTOM, 2)
                        .displayName(Text.translatable("itemGroup.tam.mod_weapons"))
                        .icon(() -> new ItemStack(ModItems.ICE_FREEZE_SWORD)).entries((displayContext, entries) -> {
                            entries.add(ModItems.BLAZING_FLAME_SWORD);
                            entries.add(ModItems.ICE_FREEZE_SWORD);
                        })
                        .build()
        );

        Registry.register(Registries.ITEM_GROUP,ModItemGroups.MOD_ARMOR,
                ItemGroup.create(
                                ItemGroup.Row.BOTTOM, 3)
                        .displayName(Text.translatable("itemGroup.tam.mod_armors"))
                        .icon(() -> new ItemStack(ModItems.GUIDITE_CHESTPLATE)).entries((displayContext, entries) -> {
                            entries.add(ModItems.GUIDITE_HELMET);
                            entries.add(ModItems.GUIDITE_CHESTPLATE);
                            entries.add(ModItems.GUIDITE_LEGGINGS);
                            entries.add(ModItems.GUIDITE_BOOTS);
                            entries.add(ModItems.SUSPICIOUS_SUBSTANCE);
                        })
                        .build()
        );

        TestAnnMod.LOGGER.info("Registered Item Groups");
    }
}
