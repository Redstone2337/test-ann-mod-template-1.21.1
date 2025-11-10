package net.redstone233.tam.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.redstone233.tam.core.brewing.BrewingRecipeParser;

import java.util.Objects;

/**
 * 酿造配方命令 - 需要传入 BrewingRecipeParser 实例
 */
public class BrewingRecipeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, BrewingRecipeParser parser) {
        dispatcher.register(
                CommandManager.literal("brewingrecipes")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> listRecipes(context, parser))
                        .then(CommandManager.literal("reload")
                                .executes(context -> reloadRecipes(context, parser)))
        );
    }

    private static int listRecipes(CommandContext<ServerCommandSource> context, BrewingRecipeParser parser) {
        var recipes = parser.getRecipes();
        var source = context.getSource();

        if (recipes.isEmpty()) {
            source.sendMessage(Text.literal("没有注册的自定义酿造配方").formatted(Formatting.YELLOW));
            return 0;
        }

        source.sendMessage(Text.literal("已注册的自定义酿造配方 (" + recipes.size() + " 个):").formatted(Formatting.GREEN));

        for (int i = 0; i < recipes.size(); i++) {
            var recipe = recipes.get(i);
            ItemStack input = recipe.input();
            var ingredient = recipe.ingredient();
            ItemStack output = recipe.output();

            String inputName = getItemDisplayName(input);
            String ingredientName = Registries.ITEM.getId(ingredient).toString();
            String outputName = getItemDisplayName(output);

            Text message = Text.literal(String.format("%d. %s + %s -> %s",
                    i + 1, inputName, ingredientName, outputName));

            source.sendMessage(message);
        }

        return recipes.size();
    }

    private static int reloadRecipes(CommandContext<ServerCommandSource> context, BrewingRecipeParser parser) {
        var source = context.getSource();

        // 重新加载配方
        parser.reloadBrewingRecipes();

        int count = parser.getRecipeCount();
        source.sendMessage(Text.literal("已重新加载 " + count + " 个酿造配方").formatted(Formatting.GREEN));

        return count;
    }

    private static String getItemDisplayName(ItemStack stack) {
        String itemId = Registries.ITEM.getId(stack.getItem()).toString();

        // 如果有自定义名称，显示自定义名称
        if (stack.contains(net.minecraft.component.DataComponentTypes.CUSTOM_NAME)) {
            Text customName = stack.get(net.minecraft.component.DataComponentTypes.CUSTOM_NAME);
            if (customName != null) {
                return customName.getString() + " (" + itemId + ")";
            }
        }

        // 如果有药水效果，显示药水效果
        if (stack.contains(net.minecraft.component.DataComponentTypes.POTION_CONTENTS)) {
            var potion = stack.get(net.minecraft.component.DataComponentTypes.POTION_CONTENTS);
            if (potion != null && potion.potion().isPresent()) {
                String potionId = Objects.requireNonNull(Registries.POTION.getId(potion.potion().get().value())).toString();
                return itemId + "[" + potionId + "]";
            }
        }

        return itemId;
    }
}