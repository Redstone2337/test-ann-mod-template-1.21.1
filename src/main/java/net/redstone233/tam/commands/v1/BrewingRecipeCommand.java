package net.redstone233.tam.commands.v1;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.redstone233.tam.core.brewing.CustomPotionRegistry;
import net.redstone233.tam.core.brewing.EnhancedBrewingRecipeParser;

import static net.minecraft.server.command.CommandManager.literal;

/**
 * 酿造配方命令
 */
public class BrewingRecipeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, EnhancedBrewingRecipeParser parser) {
        dispatcher.register(
                literal("brewing")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(literal("reload")
                                .executes(context -> reloadRecipes(context, parser)))
                        .then(literal("info")
                                .executes(context -> showInfo(context, parser)))
                        .then(literal("potions")
                                .executes(BrewingRecipeCommand::listPotions))
        );
    }

    private static int reloadRecipes(CommandContext<ServerCommandSource> context, EnhancedBrewingRecipeParser parser) {
        var source = context.getSource();

        // 重新加载配方
        parser.reloadBrewingRecipes();

        int count = parser.getRecipeCount();
        source.sendMessage(Text.literal("已重新加载 " + count + " 个酿造配方").formatted(Formatting.GREEN));

        return count;
    }

    private static int showInfo(CommandContext<ServerCommandSource> context, EnhancedBrewingRecipeParser parser) {
        var source = context.getSource();

        int recipeCount = parser.getRecipeCount();
        var customPotions = CustomPotionRegistry.getCustomPotions();

        source.sendMessage(Text.literal("=== TAM 酿造系统信息 ===").formatted(Formatting.GOLD));
        source.sendMessage(Text.literal("已加载配方: " + recipeCount).formatted(Formatting.WHITE));
        source.sendMessage(Text.literal("自定义药水: " + customPotions.size()).formatted(Formatting.WHITE));

        return 1;
    }

    private static int listPotions(CommandContext<ServerCommandSource> context) {
        var source = context.getSource();
        var customPotions = CustomPotionRegistry.getCustomPotions();

        if (customPotions.isEmpty()) {
            source.sendMessage(Text.literal("没有注册的自定义药水").formatted(Formatting.YELLOW));
            return 0;
        }

        source.sendMessage(Text.literal("自定义药水列表:").formatted(Formatting.GREEN));
        customPotions.forEach((key, potion) -> {
            String potionName = potion.getKey().map(k -> k.getValue().toString()).orElse("unknown");
            source.sendMessage(Text.literal(" - " + key + " → " + potionName).formatted(Formatting.WHITE));
        });

        return customPotions.size();
    }
}