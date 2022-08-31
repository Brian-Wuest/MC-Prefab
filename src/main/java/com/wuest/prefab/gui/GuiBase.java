package com.wuest.prefab.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.Utils;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.gui.controls.CustomButton;
import com.wuest.prefab.gui.controls.ExtendedButton;
import com.wuest.prefab.gui.controls.GuiCheckBox;
import com.wuest.prefab.gui.controls.GuiSlider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;


public abstract class GuiBase extends Screen {

    private final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
    private final ResourceLocation narrowPanelTexture = new ResourceLocation("prefab", "textures/gui/custom_background.png");
    private final ResourceLocation leftPanelTexture = new ResourceLocation("prefab", "textures/gui/custom_left_panel.png");
    private final ResourceLocation middlePanelTexture = new ResourceLocation("prefab", "textures/gui/custom_middle_panel.png");
    private final ResourceLocation rightPanelTexture = new ResourceLocation("prefab", "textures/gui/custom_right_panel.png");
    protected int modifiedInitialXAxis = 213;
    protected int modifiedInitialYAxis = 83;
    protected int imagePanelWidth = 0;
    protected int imagePanelHeight = 0;
    protected int shownImageHeight = 0;
    protected int shownImageWidth = 0;
    protected int textColor = Color.DARK_GRAY.getRGB();

    public GuiBase(String title) {
        super(Utils.createTextComponent(title));
    }

    @Override
    public void init() {
        this.Initialize();
    }

    /**
     * This method is used to initialize GUI specific items.
     */
    protected void Initialize() {
        this.modifiedInitialXAxis = 160;
        this.modifiedInitialYAxis = 120;
        this.imagePanelWidth = 325;
        this.imagePanelHeight = 300;
        this.shownImageHeight = 150;
        this.shownImageWidth = 268;
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

    @Override
    public void render(PoseStack matrixStack, int x, int y, float f) {
        Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();

        this.preButtonRender(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);

        this.renderButtons(matrixStack, x, y);

        this.postButtonRender(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond(), x, y, f);
    }

    /**
     * Creates a button using the button clicked event as the handler. Then adds it to the buttons list and returns the created object.
     *
     * @param x      The x-axis position.
     * @param y      The y-axis position.
     * @param width  The width of the button.
     * @param height The height of the button.
     * @param text   The text of the button.
     * @param label  The label of the button.
     * @return A new button.
     */
    public ExtendedButton createAndAddButton(int x, int y, int width, int height, String text, String label) {
        return this.createAndAddButton(x, y, width, height, text, true, label);
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
    public ExtendedButton createAndAddButton(int x, int y, int width, int height, String text) {
        return this.createAndAddButton(x, y, width, height, text, true);
    }

    /**
     * Creates a button using the button clicked event as the handler. Then adds it to the buttons list and returns the created object.
     *
     * @param x      The x-axis position.
     * @param y      The y-axis position.
     * @param width  The width of the button.
     * @param height The height of the button.
     * @param text   The text of the button.
     * @param label  The label of the button.
     * @return A new button.
     */
    public ExtendedButton createAndAddButton(int x, int y, int width, int height, String text, boolean translate, String label) {
        ExtendedButton returnValue = new ExtendedButton(x, y, width, height, translate ? GuiLangKeys.translateToComponent(text) : Utils.createTextComponent(text), this::buttonClicked, label);

        return this.addRenderableWidget(returnValue);
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
    public ExtendedButton createAndAddButton(int x, int y, int width, int height, String text, boolean translate) {
        ExtendedButton returnValue = new ExtendedButton(x, y, width, height, translate ? GuiLangKeys.translateToComponent(text) : Utils.createTextComponent(text), this::buttonClicked, null);

        return this.addRenderableWidget(returnValue);
    }

    public CustomButton createAndAddCustomButton(int x, int y, int width, int height, String text) {
        return this.createAndAddCustomButton(x, y, width, height, text, true);
    }

    public CustomButton createAndAddCustomButton(int x, int y, int width, int height, String text, boolean translate) {
        CustomButton returnValue = new CustomButton(x, y, width, height, translate ? GuiLangKeys.translateToComponent(text) : Utils.createTextComponent(text), this::buttonClicked);

        return this.addRenderableWidget(returnValue);
    }

    /**
     * Creates an ExtendedButton using the button clicked event as the handler. Then adds it to the buttons list and returns the created object.
     *
     * @param x      The x-axis position.
     * @param y      The y-axis position.
     * @param width  The width of the button.
     * @param height The height of the button.
     * @param color  The color to describe on the button.
     * @return A new button.
     */
    public ExtendedButton createAndAddDyeButton(int x, int y, int width, int height, DyeColor color, String label) {
        ExtendedButton returnValue = new ExtendedButton(x, y, width, height, Utils.createTextComponent(GuiLangKeys.translateDye(color)), this::buttonClicked, label);

        this.addRenderableWidget(returnValue);

        return returnValue;
    }

    /**
     * Creates an ExtendedButton using the button clicked event as the handler. Then adds it to the buttons list and returns the created object.
     *
     * @param x      The x-axis position.
     * @param y      The y-axis position.
     * @param width  The width of the button.
     * @param height The height of the button.
     * @param color  The color to describe on the button.
     * @return A new button.
     */
    public ExtendedButton createAndAddFullDyeButton(int x, int y, int width, int height, FullDyeColor color, String label) {
        ExtendedButton returnValue = new ExtendedButton(x, y, width, height, Utils.createTextComponent(GuiLangKeys.translateFullDye(color)), this::buttonClicked, label);

        this.addRenderableWidget(returnValue);

        return returnValue;
    }

    public GuiCheckBox createAndAddCheckBox(int xPos, int yPos, String displayString, boolean isChecked,
                                            GuiCheckBox.IPressable handler) {
        GuiCheckBox checkBox = new GuiCheckBox(xPos, yPos, GuiLangKeys.translateString(displayString), isChecked, handler);

        this.addRenderableWidget(checkBox);
        return checkBox;
    }

    public GuiSlider createAndAddSlider(int xPos, int yPos, int width, int height, String prefix, String suf,
                                        double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr,
                                        Button.OnPress handler) {
        GuiSlider slider = new GuiSlider(xPos, yPos, width, height, Utils.createTextComponent(prefix), Utils.createTextComponent(suf), minVal, maxVal, currentVal, showDec,
                drawStr, handler);

        this.addRenderableWidget(slider);
        return slider;
    }

    protected void drawControlBackground(PoseStack matrixStack, int grayBoxX, int grayBoxY, int width, int height) {
        GuiUtils.bindAndDrawScaledTexture(
                this.backgroundTextures,
                matrixStack,
                grayBoxX,
                grayBoxY,
                width,
                height,
                width,
                height,
                width,
                height);
    }

    protected void drawControlLeftPanel(PoseStack matrixStack, int grayBoxX, int grayBoxY, int width, int height) {
        GuiUtils.drawContinuousTexturedBox(
                this.leftPanelTexture,
                grayBoxX,
                grayBoxY,
                0,
                0,
                width,
                height,
                89,
                233,
                2,
                2,
                4,
                4,
                0);
    }

    protected void drawControlMiddlePanel(PoseStack matrixStack, int grayBoxX, int grayBoxY, int width, int height) {
        GuiUtils.drawContinuousTexturedBox(
                this.middlePanelTexture,
                grayBoxX,
                grayBoxY,
                0,
                0,
                width,
                height,
                89,
                233,
                2,
                2,
                4,
                4,
                0);
    }

    protected void drawControlRightPanel(PoseStack matrixStack, int grayBoxX, int grayBoxY, int width, int height) {
        GuiUtils.drawContinuousTexturedBox(
                this.rightPanelTexture,
                grayBoxX,
                grayBoxY,
                0,
                0,
                width,
                height,
                89,
                233,
                2,
                2,
                4,
                4,
                0);
    }

    protected void drawStandardControlBoxAndImage(PoseStack matrixStack, ResourceLocation imageLocation, int x, int y, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.drawControlBackground(matrixStack, x, y, this.imagePanelWidth, this.imagePanelHeight);

        // TODO: Remove this when structure is generated in GUI instead of showing a picture.
        if (imageLocation != null) {
            int imagePanelMiddle = this.imagePanelWidth / 2;

            int middleOfImage = this.shownImageWidth / 2;
            int imagePos = x + (imagePanelMiddle - middleOfImage - 5);

            GuiUtils.bindAndDrawTexture(
                    imageLocation,
                    matrixStack,
                    imagePos,
                    y + 10,
                    1,
                    this.shownImageWidth,
                    this.shownImageHeight,
                    this.shownImageWidth,
                    this.shownImageHeight);
        }
    }

    protected void renderButtons(PoseStack matrixStack, int mouseX, int mouseY) {
        for (GuiEventListener widget : this.children()) {
            if (widget instanceof AbstractWidget currentButton) {
                if (currentButton.visible) {
                    currentButton.renderButton(matrixStack, mouseX, mouseY, this.getMinecraft().getFrameTime());
                }
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
    public int drawString(PoseStack matrixStack, String text, float x, float y, int color) {
        return this.getFontRenderer().draw(matrixStack, text, x, y, color);
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
        this.getFontRenderer().drawWordWrap(Utils.createTextComponent(str), x, y, wrapWidth, textColor);
    }

    public List<FormattedCharSequence> getSplitString(String str, int wrapWidth) {
        return this.getFontRenderer().split(Utils.createTextComponent(str), wrapWidth);
    }

    public List<FormattedCharSequence> getSplitString(FormattedText str, int wrapWidth) {
        return this.getFontRenderer().split(str, wrapWidth);
    }

    /**
     * Closes the current screen.
     */
    public void closeScreen() {
        this.getMinecraft().setScreen(null);
    }

    public @NotNull Minecraft getMinecraft() {
        assert this.minecraft != null;
        return this.minecraft;
    }

    public Font getFontRenderer() {
        return this.getMinecraft().font;
    }

    /**
     * This event is called when a particular button is clicked.
     *
     * @param button The button which was clicked.
     */
    public abstract void buttonClicked(AbstractButton button);

    protected abstract void preButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks);

    protected abstract void postButtonRender(PoseStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks);
}
