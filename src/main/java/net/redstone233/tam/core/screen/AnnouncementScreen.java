package net.redstone233.tam.core.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.redstone233.tam.TestAnnMod;
import net.redstone233.tam.config.AnnouncementConfig;
import net.redstone233.tam.core.button.ScrollableTextWidget;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnouncementScreen extends Screen {
    private final AnnouncementConfig config;
    private ScrollableTextWidget scrollableText;
    private int tickCount = 0;
    private Identifier iconTexture;
    private Identifier backgroundTexture;

    public AnnouncementScreen(AnnouncementConfig config) {
        super(Text.literal("Server Announcement"));
        this.config = config != null ? config : new AnnouncementConfig();
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int buttonY = this.height - 30;

        // Load icon texture
        loadIconTexture();

        // Load background texture
        loadBackgroundTexture();

        // Create title and subtitle
        createTitleWidgets(centerX);

        // Create announcement content
        MutableText contentText = createAnnouncementContent();

        // Create scrollable text widget
        int contentColor = config.useCustomRGB ? config.contentColor : 0xFFFFFFFF;

        // Ensure color contrast
        if ((contentColor & 0xFFFFFF) == (0xB4303030 & 0xFFFFFF)) {
            contentColor = 0xFFFFFFFF;
        }

        scrollableText = new ScrollableTextWidget(
                centerX - 150, 80, 300, this.height - 150,
                contentText, this.textRenderer, this.client,
                contentColor
        );
        this.addDrawableChild(scrollableText);

        // Create buttons
        createButtons(centerX, buttonWidth, buttonHeight, buttonY);
    }

    private void loadIconTexture() {
        if (config.showIcon && config.iconPath != null && !config.iconPath.isEmpty()) {
            try {
                iconTexture = Identifier.of(config.iconPath);
            } catch (Exception e) {
                TestAnnMod.LOGGER.warn("Failed to load icon texture: {}", config.iconPath, e);
                iconTexture = null;
            }
        }
    }

    private void loadBackgroundTexture() {
        if (config.useCustomAnnouncementBackground &&
                config.announcementBackgroundPath != null &&
                !config.announcementBackgroundPath.isEmpty()) {
            try {
                backgroundTexture = Identifier.of(config.announcementBackgroundPath);
            } catch (Exception e) {
                TestAnnMod.LOGGER.warn("Failed to load background texture: {}", config.announcementBackgroundPath, e);
                backgroundTexture = null;
            }
        }
    }

    private void createTitleWidgets(int centerX) {
        // Calculate title position (considering icon display)
        int titleX = centerX;
        if (config.showIcon && iconTexture != null) {
            int iconAreaWidth = config.iconWidth + config.iconTextSpacing;
            titleX = centerX + iconAreaWidth / 2;
        }

        // Main title
        String mainTitleText = config.mainTitle != null ? config.mainTitle : "Server Announcement";
        MutableText mainTitle = createStyledText(mainTitleText, config.mainTitleColor, config.useCustomRGB)
                .formatted(Formatting.BOLD);

        TextWidget titleWidget = new TextWidget(titleX - 100, 30, 200, 20, mainTitle, this.textRenderer);
        titleWidget.alignCenter();
        this.addDrawableChild(titleWidget);

        // Subtitle
        String subTitleText = config.subTitle != null ? config.subTitle : "Latest News";
        MutableText subTitle = createStyledText(subTitleText, config.subTitleColor, config.useCustomRGB);

        TextWidget subtitleWidget = new TextWidget(titleX - 100, 55, 200, 20, subTitle, this.textRenderer);
        subtitleWidget.alignCenter();
        this.addDrawableChild(subtitleWidget);
    }

    private MutableText createStyledText(String text, int color, boolean useCustomRGB) {
        MutableText component = Text.literal(text);
        if (useCustomRGB) {
            return component.setStyle(Style.EMPTY.withColor(color));
        } else {
            Formatting formatting = findMatchingFormatting(color);
            return component.formatted(formatting);
        }
    }

    private Formatting findMatchingFormatting(int color) {
        int rgbColor = color & 0xFFFFFF;
        for (Formatting formatting : Formatting.values()) {
            if (formatting.isColor() && formatting.getColorValue() != null) {
                int formattingColor = formatting.getColorValue();
                if ((formattingColor & 0xFFFFFF) == rgbColor) {
                    return formatting;
                }
            }
        }
        return Formatting.WHITE;
    }

    /**
     * Create announcement content, supporting vanilla color codes and custom RGB
     */
    private MutableText createAnnouncementContent() {
        MutableText root = Text.empty();
        List<String> contentLines = getContentStrings();

        for (int i = 0; i < contentLines.size(); i++) {
            String line = contentLines.get(i);

            if (line.trim().isEmpty()) {
                root.append(Text.literal("\n"));
                continue;
            }

            MutableText lineText = parseFormattedText(line);
            root.append(lineText);

            if (i < contentLines.size() - 1) {
                root.append(Text.literal("\n"));
            }
        }

        return root;
    }

    private MutableText parseFormattedText(String text) {
        MutableText result = Text.empty();
        Pattern pattern = Pattern.compile("(&#[0-9a-fA-F]{6}|§[0-9a-fk-or]|[^&§]+)");
        Matcher matcher = pattern.matcher(text);
        Style currentStyle = Style.EMPTY;

        while (matcher.find()) {
            String segment = matcher.group();

            if (segment.startsWith("&#")) {
                // Handle custom RGB colors
                try {
                    int rgb = Integer.parseInt(segment.substring(2), 16);
                    currentStyle = currentStyle.withColor(rgb);
                } catch (NumberFormatException e) {
                    TestAnnMod.LOGGER.warn("Invalid RGB color code: {}", segment);
                }
            } else if (segment.startsWith("§")) {
                // Handle vanilla format codes
                Formatting formatting = Formatting.byCode(segment.charAt(1));
                if (formatting != null) {
                    if (formatting == Formatting.RESET) {
                        currentStyle = Style.EMPTY;
                    } else if (formatting.isColor()) {
                        currentStyle = currentStyle.withFormatting(formatting);
                    }
                    // Other formats (bold, italic, etc.) can be added here
                }
            } else {
                // Normal text
                MutableText textSegment = Text.literal(segment).setStyle(currentStyle);
                result.append(textSegment);
            }
        }

        return result;
    }

    private List<String> getContentStrings() {
        List<String> defaultContent = List.of(
                "§aWelcome to our modded server!",
                " ",
                "§eSome reminders:",
                "§f1. Mod is limited to 1.21.7~1.21.8 Fabric",
                "§f2. Mod is currently in beta",
                "§f3. Will continue to update",
                " ",
                "§bMod updates randomly",
                "§cIf you find bugs, please report to the mod author or repository!"
        );

        return config.announcementContent != null && !config.announcementContent.isEmpty()
                ? config.announcementContent
                : defaultContent;
    }

    private void createButtons(int centerX, int buttonWidth, int buttonHeight, int buttonY) {
        // Confirm button
        String confirmText = config.confirmButtonText != null ? config.confirmButtonText : "Confirm";
        Text confirmButtonText = createStyledText(confirmText, 0xFFFFFF, false);

        this.addDrawableChild(ButtonWidget.builder(confirmButtonText, button -> this.close())
                .position(centerX - buttonWidth - 5, buttonY)
                .size(buttonWidth, buttonHeight)
                .build());

        // Submit button
        String submitText = config.submitButtonText != null ? config.submitButtonText : "Submit Report";
        Text submitButtonText = createStyledText(submitText, 0xFFFFFF, false);
        String buttonLink = Objects.requireNonNullElse(config.buttonLink, "https://example.com");

        this.addDrawableChild(ButtonWidget.builder(submitButtonText, button -> openLink(buttonLink))
                .position(centerX + 5, buttonY)
                .size(buttonWidth, buttonHeight)
                .build());
    }

    private void openLink(String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            Util.getOperatingSystem().open(URI.create(url));
        } catch (Exception e) {
            if (this.client != null && this.client.player != null) {
                this.client.player.sendMessage(Text.literal("Unable to open link: " + e.getMessage()), false);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render background
        renderBackground(context);

        // Draw icon
        renderIcon(context);

        // Render other widgets
        super.render(context, mouseX, mouseY, delta);

        // Auto scroll
        handleAutoScroll();
    }

    private void renderBackground(DrawContext context) {
        if (config.useCustomAnnouncementBackground && backgroundTexture != null) {
            try {
                context.drawTexture(backgroundTexture, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
            } catch (Exception e) {
                context.fill(0, 0, this.width, this.height, 0xB4303030);
            }
        } else {
            context.fill(0, 0, this.width, this.height, 0xB4303030);
        }
    }

    private void renderIcon(DrawContext context) {
        if (config.showIcon && iconTexture != null) {
            int iconX = (this.width / 2) - 150 - config.iconWidth - config.iconTextSpacing;
            int iconY = 30;

            try {
                context.drawTexture(iconTexture, iconX, iconY, 0, 0, config.iconWidth, config.iconHeight, config.iconWidth, config.iconHeight);
            } catch (Exception e) {
                TestAnnMod.LOGGER.warn("Failed to draw icon", e);
            }
        }
    }

    private void handleAutoScroll() {
        if (scrollableText != null && tickCount % 2 == 0) {
            double maxScroll = scrollableText.getTotalHeight() - scrollableText.getHeight();
            if (maxScroll > 0) {
                double scrollAmount = scrollableText.getScrollAmount() + (config.scrollSpeed / 20.0);
                if (scrollAmount > maxScroll) scrollAmount = 0;
                scrollableText.setScrollAmount(Math.min(scrollAmount, maxScroll));
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        tickCount++;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return super.shouldCloseOnEsc();
    }

    @Override
    public void close() {
        super.close();
    }
}