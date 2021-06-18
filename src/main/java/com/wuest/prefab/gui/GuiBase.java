package com.wuest.prefab.gui;

import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;

public abstract class GuiBase extends GuiScreen {

    private final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
    protected int modifiedInitialXAxis = 213;
    protected int modifiedInitialYAxis = 83;
    private boolean pauseGame;

    public GuiBase() {
        super();
        this.pauseGame = true;
    }

    @Override
    public void initGui() {
        this.Initialize();
    }

    /**
     * This method is used to initialize GUI specific items.
     */
    protected void Initialize() {
    }

    /**
     * Gets the X-Coordinates of the center of the screen.
     *
     * @return The coordinate value.
     */
    protected int getCenteredXAxis() {
        return this.width / 2;
    }

    /**
     * Gets the Y-Coordinates of the center off the screen.
     *
     * @return The coordinate value.
     */
    protected int getCenteredYAxis() {
        return this.height / 2;
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    @Override
    public boolean doesGuiPauseGame() {
        return this.pauseGame;
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();

        this.preButtonRender(adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);

        this.renderButtons(x, y);

        this.postButtonRender(adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);
    }

    /**
     * Creates a button using the button clicked event as the handler. Then adds it to the buttons list and returns the created object.
     *
     * @param x      The x-axis position.
     * @param y      The y-axis position.
     * @param width  The width of the button.
     * @param height The height of the button.
     * @param text   The text of the button.
     * @return A new button.
     */
    public GuiButtonExt createAndAddButton(int id, int x, int y, int width, int height, String text) {
        return this.createAndAddButton(id, x, y, width, height, text, true);
    }

    /**
     * Creates a button using the button clicked event as the handler. Then adds it to the buttons list and returns the created object.
     *
     * @param x      The x-axis position.
     * @param y      The y-axis position.
     * @param width  The width of the button.
     * @param height The height of the button.
     * @param text   The text of the button.
     * @return A new button.
     */
    public GuiButtonExt createAndAddButton(int id, int x, int y, int width, int height, String text, boolean translate) {
        GuiButtonExt returnValue = new GuiButtonExt(id, x, y, width, height, translate ? GuiLangKeys.translateString(text) : text);

        this.addButton(returnValue);

        return returnValue;
    }

    /**
     * Creates a button using the button clicked event as the handler. Then adds it to the buttons list and returns the created object.
     *
     * @param x      The x-axis position.
     * @param y      The y-axis position.
     * @param width  The width of the button.
     * @param height The height of the button.
     * @param color  The color to describe on the button.
     * @return A new button.
     */
    public GuiButtonExt createAndAddDyeButton(int id, int x, int y, int width, int height, EnumDyeColor color) {
        GuiButtonExt returnValue = new GuiButtonExt(id, x, y, width, height, GuiLangKeys.translateDye(color));

        this.addButton(returnValue);

        return returnValue;
    }

    /**
     * Creates a button using the button clicked event as the handler. Then adds it to the buttons list and returns the created object.
     *
     * @param x      The x-axis position.
     * @param y      The y-axis position.
     * @param width  The width of the button.
     * @param height The height of the button.
     * @param color  The color to describe on the button.
     * @return A new button.
     */
    public GuiButtonExt createAndAddFullDyeButton(int id, int x, int y, int width, int height, FullDyeColor color) {
        GuiButtonExt returnValue = new GuiButtonExt(id, x, y, width, height, GuiLangKeys.translateFullDye(color));

        this.addButton(returnValue);

        return returnValue;
    }

    public GuiCheckBox createAndAddCheckBox(int id, int xPos, int yPos, String displayString, boolean isChecked) {
        GuiCheckBox checkBox = new GuiCheckBox(id, xPos, yPos, GuiLangKeys.translateString(displayString), isChecked);

        this.addButton(checkBox);
        return checkBox;
    }

    public GuiSlider createAndAddSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf,
                                        double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr) {
        GuiSlider slider = new GuiSlider(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec,
                drawStr);

        this.addButton(slider);
        return slider;
    }

    protected void drawControlBackground(int grayBoxX, int grayBoxY) {
        GuiUtils.bindTexture(this.backgroundTextures);
        this.drawTexturedModalRect(grayBoxX, grayBoxY, 0, 0, 256, 256);
    }

    protected void renderButtons(int mouseX, int mouseY) {
        for (GuiButton currentButton : this.buttonList) {
            if (currentButton != null && currentButton.visible) {
                currentButton.drawButton(this.mc, mouseX, mouseY, this.mc.getRenderPartialTicks());
            }
        }

        for (GuiLabel currentLabel : this.labelList) {
            if (currentLabel != null) {
                currentLabel.drawLabel(this.mc, mouseX, mouseY);
            }
        }
    }

    /**
     * Gets the adjusted x/y coordinates for the topleft most part of the screen.
     *
     * @return A new tuple containing the x/y coordinates.
     */
    protected Tuple<Integer, Integer> getAdjustedXYValue() {
        return new Tuple<>(this.getCenteredXAxis() - this.modifiedInitialXAxis, this.getCenteredYAxis() - this.modifiedInitialYAxis);
    }

    /**
     * Draws a string on the screen.
     *
     * @param text  The text to draw.
     * @param x     The X-Coordinates of the string to start.
     * @param y     The Y-Coordinates of the string to start.
     * @param color The color of the text.
     * @return Some integer value.
     */
    public int drawString(String text, int x, int y, int color) {
        return this.getFontRenderer().drawString(text, x, y, color);
    }

    /**
     * Draws a string on the screen with word wrapping.
     *
     * @param str       The text to draw.
     * @param x         The X-Coordinates of the string to start.
     * @param y         The Y-Coordinates of the string to start.
     * @param wrapWidth The maximum width before wrapping begins.
     * @param textColor The color of the text.
     */
    public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor) {
        this.getFontRenderer().drawSplitString(str, x, y, wrapWidth, textColor);
    }

    /**
     * Closes the current screen.
     */
    public void closeScreen() {
        this.getMinecraft().displayGuiScreen(null);
    }

    public Minecraft getMinecraft() {
        return this.mc;
    }

    public FontRenderer getFontRenderer() {
        return this.getMinecraft().fontRenderer;
    }

    protected abstract void preButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks);

    protected abstract void postButtonRender(int x, int y, int mouseX, int mouseY, float partialTicks);
}