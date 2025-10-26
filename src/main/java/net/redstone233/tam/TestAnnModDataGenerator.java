package net.redstone233.tam;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.redstone233.tam.data.ModChineseLanguageProvider;
import net.redstone233.tam.data.ModEnglishLanguageProvider;
import net.redstone233.tam.data.ModModelsProvider;
import net.redstone233.tam.enchantment.ModEnchantments;

public class TestAnnModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModEnglishLanguageProvider::new);
        pack.addProvider(ModChineseLanguageProvider::new);
        pack.addProvider(ModModelsProvider::new);
	}

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, ModEnchantments::bootstrap);
        DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
    }
}
