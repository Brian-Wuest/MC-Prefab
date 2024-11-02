package com.wuest.prefab.gui.controls;

import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Note: This is a copy of the Minecraft:EditBox component.
 * Otherwise seen as the "widget/text_field"
 *
 * The major difference between this class and the main EditBox is that this one allows for different text to be drawn.
 * I.E. Not Drawing the text shadow if we don't want too, or including it if we do!
 */
public class GuiTextBox extends AbstractWidget implements Renderable, GuiEventListener {
    private final net.minecraft.client.gui.Font font;
    public int backgroundColor;
    public boolean drawsTextShadow;

    public String suggestion;
    private String value;
    private int maxLength;
    private int frame;
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
            String string2 = SharedConstants.filterText(textToWrite);
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

    }

    private void deleteText(int i) {
        if (Screen.hasControlDown()) {
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

        for(int m = 0; m < l; ++m) {
            if (!bl2) {
                int n = this.value.length();
                k = this.value.indexOf(32, k);
                if (k == -1) {
                    k = n;
                } else {
                    while(skipWs && k < n && this.value.charAt(k) == ' ') {
                        ++k;
                    }
                }
            } else {
                while(skipWs && k > 0 && this.value.charAt(k - 1) == ' ') {
                    --k;
                }

                while(k > 0 && this.value.charAt(k - 1) != ' ') {
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

    public void setCursorPosition(int i) {
        this.cursorPos = Mth.clamp(i, 0, this.value.length());
        this.scrollTo(this.cursorPos);
    }

    public void moveCursorToStart(boolean bl) {
        this.moveCursorTo(0, bl);
    }

    public void moveCursorToEnd(boolean bl) {
        this.moveCursorTo(this.value.length(), bl);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.isActive() && this.isFocused()) {
            switch (keyCode) {
                case 259:
                    if (this.isEditable) {
                        this.deleteText(-1);
                    }

                    return true;
                case 260:
                case 264:
                case 265:
                case 266:
                case 267:
                default:
                    if (Screen.isSelectAll(keyCode)) {
                        this.moveCursorToEnd(false);
                        this.setHighlightPos(0);
                        return true;
                    } else if (Screen.isCopy(keyCode)) {
                        Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
                        return true;
                    } else if (Screen.isPaste(keyCode)) {
                        if (this.isEditable()) {
                            this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
                        }

                        return true;
                    } else {
                        if (Screen.isCut(keyCode)) {
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
                        this.deleteText(1);
                    }

                    return true;
                case 262:
                    if (Screen.hasControlDown()) {
                        this.moveCursorTo(this.getWordPosition(1), Screen.hasShiftDown());
                    } else {
                        this.moveCursor(1, Screen.hasShiftDown());
                    }

                    return true;
                case 263:
                    if (Screen.hasControlDown()) {
                        this.moveCursorTo(this.getWordPosition(-1), Screen.hasShiftDown());
                    } else {
                        this.moveCursor(-1, Screen.hasShiftDown());
                    }

                    return true;
                case 268:
                    this.moveCursorToStart(Screen.hasShiftDown());
                    return true;
                case 269:
                    this.moveCursorToEnd(Screen.hasShiftDown());
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
        } else if (SharedConstants.isAllowedChatCharacter(codePoint)) {
            if (this.isEditable) {
                this.insertText(Character.toString(codePoint));
            }

            return true;
        } else {
            return false;
        }
    }

    public void onClick(double d, double e) {
        int i = Mth.floor(d) - this.getX();
        if (this.bordered) {
            i -= 4;
        }

        String string = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
        this.moveCursorTo(this.font.plainSubstrByWidth(string, i).length() + this.displayPos, Screen.hasShiftDown());
    }

    public void setFocus(boolean isFocused) {
        this.setFocused(isFocused);
    }

    public void renderTextBox(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialFrames) {
        if (this.isVisible()) {
            int innerTextColor;

            if (this.isBordered()) {
                innerTextColor = this.isFocused() ? -1 : -6250336;
                guiGraphics.fill(this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1, this.getY() + this.height + 1, innerTextColor);
                guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, this.backgroundColor);
            }

            innerTextColor = this.isEditable ? this.textColor : this.textColorUneditable;

            int l = this.cursorPos - this.displayPos;

            String string = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
            boolean bl = l >= 0 && l <= string.length();
            boolean bl2 = this.isFocused() && (Util.getMillis() - this.focusedTime) / 300L % 2L == 0L && bl;
            int m = this.bordered ? this.getX() + 4 : this.getX();
            int n = this.bordered ? this.getY() + (this.height - 8) / 2 : this.getY();
            int o = m;
            int p = Mth.clamp(this.highlightPos - this.displayPos, 0, string.length());

            if (!string.isEmpty()) {
                String string2 = bl ? string.substring(0, l) : string;

                o = guiGraphics.drawString(this.font, this.formatter.apply(string2, this.displayPos), m, n, innerTextColor, this.drawsTextShadow);
            }

            boolean bl3 = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();
            int q = o;
            if (!bl) {
                q = l > 0 ? m + this.width : m;
            } else if (bl3) {
                q = o - 1;
                --o;
            }

            if (!string.isEmpty() && bl && l < string.length()) {
                guiGraphics.drawString(this.font, this.formatter.apply(string.substring(l), this.cursorPos), o, n, innerTextColor, this.drawsTextShadow);
            }

            if (this.hint != null && string.isEmpty() && !this.isFocused()) {
                guiGraphics.drawString(this.font, this.hint, o, n, innerTextColor, this.drawsTextShadow);
            }

            if (!bl3 && this.suggestion != null) {
                guiGraphics.drawString(this.font, this.suggestion, q - 1, n, -8355712, this.drawsTextShadow);
            }

            int var10003;
            int var10004;
            int var10005;

            if (bl2) {
                if (bl3) {
                    RenderType var10001 = RenderType.guiOverlay();
                    var10003 = n - 1;
                    var10004 = q + 1;
                    var10005 = n + 1;
                    Objects.requireNonNull(this.font);
                    guiGraphics.fill(var10001, q, var10003, var10004, var10005 + 9, -3092272);
                } else {
                    guiGraphics.drawString(this.font, "_", q, n, innerTextColor, this.drawsTextShadow);
                }
            }

            if (p != l) {
                int r = m + this.font.width(string.substring(0, p));
                var10003 = n - 1;
                var10004 = r - 1;
                var10005 = n + 1;
                Objects.requireNonNull(this.font);
                this.renderHighlight(guiGraphics, q, var10003, var10004, var10005 + 9);
            }

        }
    }

    private void renderHighlight(GuiGraphics guiGraphics, int startX, int startY, int endX, int endY) {
        int j;
        if (startX < endX) {
            j = startX;
            startX = endX;
            endX = j;
        }

        if (startY < endY) {
            j = startY;
            startY = endY;
            endY = j;
        }

        if (endX > this.getX() + this.width) {
            endX = this.getX() + this.width;
        }

        if (startX > this.getX() + this.width) {
            startX = this.getX() + this.width;
        }

        guiGraphics.fill(RenderType.guiTextHighlight(), startX, startY, endX, endY, -16776961);
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

    private boolean isBordered() {
        return this.bordered;
    }

    public void setBordered(boolean enableBackgroundDrawing) {
        this.bordered = enableBackgroundDrawing;
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
