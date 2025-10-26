package net.redstone233.tam.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.ArmorItem;
import net.redstone233.tam.item.ModItems;

public class ModModelsProvider extends FabricModelProvider {
    public ModModelsProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.ICE_FREEZE_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.BLAZING_FLAME_SWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.SUSPICIOUS_SUBSTANCE, Models.GENERATED);
        itemModelGenerator.registerArmor(ModItems.GUIDITE_HELMET);
        itemModelGenerator.registerArmor(ModItems.GUIDITE_CHESTPLATE);
        itemModelGenerator.registerArmor(ModItems.GUIDITE_LEGGINGS);
        itemModelGenerator.registerArmor(ModItems.GUIDITE_BOOTS);
    }
}
