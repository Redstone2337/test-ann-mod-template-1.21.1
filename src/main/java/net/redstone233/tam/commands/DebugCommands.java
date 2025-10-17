package net.redstone233.tam.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.redstone233.tam.config.ConfigManager;
import net.redstone233.tam.manager.AnnouncementManager;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.server.command.CommandManager.*;

public class DebugCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("announcement")
                .requires(source -> source.hasPermissionLevel(2)) // OP权限要求
                .then(literal("debug")
                        .then(literal("show")
                                .executes(DebugCommands::showAnnouncement))
                        .then(literal("showToAll")
                                .executes(DebugCommands::showAnnouncementToAll))
                        .then(literal("reload")
                                .executes(DebugCommands::reloadConfig))
                        .then(literal("setTitle")
                                .then(argument("title", StringArgumentType.greedyString())
                                        .executes(DebugCommands::setMainTitle)))
                        .then(literal("setSubTitle")
                                .then(argument("subtitle", StringArgumentType.greedyString())
                                        .executes(DebugCommands::setSubTitle)))
                        .then(literal("addContent")
                                .then(argument("content", StringArgumentType.greedyString())
                                        .executes(DebugCommands::addContent)))
                        .then(literal("clearContent")
                                .executes(DebugCommands::clearContent))
                        .then(literal("setButtonText")
                                .then(argument("confirm", StringArgumentType.string())
                                        .then(argument("submit", StringArgumentType.string())
                                                .executes(DebugCommands::setButtonText))))
                        .then(literal("setScrollSpeed")
                                .then(argument("speed", IntegerArgumentType.integer(1, 10))
                                        .executes(DebugCommands::setScrollSpeed)))
                        .then(literal("toggleIcon")
                                .executes(DebugCommands::toggleIcon))
                        .then(literal("toggleBackground")
                                .executes(DebugCommands::toggleBackground))
                        .then(literal("reset")
                                .executes(DebugCommands::resetConfig))
                        .then(literal("info")
                                .executes(DebugCommands::showConfigInfo))
                ));
    }

    private static int showAnnouncement(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        if (source.getPlayer() == null) {
            source.sendError(Text.literal("该命令只能由玩家执行"));
            return 0;
        }

        AnnouncementManager.forceShowToPlayer(source.getPlayer());
        source.sendFeedback(() -> Text.literal("§a已向您显示公告"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int showAnnouncementToAll(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        if (source.getServer() == null) {
            source.sendError(Text.literal("无法获取服务器实例"));
            return 0;
        }

        net.redstone233.tam.network.NetworkHandler.sendToAllPlayers(source.getServer());
        source.sendFeedback(() -> Text.literal("§a已向所有在线玩家显示公告"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int reloadConfig(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        // 重新加载配置
        ConfigManager.reloadConfig();
        source.sendFeedback(() -> Text.literal("§a配置已重新加载"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int setMainTitle(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String title = StringArgumentType.getString(context, "title");

        ConfigManager.setMainTitle(title);
        source.sendFeedback(() -> Text.literal("§a主标题已设置为: " + title), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int setSubTitle(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String subtitle = StringArgumentType.getString(context, "subtitle");

        ConfigManager.setSubTitle(subtitle);
        source.sendFeedback(() -> Text.literal("§a副标题已设置为: " + subtitle), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int addContent(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String content = StringArgumentType.getString(context, "content");

        // 获取当前内容并添加新行
        List<String> currentContent = ConfigManager.getAnnouncementContent();
        currentContent.add(content);
        ConfigManager.setAnnouncementContent(currentContent);

        source.sendFeedback(() -> Text.literal("§a已添加公告内容: " + content), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int clearContent(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        ConfigManager.setAnnouncementContent(Arrays.asList("公告内容已清空"));
        source.sendFeedback(() -> Text.literal("§a公告内容已清空"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int setButtonText(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String confirm = StringArgumentType.getString(context, "confirm");
        String submit = StringArgumentType.getString(context, "submit");

        ConfigManager.setConfirmButtonText(confirm);
        ConfigManager.setSubmitButtonText(submit);

        source.sendFeedback(() -> Text.literal("§a按钮文本已设置 - 确认: " + confirm + ", 提交: " + submit), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int setScrollSpeed(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        int speed = IntegerArgumentType.getInteger(context, "speed");

        ConfigManager.setScrollSpeed(speed);
        source.sendFeedback(() -> Text.literal("§a滚动速度已设置为: " + speed), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int toggleIcon(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        boolean current = ConfigManager.shouldShowIcon();

        ConfigManager.setShowIcon(!current);
        source.sendFeedback(() -> Text.literal("§a图标显示已" + (!current ? "开启" : "关闭")), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int toggleBackground(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        boolean current = ConfigManager.useCustomAnnouncementBackground();

        ConfigManager.setUseCustomAnnouncementBackground(!current);
        source.sendFeedback(() -> Text.literal("§a自定义背景已" + (!current ? "开启" : "关闭")), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int resetConfig(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        ConfigManager.resetToDefaults();
        source.sendFeedback(() -> Text.literal("§a配置已重置为默认值"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int showConfigInfo(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        String hash = ConfigManager.getConfigHash();
        boolean hasChanged = ConfigManager.hasConfigChanged();

        source.sendFeedback(() -> Text.literal("§6=== 公告配置信息 ==="), false);
        source.sendFeedback(() -> Text.literal("§e配置哈希: §f" + hash), false);
        source.sendFeedback(() -> Text.literal("§e配置是否变化: §f" + hasChanged), false);
        source.sendFeedback(() -> Text.literal("§e主标题: §f" + ConfigManager.getMainTitle()), false);
        source.sendFeedback(() -> Text.literal("§e内容行数: §f" + ConfigManager.getAnnouncementContent().size()), false);
        source.sendFeedback(() -> Text.literal("§e显示图标: §f" + ConfigManager.shouldShowIcon()), false);
        source.sendFeedback(() -> Text.literal("§e自定义背景: §f" + ConfigManager.useCustomAnnouncementBackground()), false);

        return Command.SINGLE_SUCCESS;
    }
}