package net.redstone233.tam.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.redstone233.tam.config.ConfigManager;
import net.redstone233.tam.manager.AnnouncementManager;
import net.redstone233.tam.network.NetworkHandler;

import java.util.List;
import java.util.Objects;

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
                        .then(literal("ponder")
                                .then(argument("shown", BoolArgumentType.bool())
                                        .executes(
                                                run -> showPonderScreen(
                                                        run, BoolArgumentType.getBool(run, "shown")
                                                )
                                        )
                                )
                        )
                        .then(literal("player")
                                .then(literal("reset")
                                        .executes(DebugCommands::resetPlayerAnnouncement)
                                        .then(argument("player", EntityArgumentType.player())
                                                .executes(DebugCommands::resetOtherPlayerAnnouncement)))
                                .then(literal("info")
                                        .executes(DebugCommands::showPlayerAnnouncementInfo))
                        )
                        .then(literal("hash")
                                .then(literal("show")
                                        .executes(DebugCommands::showHashInfo))
                                .then(literal("forceUpdate")
                                        .executes(DebugCommands::forceUpdateHash))
                        )
                )
        );
    }

    private static int showPonderScreen(CommandContext<ServerCommandSource> context, boolean isShow) {
        ServerCommandSource source = context.getSource();

        // 直接设置值，不需要条件判断
        ConfigManager.setPonderScreen(isShow);
        ConfigManager.saveConfig();

        source.sendFeedback(() -> Text.literal("§aPonder界面显示已" + (isShow ? "开启" : "关闭")), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int showAnnouncement(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null) {
            source.sendError(Text.literal("该命令只能由玩家执行"));
            return 0;
        }

        // 先显示调试信息
        String debugInfo = AnnouncementManager.getDebugInfo(player);
        source.sendFeedback(() -> Text.literal("§e公告状态: " + debugInfo), false);

        // 强制显示公告
        AnnouncementManager.forceShowToPlayer(player);
        source.sendFeedback(() -> Text.literal("§a已向您显示公告"), false);

        return Command.SINGLE_SUCCESS;
    }

    private static int showAnnouncementToAll(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        if (source.getServer() == null) {
            source.sendError(Text.literal("无法获取服务器实例"));
            return 0;
        }

        NetworkHandler.sendToAllPlayers(source.getServer());
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

        ConfigManager.setAnnouncementContent(List.of("公告内容已清空"));
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
        String lastHash = ConfigManager.getLastDisplayedHash();
        boolean hasChanged = ConfigManager.hasConfigChanged();

        source.sendFeedback(() -> Text.literal("§6=== 公告配置信息 ==="), false);
        source.sendFeedback(() -> Text.literal("§e当前配置哈希: §f" + hash), false);
        source.sendFeedback(() -> Text.literal("§e上次显示哈希: §f" + lastHash), false);
        source.sendFeedback(() -> Text.literal("§e配置是否变化: §f" + hasChanged), false);
        source.sendFeedback(() -> Text.literal("§e主标题: §f" + ConfigManager.getMainTitle()), false);
        source.sendFeedback(() -> Text.literal("§e副标题: §f" + ConfigManager.getSubTitle()), false);
        source.sendFeedback(() -> Text.literal("§e内容行数: §f" + ConfigManager.getAnnouncementContent().size()), false);
        source.sendFeedback(() -> Text.literal("§e显示图标: §f" + ConfigManager.shouldShowIcon()), false);
        source.sendFeedback(() -> Text.literal("§e自定义背景: §f" + ConfigManager.useCustomAnnouncementBackground()), false);
        source.sendFeedback(() -> Text.literal("§ePonder界面: §f" + ConfigManager.showPonderScreen()), false);

        // 显示前3行内容作为预览
        List<String> content = ConfigManager.getAnnouncementContent();
        source.sendFeedback(() -> Text.literal("§e内容预览:"), false);
        for (int i = 0; i < Math.min(3, content.size()); i++) {
            final String line = content.get(i);
            source.sendFeedback(() -> Text.literal("§f  " + line), false);
        }
        if (content.size() > 3) {
            source.sendFeedback(() -> Text.literal("§e  ... 还有 " + (content.size() - 3) + " 行"), false);
        }

        return Command.SINGLE_SUCCESS;
    }

    /**
     * 重置当前玩家的公告观看记录
     */
    private static int resetPlayerAnnouncement(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null) {
            source.sendError(Text.literal("该命令只能由玩家执行"));
            return 0;
        }

        AnnouncementManager.resetPlayerAnnouncement(player);
        source.sendFeedback(() -> Text.literal("§a已重置您的公告观看记录，下次进入将重新显示公告"), false);
        return Command.SINGLE_SUCCESS;
    }

    /**
     * 重置其他玩家的公告观看记录
     */
    private static int resetOtherPlayerAnnouncement(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(context, "player");

        AnnouncementManager.resetPlayerAnnouncement(targetPlayer);
        source.sendFeedback(() -> Text.literal("§a已重置玩家 " + targetPlayer.getGameProfile().getName() + " 的公告观看记录"), false);

        // 通知目标玩家
        if (!Objects.equals(source.getPlayer(), targetPlayer)) {
            targetPlayer.sendMessage(Text.literal("§e管理员已重置您的公告观看记录，下次进入将重新显示公告"));
        }

        return Command.SINGLE_SUCCESS;
    }

    /**
     * 显示当前玩家的公告调试信息
     */
    private static int showPlayerAnnouncementInfo(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player == null) {
            source.sendError(Text.literal("该命令只能由玩家执行"));
            return 0;
        }

        String debugInfo = AnnouncementManager.getDebugInfo(player);
        source.sendFeedback(() -> Text.literal("§6=== 玩家公告状态 ==="), false);
        source.sendFeedback(() -> Text.literal("§e" + debugInfo), false);

        return Command.SINGLE_SUCCESS;
    }

    /**
     * 显示哈希相关信息
     */
    private static int showHashInfo(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        String currentHash = ConfigManager.getConfigHash();
        String lastDisplayedHash = ConfigManager.getLastDisplayedHash();
        boolean hasChanged = ConfigManager.hasConfigChanged();

        source.sendFeedback(() -> Text.literal("§6=== 哈希信息 ==="), false);
        source.sendFeedback(() -> Text.literal("§e当前配置哈希: §f" + currentHash), false);
        source.sendFeedback(() -> Text.literal("§e上次显示哈希: §f" + lastDisplayedHash), false);
        source.sendFeedback(() -> Text.literal("§e配置是否变化: §f" + hasChanged), false);
        source.sendFeedback(() -> Text.literal("§e稳定哈希算法: §a已启用"), false);

        return Command.SINGLE_SUCCESS;
    }

    /**
     * 强制更新哈希记录
     */
    private static int forceUpdateHash(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        String currentHash = ConfigManager.getConfigHash();
        ConfigManager.setLastDisplayedHash(currentHash);
        ConfigManager.saveConfig();

        source.sendFeedback(() -> Text.literal("§a已强制更新哈希记录为当前配置"), false);
        source.sendFeedback(() -> Text.literal("§e新哈希值: §f" + currentHash), false);

        return Command.SINGLE_SUCCESS;
    }
}