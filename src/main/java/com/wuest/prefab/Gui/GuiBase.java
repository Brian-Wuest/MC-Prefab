package com.wuest.prefab.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Gui.Controls.GuiCheckBox;
import com.wuest.prefab.Tuple;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.client.gui.widget.Slider;

public abstract class GuiBase extends Screen {

	private final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
	private boolean pauseGame;
	protected int modifiedInitialXAxis = 213;
	protected int modifiedInitialYAxis = 83;

	public GuiBase(String title) {
		super(new StringTextComponent(title));
		this.pauseGame = true;
	}

	@Override
	public void init() {
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
	public boolean isPauseScreen() {
		return this.pauseGame;
	}

	@Override
	public void render(MatrixStack matrixStack, int x, int y, float f) {
		Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();

		this.preButtonRender(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond());

		this.renderButtons(matrixStack, x, y);

		this.postButtonRender(matrixStack, adjustedXYValue.getFirst(), adjustedXYValue.getSecond());
	}

	/**
	 * Creates a {@link net.minecraftforge.fml.client.gui.widget.ExtendedButton} using the button clicked event as the handler. Then adds it to the buttons list and returns the created object.
	 *
	 * @param x      The x-axis position.
	 * @param y      The y-axis position.
	 * @param width  The width of the button.
	 * @param height The height of the button.
	 * @param text   The text of the button.
	 * @return A new button.
	 */
	public ExtendedButton createAndAddButton(int x, int y, int width, int height, String text) {
		ExtendedButton returnValue = new ExtendedButton(x, y, width, height, new StringTextComponent(text), this::buttonClicked);

		this.addButton(returnValue);

		return returnValue;
	}

	public GuiCheckBox createAndAddCheckBox(int xPos, int yPos, String displayString, boolean isChecked,
											GuiCheckBox.IPressable handler) {
		GuiCheckBox checkBox = new GuiCheckBox(xPos, yPos, displayString, isChecked, handler);

		this.addButton(checkBox);
		return checkBox;
	}

	public Slider createAndAddSlider(int xPos, int yPos, int width, int height, String prefix, String suf,
									 double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr,
									 Button.IPressable handler) {
		Slider slider = new Slider(xPos, yPos, width, height, new StringTextComponent(prefix), new StringTextComponent(suf), minVal, maxVal, currentVal, showDec,
				drawStr, handler);

		this.addButton(slider);
		return slider;
	}

	protected void drawControlBackground(MatrixStack matrixStack, int grayBoxX, int grayBoxY) {
		this.getMinecraft().getTextureManager().bind(this.backgroundTextures);
		this.blit(matrixStack, grayBoxX, grayBoxY, 0, 0, 256, 256);
	}

	protected void renderButtons(MatrixStack matrixStack, int mouseX, int mouseY) {
		for (net.minecraft.client.gui.widget.Widget button : this.buttons) {
			AbstractButton currentButton = (AbstractButton) button;

			if (currentButton != null && currentButton.visible) {
				currentButton.renderButton(matrixStack, mouseX, mouseY, this.getMinecraft().getFrameTime());
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
	public int drawString(MatrixStack matrixStack, String text, float x, float y, int color) {
		return this.getMinecraft().font.draw(matrixStack, text, x, y, color);
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
		this.getMinecraft().font.drawWordWrap(new StringTextComponent(str), x, y, wrapWidth, textColor);
	}

	/**
	 * Closes the current screen.
	 */
	public void closeScreen() {
		this.getMinecraft().setScreen(null);
	}

	/**
	 * Binds a texture to the texture manager for rendering.
	 *
	 * @param resourceLocation The resource location to bind.
	 */
	public void bindTexture(ResourceLocation resourceLocation) {
		this.getMinecraft().getTextureManager().bind(resourceLocation);
	}

	/**
	 * This event is called when a particular button is clicked.
	 *
	 * @param button The button which was clicked.
	 */
	public abstract void buttonClicked(AbstractButton button);

	protected abstract void preButtonRender(MatrixStack matrixStack, int x, int y);

	protected abstract void postButtonRender(MatrixStack matrixStack, int x, int y);
}
