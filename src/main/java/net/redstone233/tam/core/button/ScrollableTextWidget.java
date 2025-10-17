package net.redstone233.tam.core.button;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.redstone233.tam.TestAnnMod;

import java.util.ArrayList;
import java.util.List;

public class ScrollableTextWidget extends ClickableWidget {
    private final MinecraftClient client;
    private final TextRenderer textRenderer;
    private final List<Text> textLines;
    private double scrollAmount;
    private boolean scrolling;
    private int totalHeight;
    private final int scrollbarWidth = 6;
    private final int scrollbarPadding = 2;
    private final int textColor;
    private int visibleLines;

    public ScrollableTextWidget(int x, int y, int width, int height, Text message, TextRenderer textRenderer, MinecraftClient client, int color) {
        super(x, y, width, height, message);
        this.client = client;
        this.textRenderer = textRenderer;
        this.textColor = color;
        this.textLines = new ArrayList<>();
        this.scrollAmount = 0;
        this.totalHeight = 0;
        this.visibleLines = 0;
        updateTextLines();
    }

    public void updateTextLines() {
        textLines.clear();

        try {
            if (getMessage() == null) {
                textLines.add(Text.literal("No announcement content"));
            } else {
                // Process multi-line text
                String plainText = getMessage().getString();
                String[] lines = plainText.split("\n");
                for (String line : lines) {
                    textLines.add(parseFormattingCodes(line));
                }
            }

            // Calculate total height and visible lines
            totalHeight = textLines.size() * (textRenderer.fontHeight + 2);
            visibleLines = (height - scrollbarPadding * 2) / (textRenderer.fontHeight + 2);

        } catch (Exception e) {
            TestAnnMod.LOGGER.error("Text processing failed", e);
            textLines.add(Text.literal("Text rendering error"));
            totalHeight = textRenderer.fontHeight;
            visibleLines = 1;
        }
    }

    /**
     * Parse strings containing Minecraft formatting codes
     */
    private Text parseFormattingCodes(String text) {
        MutableText result = Text.empty();
        StringBuilder currentText = new StringBuilder();
        Formatting currentFormatting = null;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '§' && i + 1 < text.length()) {
                char codeChar = text.charAt(i + 1);
                Formatting formatting = Formatting.byCode(codeChar);

                if (formatting != null) {
                    if (!currentText.isEmpty()) {
                        MutableText segment = Text.literal(currentText.toString());
                        if (currentFormatting != null) {
                            segment = segment.setStyle(Style.EMPTY.withFormatting(currentFormatting));
                        }
                        result.append(segment);
                        currentText.setLength(0);
                    }

                    currentFormatting = formatting.isColor() ? formatting : currentFormatting;
                    i++;
                } else {
                    currentText.append(c);
                }
            } else {
                currentText.append(c);
            }
        }

        if (!currentText.isEmpty()) {
            MutableText segment = Text.literal(currentText.toString());
            if (currentFormatting != null) {
                segment = segment.setStyle(Style.EMPTY.withFormatting(currentFormatting));
            }
            result.append(segment);
        }

        return result;
    }

    @Override
    public void setMessage(Text message) {
        super.setMessage(message);
        updateTextLines();
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, Text.translatable("narration.scrollable_text", this.getMessage()));
        builder.put(NarrationPart.USAGE, Text.translatable("narration.scrollable_text.usage"));
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw background
        context.fill(getX(), getY(), getX() + width, getY() + height, 0x80404040);

        // Draw border
        context.fill(getX(), getY(), getX() + width, getY() + 1, 0xFF808080);
        context.fill(getX(), getY() + height - 1, getX() + width, getY() + height, 0xFF808080);
        context.fill(getX(), getY(), getX() + 1, getY() + height, 0xFF808080);
        context.fill(getX() + width - 1, getY(), getX() + width, getY() + height, 0xFF808080);

        // Enable scissor
        int clipX = getX() + scrollbarPadding;
        int clipY = getY() + scrollbarPadding;
        int clipWidth = width - scrollbarWidth - scrollbarPadding * 2;
        int clipHeight = height - scrollbarPadding * 2;

        context.enableScissor(clipX, clipY, clipX + clipWidth, clipY + clipHeight);

        // Draw text
        int lineHeight = textRenderer.fontHeight + 2;
        int startLine = (int) (scrollAmount / lineHeight);
        int endLine = Math.min(startLine + visibleLines + 1, textLines.size());

        int yOffset = getY() + scrollbarPadding - (int) (scrollAmount % lineHeight);
        for (int i = startLine; i < endLine; i++) {
            Text line = textLines.get(i);
            if (yOffset + textRenderer.fontHeight >= getY() && yOffset <= getY() + height) {
                context.drawText(textRenderer, line, getX() + scrollbarPadding, yOffset, textColor, false);
            }
            yOffset += lineHeight;
        }

        // Disable scissor
        context.disableScissor();

        // Draw scrollbar
        drawScrollbar(context);
    }

    private void drawScrollbar(DrawContext context) {
        if (totalHeight > height) {
            int scrollbarHeight = Math.max((int) ((float) height * height / totalHeight), 20);
            int maxScroll = totalHeight - height;

            int scrollbarY = getY() + (int) ((scrollAmount / maxScroll) * (height - scrollbarHeight));
            scrollbarY = MathHelper.clamp(scrollbarY, getY(), getY() + height - scrollbarHeight);

            // Scrollbar background
            context.fill(getX() + width - scrollbarWidth, getY(),
                    getX() + width, getY() + height,
                    0x55AAAAAA);

            // Scrollbar thumb
            context.fill(getX() + width - scrollbarWidth + 1, scrollbarY,
                    getX() + width - 1, scrollbarY + scrollbarHeight,
                    0xFF888888);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.visible && button == 0) {
            // Check if scrollbar area was clicked
            if (mouseX >= getX() + width - scrollbarWidth && mouseX <= getX() + width) {
                scrolling = true;

                // Click scrollbar to jump to position
                if (totalHeight > height) {
                    double relativeY = mouseY - getY();
                    double maxScroll = totalHeight - height;
                    scrollAmount = (relativeY / height) * maxScroll;
                    scrollAmount = MathHelper.clamp(scrollAmount, 0, maxScroll);
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            scrolling = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.visible && this.scrolling && totalHeight > height) {
            double maxScroll = totalHeight - height;
            double relativeY = mouseY - getY();
            scrollAmount = (relativeY / height) * maxScroll;
            scrollAmount = MathHelper.clamp(scrollAmount, 0, maxScroll);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.visible && totalHeight > height) {
            double maxScroll = totalHeight - height;
            scrollAmount = MathHelper.clamp(scrollAmount - verticalAmount * 20, 0, maxScroll);
            return true;
        }
        return false;
    }
    // Getter and Setter methods
    public double getScrollAmount() {
        return scrollAmount;
    }

    public void setScrollAmount(double scrollAmount) {
        this.scrollAmount = scrollAmount;
    }

    public int getTotalHeight() {
        return totalHeight;
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    public int getColor() {
        return textColor;
    }

    public int getVisibleLines() {
        return visibleLines;
    }

    public List<Text> getTextLines() {
        return textLines;
    }
}
