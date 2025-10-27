package net.redstone233.tam.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.redstone233.tam.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends FabricRecipeProvider {
    public ModRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.GUIDITE_HELMET,1)
                .pattern("###")
                .pattern("# #")
                .input('#', ModItems.SUSPICIOUS_SUBSTANCE)
                .criterion("has_suspicious_substance",
                        FabricRecipeProvider.conditionsFromItem(ModItems.SUSPICIOUS_SUBSTANCE))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.GUIDITE_CHESTPLATE,1)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .input('#', ModItems.SUSPICIOUS_SUBSTANCE)
                .criterion("has_suspicious_substance",
                        FabricRecipeProvider.conditionsFromItem(ModItems.SUSPICIOUS_SUBSTANCE))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.GUIDITE_LEGGINGS,1)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.SUSPICIOUS_SUBSTANCE)
                .criterion("has_suspicious_substance",
                        FabricRecipeProvider.conditionsFromItem(ModItems.SUSPICIOUS_SUBSTANCE))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.GUIDITE_BOOTS,1)
                .pattern("# #")
                .pattern("# #")
                .input('#', ModItems.SUSPICIOUS_SUBSTANCE)
            .criterion("has_suspicious_substance",
                    FabricRecipeProvider.conditionsFromItem(ModItems.SUSPICIOUS_SUBSTANCE))
                .offerTo(recipeExporter);
    }
}
