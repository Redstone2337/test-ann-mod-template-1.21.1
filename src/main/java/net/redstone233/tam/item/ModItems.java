package net.redstone233.tam.item;

import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;
import net.redstone233.tam.armor.ModArmorMaterials;
import net.redstone233.tam.item.custom.BlazingFlameSwordItem;
import net.redstone233.tam.item.custom.IceFreezeSwordItem;

public class ModItems {

    public static final Item BLAZING_FLAME_SWORD = register(
            "blazing_flame_sword",
            new BlazingFlameSwordItem(
                    ToolMaterials.NETHERITE, new Item.Settings()
                    .maxDamage(3000000)
            ));

    public static final Item ICE_FREEZE_SWORD = register(
            "ice_freeze_sword",
            new IceFreezeSwordItem(
                    ToolMaterials.NETHERITE, new Item.Settings()
                    .maxDamage(3000000)
            ));
    public static final Item SUSPICIOUS_SUBSTANCE = register(
            "suspicious_substance",
            new Item(new Item.Settings().maxCount(64)
            ));

    public static final ArmorItem GUIDITE_HELMET = register(
            new ArmorItem(
                    ModArmorMaterials.GUIDITE,
                    ArmorItem.Type.HELMET,
                    new Item.Settings()
                            .maxDamage(
                                    ArmorItem.Type.HELMET.getMaxDamage(
                                            ModArmorMaterials.GUIDITE_DURABILITY_MULTIPLIER
                                                                      )
                                      )
            ),
            "guidite_helmet");

    public static final ArmorItem GUIDITE_CHESTPLATE = register(
            new ArmorItem(ModArmorMaterials.GUIDITE,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Settings()
                            .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(
                                    ModArmorMaterials.GUIDITE_DURABILITY_MULTIPLIER))),
            "guidite_chestplate");

    public static final ArmorItem GUIDITE_LEGGINGS = register(
            new ArmorItem(ModArmorMaterials.GUIDITE,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Settings()
                            .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(
                                    ModArmorMaterials.GUIDITE_DURABILITY_MULTIPLIER))),
            "guidite_leggings");


    public static final ArmorItem GUIDITE_BOOTS = register(
            new ArmorItem(ModArmorMaterials.GUIDITE,
                    ArmorItem.Type.BOOTS,
                    new Item.Settings()
                            .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(
                                    ModArmorMaterials.GUIDITE_DURABILITY_MULTIPLIER))),
            "guidite_boots");

    public static void registerItems() {
        TestAnnMod.LOGGER.info("Registering Mod Items for " + TestAnnMod.MOD_ID);
    }

    public static Item register(String id, Item item) {
        return Items.register(Identifier.of(TestAnnMod.MOD_ID, id), item);
    }

    public static ArmorItem register(ArmorItem item, String id) {
        return (ArmorItem) Items.register(Identifier.of(TestAnnMod.MOD_ID, id), item);
    }

    public static Item register(Item item, String id) {
        return register(id, item);
    }
}
