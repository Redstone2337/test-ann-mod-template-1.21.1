package net.redstone233.tam.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.redstone233.tam.core.brewing.CustomBrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 这是一个针对BrewingRecipeRegistry类的Mixin类，用于添加自定义酿造配方功能
 */
@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {

    /**
     * 检查是否存在自定义酿造配方的方法注入
     * @param input 输入物品堆栈
     * @param ingredient 酿造原料物品堆栈
     * @param cir 回调信息，用于控制方法返回值
     */
    @Inject(method = "hasRecipe", at = @At("HEAD"), cancellable = true)
    private static void checkCustomRecipes(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        // 检查是否存在自定义酿造配方
        if (CustomBrewingRecipeRegistry.isCustomBrewingRecipe(input, ingredient.getItem())) {
            // 如果存在自定义配方，直接返回true
            cir.setReturnValue(true);
        }
    }

    /**
     * 获取自定义酿造配方输出物品的方法注入
     * @param ingredient 酿造原料物品堆栈
     * @param input 输入物品堆栈
     * @param cir 回调信息，用于控制方法返回值
     */
    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    private static void getCustomOutput(ItemStack ingredient, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
        // 获取自定义酿造配方的输出物品
        ItemStack customOutput = CustomBrewingRecipeRegistry.getCustomBrewingOutput(input, ingredient.getItem());
        // 如果输出物品不为空，则返回该物品
        if (!customOutput.isEmpty()) {
            cir.setReturnValue(customOutput);
        }
    }
}