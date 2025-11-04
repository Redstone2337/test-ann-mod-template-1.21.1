package net.redstone233.tam.core.brewing;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CustomBrewingRecipeRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomBrewingRecipeRegistry.class);
    private static final List<CustomBrewingRecipe> CUSTOM_RECIPES = new ArrayList<>();

    public static void registerBrewingRecipe(ItemStack input, Item ingredient, ItemStack output) {
        CUSTOM_RECIPES.add(new CustomBrewingRecipe(input, ingredient, output));
        LOGGER.debug("已添加自定义酿造配方: {} + {} -> {}",
                Registries.ITEM.getId(input.getItem()),
                Registries.ITEM.getId(ingredient),
                Registries.ITEM.getId(output.getItem()));
    }

    public static boolean isCustomBrewingRecipe(ItemStack input, Item ingredient) {
        for (CustomBrewingRecipe recipe : CUSTOM_RECIPES) {
            if (recipe.matches(input, ingredient)) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getCustomBrewingOutput(ItemStack input, Item ingredient) {
        for (CustomBrewingRecipe recipe : CUSTOM_RECIPES) {
            if (recipe.matches(input, ingredient)) {
                return recipe.getOutput().copy();
            }
        }
        return ItemStack.EMPTY;
    }

    public static List<CustomBrewingRecipe> getCustomRecipes() {
        return new ArrayList<>(CUSTOM_RECIPES);
    }

    public static int getRecipeCount() {
        return CUSTOM_RECIPES.size();
    }

    public static void clearRecipes() {
        CUSTOM_RECIPES.clear();
        LOGGER.info("已清空所有自定义酿造配方");
    }

    public static class CustomBrewingRecipe {
        private final ItemStack input;
        private final Item ingredient;
        private final ItemStack output;

        public CustomBrewingRecipe(ItemStack input, Item ingredient, ItemStack output) {
            this.input = input;
            this.ingredient = ingredient;
            this.output = output;
        }

        public boolean matches(ItemStack inputStack, Item ingredientItem) {
            // 比较物品类型
            if (inputStack.getItem() != input.getItem() || ingredientItem != ingredient) {
                return false;
            }

            // 比较药水组件（如果有）
            PotionContentsComponent recipePotion = input.get(DataComponentTypes.POTION_CONTENTS);
            PotionContentsComponent inputPotion = inputStack.get(DataComponentTypes.POTION_CONTENTS);

            if (recipePotion != null) {
                if (inputPotion == null) return false;

                // 比较药水效果
                if (!arePotionsEqual(recipePotion, inputPotion)) {
                    return false;
                }
            } else {
                // 如果配方没有指定药水组件，但输入有，也不匹配
                if (inputPotion != null) return false;
            }

            // 可以在这里添加其他组件的比较
            // 例如自定义名称、Lore等

            return true;
        }

        private boolean arePotionsEqual(PotionContentsComponent a, PotionContentsComponent b) {
            // 比较药水类型
            if (a.potion().isPresent() && b.potion().isPresent()) {
                if (!a.potion().get().equals(b.potion().get())) {
                    return false;
                }
            } else if (a.potion().isPresent() != b.potion().isPresent()) {
                return false;
            }

            // 比较自定义效果
            if (a.customEffects().size() != b.customEffects().size()) {
                return false;
            }

            for (int i = 0; i < a.customEffects().size(); i++) {
                if (!areStatusEffectsEqual(a.customEffects().get(i), b.customEffects().get(i))) {
                    return false;
                }
            }

            return true;
        }

        private boolean areStatusEffectsEqual(net.minecraft.entity.effect.StatusEffectInstance a, net.minecraft.entity.effect.StatusEffectInstance b) {
            return a.getEffectType().equals(b.getEffectType()) &&
                    a.getDuration() == b.getDuration() &&
                    a.getAmplifier() == b.getAmplifier() &&
                    a.isAmbient() == b.isAmbient() &&
                    a.shouldShowParticles() == b.shouldShowParticles() &&
                    a.shouldShowIcon() == b.shouldShowIcon();
        }

        public ItemStack getOutput() {
            return output;
        }

        public ItemStack getInput() {
            return input;
        }

        public Item getIngredient() {
            return ingredient;
        }

        @Override
        public String toString() {
            return String.format("CustomBrewingRecipe[input=%s, ingredient=%s, output=%s]",
                    Registries.ITEM.getId(input.getItem()),
                    Registries.ITEM.getId(ingredient),
                    Registries.ITEM.getId(output.getItem()));
        }
    }
}