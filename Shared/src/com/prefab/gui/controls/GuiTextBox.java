package com.prefab.gui.controls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Note: This is a copy of the Minecraft:EditBox component.
 * Otherwise, seen as the "widget/text_field"
 * <p>
 * The major difference between this class and the main EditBox is that this one allows for different text to be drawn.
 * I.E. Not Drawing the text shadow if we don't want too, or including it if we do!
 * <p>
 * Also, this shows a WHITE background instead of the standard black background.
 */
public class GuiTextBox extends AbstractWidget implements Renderable, GuiEventListener {
    private final net.minecraft.client.gui.Font font;
    public int backgroundColor;
    public boolean drawsTextShadow;

    public String suggestion;
    private String value;
    private int maxLength;
    private int frame;
    private int textX;
    private int textY;
    private boolean bordered;
    private boolean canLoseFocus;
    private boolean isEditable;
    private boolean shiftPressed;
    private int displayPos;
    private int cursorPos;
    private int highlightPos;
    private int textColor;
    private int textColorUneditable;
    private Consumer<String> responder;
    private Predicate<String> filter;
    private BiFunction<String, Integer, FormattedCharSequence> formatter;
    @Nullable
    private Component hint;
    private long focusedTime;

    public GuiTextBox(Font font, int x, int y, int width, int height, Component text) {
        this(font, x, y, width, height, null, text);
    }

    public GuiTextBox(Font textRenderer, int x, int y, int width, int height, GuiTextBox copyFrom, Component text) {
        super(x, y, width, height, text);
        this.value = "";
        this.maxLength = 32;
        this.bordered = true;
        this.canLoseFocus = true;
        this.isEditable = true;
        this.textColor = 14737632;
        this.textColorUneditable = 7368816;
        this.backgroundColor = Color.WHITE.getRGB();
        this.filter = Objects::nonNull;

        this.formatter = (string, integer) -> {
            return FormattedCharSequence.forward(string, Style.EMPTY);
        };

        this.font = textRenderer;

        if (copyFrom != null) {
            this.setValue(copyFrom.getValue());
        }

        this.updateTextPosition();
    }

    public void setResponder(Consumer<String> rssponder) {
        this.responder = rssponder;
    }

    public void setFormatter(BiFunction<String, Integer, FormattedCharSequence> textFormatter) {
        this.formatter = textFormatter;
    }

    protected MutableComponent createNarrationMessage() {
        Component component = this.getMessage();
        return Component.translatable("gui.narrate.editBox", new Object[]{component, this.value});
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        this.renderTextBox(guiGraphics, i, j, f);
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String text) {
        if (this.filter.test(text)) {
            if (text.length() > this.maxLength) {
                this.value = text.substring(0, this.maxLength);
            } else {
                this.value = text;
            }

            this.moveCursorToEnd(false);
            this.setHighlightPos(this.cursorPos);
            this.onValueChange(text);
        }
    }

    public String getHighlighted() {
        int i = Math.min(this.cursorPos, this.highlightPos);
        int j = Math.max(this.cursorPos, this.highlightPos);
        return this.value.substring(i, j);
    }

    public void setFilter(Predicate<String> validator) {
        this.filter = validator;
    }

    public void insertText(String textToWrite) {
        int i = Math.min(this.cursorPos, this.highlightPos);
        int j = Math.max(this.cursorPos, this.highlightPos);
        int k = this.maxLength - this.value.length() - (i - j);
        if (k > 0) {
            String string2 = StringUtil.filterText(textToWrite);
            int l = string2.length();
            if (k < l) {
                if (Character.isHighSurrogate(string2.charAt(k - 1))) {
                    --k;
                }

                string2 = string2.substring(0, k);
                l = k;
            }

            String string3 = (new StringBuilder(this.value)).replace(i, j, string2).toString();
            if (this.filter.test(string3)) {
                this.value = string3;
                this.setCursorPosition(i + l);
                this.setHighlightPos(this.cursorPos);
                this.onValueChange(this.value);
            }
        }
    }

    private void onValueChange(String newText) {
        if (this.responder != null) {
            this.responder.accept(newText);
        }

        this.updateTextPosition();
    }

    private void deleteText(int i, boolean bl) {
        if (bl) {
            this.deleteWords(i);
        } else {
            this.deleteChars(i);
        }
    }

    public void deleteWords(int num) {
        if (!this.value.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                this.deleteCharsToPos(this.getWordPosition(num));
            }
        }
    }

    public void deleteChars(int i) {
        this.deleteCharsToPos(this.getCursorPos(i));
    }

    public void deleteCharsToPos(int i) {
        if (!this.value.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                int j = Math.min(i, this.cursorPos);
                int k = Math.max(i, this.cursorPos);
                if (j != k) {
                    String string = (new StringBuilder(this.value)).delete(j, k).toString();
                    if (this.filter.test(string)) {
                        this.value = string;
                        this.moveCursorTo(j, false);
                    }
                }
            }
        }
    }

    public int getWordPosition(int numWords) {
        return this.getWordPosition(numWords, this.getCursorPosition());
    }

    private int getWordPosition(int n, int pos) {
        return this.getWordPosition(n, pos, true);
    }

    private int getWordPosition(int i, int j, boolean skipWs) {
        int k = j;
        boolean bl2 = i < 0;
        int l = Math.abs(i);

        for (int m = 0; m < l; ++m) {
            if (!bl2) {
                int n = this.value.length();
                k = this.value.indexOf(32, k);
                if (k == -1) {
                    k = n;
                } else {
                    while (skipWs && k < n && this.value.charAt(k) == ' ') {
                        ++k;
                    }
                }
            } else {
                while (skipWs && k > 0 && this.value.charAt(k - 1) == ' ') {
                    --k;
                }

                while (k > 0 && this.value.charAt(k - 1) != ' ') {
                    --k;
                }
            }
        }

        return k;
    }

    public void moveCursor(int i, boolean bl) {
        this.moveCursorTo(this.getCursorPos(i), bl);
    }

    private int getCursorPos(int i) {
        return Util.offsetByCodepoints(this.value, this.cursorPos, i);
    }

    public void moveCursorTo(int pos, boolean bl) {
        this.setCursorPosition(pos);
        if (!bl) {
            this.setHighlightPos(this.cursorPos);
        }

        this.onValueChange(this.value);
    }

    public void moveCursorToStart(boolean bl) {
        this.moveCursorTo(0, bl);
    }

    public void moveCursorToEnd(boolean bl) {
        this.moveCursorTo(this.value.length(), bl);
    }

    public boolean keyPressed(KeyEvent keyEvent) {
        if (this.isActive() && this.isFocused()) {
            switch (keyEvent.key()) {
                case 259:
                    if (this.isEditable) {
                        this.deleteText(-1, keyEvent.hasControlDownWithQuirk());
                    }

                    return true;
                case 260:
                case 264:
                case 265:
                case 266:
                case 267:
                default:
                    if (keyEvent.isSelectAll()) {
                        this.moveCursorToEnd(false);
                        this.setHighlightPos(0);
                        return true;
                    } else if (keyEvent.isCopy()) {
                        Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
                        return true;
                    } else if (keyEvent.isPaste()) {
                        if (this.isEditable()) {
                            this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
                        }

                        return true;
                    } else {
                        if (keyEvent.isCut()) {
                            Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
                            if (this.isEditable()) {
                                this.insertText("");
                            }

                            return true;
                        }

                        return false;
                    }
                case 261:
                    if (this.isEditable) {
                        this.deleteText(1, keyEvent.hasControlDownWithQuirk());
                    }

                    return true;
                case 262:
                    if (keyEvent.hasControlDownWithQuirk()) {
                        this.moveCursorTo(this.getWordPosition(1), keyEvent.hasShiftDown());
                    } else {
                        this.moveCursor(1, keyEvent.hasShiftDown());
                    }

                    return true;
                case 263:
                    if (keyEvent.hasControlDownWithQuirk()) {
                        this.moveCursorTo(this.getWordPosition(-1), keyEvent.hasShiftDown());
                    } else {
                        this.moveCursor(-1, keyEvent.hasShiftDown());
                    }

                    return true;
                case 268:
                    this.moveCursorToStart(keyEvent.hasShiftDown());
                    return true;
                case 269:
                    this.moveCursorToEnd(keyEvent.hasShiftDown());
                    return true;
            }
        } else {
            return false;
        }
    }

    public boolean canConsumeInput() {
        return this.isActive() && this.isFocused() && this.isEditable();
    }

    public boolean charTyped(char codePoint, int modifiers) {
        if (!this.canConsumeInput()) {
            return false;
        } else if (StringUtil.isAllowedChatCharacter(codePoint)) {
            if (this.isEditable) {
                this.insertText(Character.toString(codePoint));
            }

            return true;
        } else {
            return false;
        }
    }

    public void onClick(MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (bl) {
            this.selectWord(mouseButtonEvent);
        } else {
            this.moveCursorTo(this.findClickedPositionInText(mouseButtonEvent), mouseButtonEvent.hasShiftDown());
        }
    }

    private void selectWord(MouseButtonEvent mouseButtonEvent) {
        int i = this.findClickedPositionInText(mouseButtonEvent);
        int j = this.getWordPosition(-1, i);
        int k = this.getWordPosition(1, i);
        this.moveCursorTo(j, false);
        this.moveCursorTo(k, true);
    }

    private int findClickedPositionInText(MouseButtonEvent mouseButtonEvent) {
        int i = Math.min(Mth.floor(mouseButtonEvent.x()) - this.textX, this.getInnerWidth());
        String string = this.value.substring(this.displayPos);
        return this.displayPos + this.font.plainSubstrByWidth(string, i).length();
    }

    public void setFocus(boolean isFocused) {
        this.setFocused(isFocused);
    }

    public void renderTextBox(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialFrames) {
        if (this.isVisible()) {
            int k;

            if (this.isBordered()) {
                k = this.isFocused() ? -1 : -6250336;
                guiGraphics.fill(this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1, this.getY() + this.height + 1, k);
                guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, this.backgroundColor);
            }

            k = this.isEditable ? this.textColor : this.textColorUneditable;
            int l = this.cursorPos - this.displayPos;
            String string = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
            boolean bl = l >= 0 && l <= string.length();
            boolean bl2 = this.isFocused() && (Util.getMillis() - this.focusedTime) / 300L % 2L == 0L && bl;
            int m = this.textX;
            int n = Mth.clamp(this.highlightPos - this.displayPos, 0, string.length());
            if (!string.isEmpty()) {
                String string2 = bl ? string.substring(0, l) : string;
                FormattedCharSequence formattedCharSequence = (FormattedCharSequence) this.formatter.apply(string2, this.displayPos);
                guiGraphics.drawString(this.font, formattedCharSequence, m, this.textY, k, this.drawsTextShadow);
                m += this.font.width(formattedCharSequence) + 1;
            }

            boolean bl3 = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();
            int o = m;
            if (!bl) {
                o = l > 0 ? this.textX + this.width : this.textX;
            } else if (bl3) {
                o = m - 1;
                m--;
            }

            if (!string.isEmpty() && bl && l < string.length()) {
                guiGraphics.drawString(this.font, this.formatter.apply(string.substring(l), this.cursorPos), m, this.textY, k, this.drawsTextShadow);
            }

            if (this.hint != null && string.isEmpty() && !this.isFocused()) {
                guiGraphics.drawString(this.font, this.hint, m, this.textY, k);
            }

            if (!bl3 && this.suggestion != null) {
                guiGraphics.drawString(this.font, this.suggestion, o - 1, this.textY, -8355712, this.drawsTextShadow);
            }

            if (n != l) {
                int p = this.textX + this.font.width(string.substring(0, n));
                int var10001 = Math.min(o, this.getX() + this.width);
                int var10002 = this.textY - 1;
                int var10003 = Math.min(p - 1, this.getX() + this.width);
                int var10004 = this.textY + 1;
                guiGraphics.textHighlight(var10001, var10002, var10003, var10004 + 9, true);
            }

            if (bl2) {
                if (bl3) {
                    guiGraphics.fill(o, this.textY - 1, o + 1, this.textY + 1 + 9, -3092272);
                } else {
                    guiGraphics.drawString(this.font, "_", o, this.textY, k, this.drawsTextShadow);
                }
            }

        }
    }

    @Override
    public void setX(int i) {
        super.setX(i);
        this.updateTextPosition();
    }

    @Override
    public void setY(int i) {
        super.setY(i);
        this.updateTextPosition();
    }

    private void updateTextPosition() {
        if (this.font != null) {
            String string = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
            this.textX = this.getX() + (this.bordered ? 4 : 0);
            this.textY = this.bordered ? this.getY() + (this.height - 8) / 2 : this.getY();
        }
    }

    private int getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(int length) {
        this.maxLength = length;
        if (this.value.length() > length) {
            this.value = this.value.substring(0, length);
            this.onValueChange(this.value);
        }
    }

    public int getCursorPosition() {
        return this.cursorPos;
    }

    public void setCursorPosition(int i) {
        this.cursorPos = Mth.clamp(i, 0, this.value.length());
        this.scrollTo(this.cursorPos);
    }

    private boolean isBordered() {
        return this.bordered;
    }

    public void setBordered(boolean enableBackgroundDrawing) {
        this.bordered = enableBackgroundDrawing;

        this.updateTextPosition();
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    public void setTextColorUneditable(int color) {
        this.textColorUneditable = color;
    }

    public void setFocused(boolean focused) {
        if (this.canLoseFocus || focused) {
            super.setFocused(focused);
            if (focused) {
                this.focusedTime = Util.getMillis();
            }

        }
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.visible && mouseX >= (double) this.getX() && mouseX < (double) (this.getX() + this.width) && mouseY >= (double) this.getY() && mouseY < (double) (this.getY() + this.height);
    }

    protected void onFocusedChanged(boolean focused) {
        if (focused) {
            this.frame = 0;
        }
    }

    private boolean isEditable() {
        return this.isEditable;
    }

    public void setEditable(boolean enabled) {
        this.isEditable = enabled;
    }

    public int getInnerWidth() {
        return this.isBordered() ? this.width - 8 : this.width;
    }

    public void setHighlightPos(int position) {
        this.highlightPos = Mth.clamp(position, 0, this.value.length());
        this.scrollTo(this.highlightPos);
    }

    private void scrollTo(int position) {
        if (this.font != null) {
            this.displayPos = Math.min(this.displayPos, this.value.length());
            int j = this.getInnerWidth();
            String string = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), j);
            int k = string.length() + this.displayPos;
            if (position == this.displayPos) {
                this.displayPos -= this.font.plainSubstrByWidth(this.value, j, true).length();
            }

            if (position > k) {
                this.displayPos += position - k;
            } else if (position <= this.displayPos) {
                this.displayPos -= this.displayPos - position;
            }

            this.displayPos = Mth.clamp(this.displayPos, 0, this.value.length());
        }
    }

    public void setCanLoseFocus(boolean canLoseFocus) {
        this.canLoseFocus = canLoseFocus;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean isVisible) {
        this.visible = isVisible;
    }

    public void setSuggestion(String string) {
        this.suggestion = string;
    }

    public int getScreenX(int i) {
        return i > this.value.length() ? this.getX() : this.getX() + this.font.width(this.value.substring(0, i));
    }

    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable("narration.edit_box", new Object[]{this.getValue()}));
    }

    public void setHint(Component component) {
        this.hint = component;
    }
}
