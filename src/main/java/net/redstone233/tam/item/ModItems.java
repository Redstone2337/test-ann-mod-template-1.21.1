package net.redstone233.tam.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;
import net.redstone233.tam.item.custom.BlazingFlameSwordItem;
import net.redstone233.tam.item.custom.IceFreezeSwordItem;

public class ModItems {

    public static final Item BLAZING_FLAME_SWORD = register(
            "blazing_flame_sword",
            new BlazingFlameSwordItem(ToolMaterials.NETHERITE, new Item.Settings()
                    .maxDamage(3000000)
            ));

    public static final Item ICE_FREEZE_SWORD = register(
            "ice_freeze_sword",
            new IceFreezeSwordItem(ToolMaterials.NETHERITE, new Item.Settings()
                    .maxDamage(3000000)
            ));

    public static void registerItems() {
        TestAnnMod.LOGGER.info("Registering Mod Items for " + TestAnnMod.MOD_ID);
    }

    public static Item register(String id, Item item) {
        return Items.register(Identifier.of(TestAnnMod.MOD_ID,id), item);
    }
}
