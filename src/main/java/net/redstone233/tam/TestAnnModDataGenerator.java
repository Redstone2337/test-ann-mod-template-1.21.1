package net.redstone233.tam;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.redstone233.tam.data.ModChineseLanguageProvider;
import net.redstone233.tam.data.ModEnchantmentGenerator;
import net.redstone233.tam.data.ModEnglishLanguageProvider;
import net.redstone233.tam.data.ModModelsProvider;

public class TestAnnModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModEnglishLanguageProvider::new);
        pack.addProvider(ModChineseLanguageProvider::new);
        pack.addProvider(ModModelsProvider::new);
        pack.addProvider(ModEnchantmentGenerator::new);
	}
}
