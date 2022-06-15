package com.wuest.prefab.gui.controls;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GuiTextBox extends AbstractWidget implements Widget, GuiEventListener {
    private final net.minecraft.client.gui.Font font;
    public int backgroundColor;
    public boolean drawsTextShadow;
    @Nullable
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
    @Nullable
    private Consumer<String> responder;
    private Predicate<String> filter;
    private BiFunction<String, Integer, FormattedCharSequence> formatter;

    public GuiTextBox(net.minecraft.client.gui.Font textRenderer, int x, int y, int width, int height, net.minecraft.network.chat.Component text) {
        this(textRenderer, x, y, width, height, (EditBox) null, text);
    }

    public GuiTextBox(Font textRenderer, int x, int y, int width, int height, @Nullable EditBox copyFrom, net.minecraft.network.chat.Component text) {
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

    public void tick() {
        ++this.frame;
    }

    protected MutableComponent createNarrationMessage() {
        Component component = this.getMessage();
        return Component.translatable("gui.narrate.editBox", new Object[]{component, this.value});
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

            this.moveCursorToEnd();
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
        String string = SharedConstants.filterText(textToWrite);
        int l = string.length();
        if (k < l) {
            string = string.substring(0, k);
            l = k;
        }

        String string2 = (new StringBuilder(this.value)).replace(i, j, string).toString();
        if (this.filter.test(string2)) {
            this.value = string2;
            this.setCursorPosition(i + l);
            this.setHighlightPos(this.cursorPos);
            this.onValueChange(this.value);
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
                this.deleteChars(this.getWordPosition(num) - this.cursorPos);
            }
        }
    }

    public void deleteChars(int num) {
        if (!this.value.isEmpty()) {
            if (this.highlightPos != this.cursorPos) {
                this.insertText("");
            } else {
                int i = this.getCursorPos(num);
                int j = Math.min(i, this.cursorPos);
                int k = Math.max(i, this.cursorPos);
                if (j != k) {
                    String string = (new StringBuilder(this.value)).delete(j, k).toString();
                    if (this.filter.test(string)) {
                        this.value = string;
                        this.moveCursorTo(j);
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

    private int getWordPosition(int n, int pos, boolean skipWs) {
        int i = pos;
        boolean bl = n < 0;
        int j = Math.abs(n);

        for (int k = 0; k < j; ++k) {
            if (!bl) {
                int l = this.value.length();
                i = this.value.indexOf(32, i);
                if (i == -1) {
                    i = l;
                } else {
                    while (skipWs && i < l && this.value.charAt(i) == ' ') {
                        ++i;
                    }
                }
            } else {
                while (skipWs && i > 0 && this.value.charAt(i - 1) == ' ') {
                    --i;
                }

                while (i > 0 && this.value.charAt(i - 1) != ' ') {
                    --i;
                }
            }
        }

        return i;
    }

    public void moveCursor(int num) {
        this.moveCursorTo(this.getCursorPos(num));
    }

    private int getCursorPos(int i) {
        return Util.offsetByCodepoints(this.value, this.cursorPos, i);
    }

    public void moveCursorTo(int pos) {
        this.setCursorPosition(pos);
        if (!this.shiftPressed) {
            this.setHighlightPos(this.cursorPos);
        }

        this.onValueChange(this.value);
    }

    public void moveCursorToStart() {
        this.moveCursorTo(0);
    }

    public void moveCursorToEnd() {
        this.moveCursorTo(this.value.length());
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.canConsumeInput()) {
            return false;
        } else {
            this.shiftPressed = Screen.hasShiftDown();
            if (Screen.isSelectAll(keyCode)) {
                this.moveCursorToEnd();
                this.setHighlightPos(0);
                return true;
            } else if (Screen.isCopy(keyCode)) {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
                return true;
            } else if (Screen.isPaste(keyCode)) {
                if (this.isEditable) {
                    this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
                }

                return true;
            } else if (Screen.isCut(keyCode)) {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
                if (this.isEditable) {
                    this.insertText("");
                }

                return true;
            } else {
                switch (keyCode) {
                    case 259:
                        if (this.isEditable) {
                            this.shiftPressed = false;
                            this.deleteText(-1);
                            this.shiftPressed = Screen.hasShiftDown();
                        }

                        return true;
                    case 260:
                    case 264:
                    case 265:
                    case 266:
                    case 267:
                    default:
                        return false;
                    case 261:
                        if (this.isEditable) {
                            this.shiftPressed = false;
                            this.deleteText(1);
                            this.shiftPressed = Screen.hasShiftDown();
                        }

                        return true;
                    case 262:
                        if (Screen.hasControlDown()) {
                            this.moveCursorTo(this.getWordPosition(1));
                        } else {
                            this.moveCursor(1);
                        }

                        return true;
                    case 263:
                        if (Screen.hasControlDown()) {
                            this.moveCursorTo(this.getWordPosition(-1));
                        } else {
                            this.moveCursor(-1);
                        }

                        return true;
                    case 268:
                        this.moveCursorToStart();
                        return true;
                    case 269:
                        this.moveCursorToEnd();
                        return true;
                }
            }
        }
    }

    public boolean canConsumeInput() {
        return this.isVisible() && this.isFocused() && this.isEditable();
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

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isVisible()) {
            return false;
        } else {
            boolean bl = mouseX >= (double) this.x && mouseX < (double) (this.x + this.width) && mouseY >= (double) this.y && mouseY < (double) (this.y + this.height);
            if (this.canLoseFocus) {
                this.setFocus(bl);
            }

            if (this.isFocused() && bl && button == 0) {
                int i = Mth.floor(mouseX) - this.x;
                if (this.bordered) {
                    i -= 4;
                }

                String string = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
                this.moveCursorTo(this.font.plainSubstrByWidth(string, i).length() + this.displayPos);
                return true;
            } else {
                return false;
            }
        }
    }

    public void setFocus(boolean isFocused) {
        this.setFocused(isFocused);
    }

    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (this.isVisible()) {
            int j;
            if (this.isBordered()) {
                j = this.isFocused() ? -1 : -6250336;
                fill(poseStack, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, j);
                fill(poseStack, this.x, this.y, this.x + this.width, this.y + this.height, this.backgroundColor);
            }

            j = this.isEditable ? this.textColor : this.textColorUneditable;
            int k = this.cursorPos - this.displayPos;
            int l = this.highlightPos - this.displayPos;
            String string = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
            boolean bl = k >= 0 && k <= string.length();
            boolean bl2 = this.isFocused() && this.frame / 6 % 2 == 0 && bl;
            int m = this.bordered ? this.x + 4 : this.x;
            int n = this.bordered ? this.y + (this.height - 8) / 2 : this.y;
            int o = m;
            if (l > string.length()) {
                l = string.length();
            }

            if (!string.isEmpty()) {
                String string2 = bl ? string.substring(0, k) : string;

                if (!this.drawsTextShadow) {
                    o = this.font.draw(poseStack, this.formatter.apply(string2, this.displayPos), (float) m, (float) n, j);
                } else {
                    o = this.font.drawShadow(poseStack, this.formatter.apply(string2, this.displayPos), (float) m, (float) n, j);
                }
            }

            boolean bl3 = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();
            int p = o;
            if (!bl) {
                p = k > 0 ? m + this.width : m;
            } else if (bl3) {
                p = o - 1;
                --o;
            }

            if (!string.isEmpty() && bl && k < string.length()) {
                if (!this.drawsTextShadow) {
                    this.font.draw(poseStack, this.formatter.apply(string.substring(k), this.cursorPos), (float) o, (float) n, j);
                } else {
                    this.font.drawShadow(poseStack, this.formatter.apply(string.substring(k), this.cursorPos), (float) o, (float) n, j);
                }
            }

            if (!bl3 && this.suggestion != null) {
                if (!this.drawsTextShadow) {
                    this.font.draw(poseStack, this.suggestion, (float) (p - 1), (float) n, -8355712);
                } else {
                    this.font.drawShadow(poseStack, this.suggestion, (float) (p - 1), (float) n, -8355712);
                }
            }

            int var10002;
            int var10003;
            int var10004;
            if (bl2) {
                if (bl3) {
                    var10002 = n - 1;
                    var10003 = p + 1;
                    var10004 = n + 1;
                    Objects.requireNonNull(this.font);
                    GuiComponent.fill(poseStack, p, var10002, var10003, var10004 + 9, -3092272);
                } else {
                    if (!this.drawsTextShadow) {
                        this.font.draw(poseStack, "_", (float) p, (float) n, j);
                    } else {
                        this.font.drawShadow(poseStack, "_", (float) p, (float) n, j);
                    }
                }
            }

            if (l != k) {
                int q = m + this.font.width(string.substring(0, l));
                var10002 = n - 1;
                var10003 = q - 1;
                var10004 = n + 1;
                Objects.requireNonNull(this.font);
                this.renderHighlight(p, var10002, var10003, var10004 + 9);
            }

        }
    }

    private void renderHighlight(int startX, int startY, int endX, int endY) {
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

        if (endX > this.x + this.width) {
            endX = this.x + this.width;
        }

        if (startX > this.x + this.width) {
            startX = this.x + this.width;
        }

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(0.0F, 0.0F, 1.0F, 1.0F);
        RenderSystem.disableTexture();
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        bufferBuilder.vertex((double) startX, (double) endY, 0.0D).endVertex();
        bufferBuilder.vertex((double) endX, (double) endY, 0.0D).endVertex();
        bufferBuilder.vertex((double) endX, (double) startY, 0.0D).endVertex();
        bufferBuilder.vertex((double) startX, (double) startY, 0.0D).endVertex();
        tesselator.end();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableColorLogicOp();
        RenderSystem.enableTexture();
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

    public void setCursorPosition(int pos) {
        this.cursorPos = Mth.clamp(pos, 0, this.value.length());
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

    public boolean changeFocus(boolean focus) {
        return this.visible && this.isEditable ? super.changeFocus(focus) : false;
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.visible && mouseX >= (double) this.x && mouseX < (double) (this.x + this.width) && mouseY >= (double) this.y && mouseY < (double) (this.y + this.height);
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
        int i = this.value.length();
        this.highlightPos = Mth.clamp(position, 0, i);
        if (this.font != null) {
            if (this.displayPos > i) {
                this.displayPos = i;
            }

            int j = this.getInnerWidth();
            String string = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), j);
            int k = string.length() + this.displayPos;
            if (this.highlightPos == this.displayPos) {
                this.displayPos -= this.font.plainSubstrByWidth(this.value, j, true).length();
            }

            if (this.highlightPos > k) {
                this.displayPos += this.highlightPos - k;
            } else if (this.highlightPos <= this.displayPos) {
                this.displayPos -= this.displayPos - this.highlightPos;
            }

            this.displayPos = Mth.clamp(this.displayPos, 0, i);
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

    public void setSuggestion(@Nullable String string) {
        this.suggestion = string;
    }

    public int getScreenX(int i) {
        return i > this.value.length() ? this.x : this.x + this.font.width(this.value.substring(0, i));
    }

    public void setX(int x) {
        this.x = x;
    }

    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable("narration.edit_box", new Object[]{this.getValue()}));
    }
}
