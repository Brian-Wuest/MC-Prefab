package com.wuest.prefab.Gui;

import com.wuest.prefab.Tuple;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public abstract class GuiBase extends Screen {

	private final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
	private boolean pauseGame;

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

	protected int getCenteredXAxis() {
		return this.width / 2;
	}

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
	public void render(int x, int y, float f) {
		Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();

		this.preButtonRender(adjustedXYValue.getFirst(), adjustedXYValue.getSecond());

		this.renderButtons(x, y);

		this.postButtonRender(adjustedXYValue.getFirst(), adjustedXYValue.getSecond());
	}

	/**
	 * Creates a {@link GuiButtonExt} using the button clicked event as the handler. Then adds it to the buttons list and returns the created object.
	 *
	 * @param x      The x-axis position.
	 * @param y      The y-axis position.
	 * @param width  The width of the button.
	 * @param height The height of the button.
	 * @param text   The text of the button.
	 * @return A new button.
	 */
	public GuiButtonExt createAndAddButton(int x, int y, int width, int height, String text) {
		GuiButtonExt returnValue = new GuiButtonExt(x, y, width, height, text, this::buttonClicked);

		this.addButton(returnValue);

		return returnValue;
	}

	protected void drawControlBackground(int grayBoxX, int grayBoxY) {
		this.getMinecraft().getTextureManager().bindTexture(this.backgroundTextures);
		this.blit(grayBoxX, grayBoxY, 0, 0, 256, 256);
	}

	protected void renderButtons(int mouseX, int mouseY) {
		for (net.minecraft.client.gui.widget.Widget button : this.buttons) {
			Button currentButton = (Button) button;

			if (currentButton != null && currentButton.visible) {
				currentButton.renderButton(mouseX, mouseY, this.getMinecraft().getRenderPartialTicks());
			}
		}
	}

	public abstract void buttonClicked(Button button);

	protected abstract Tuple<Integer, Integer> getAdjustedXYValue();

	protected abstract void preButtonRender(int x, int y);

	protected abstract void postButtonRender(int x, int y);
}
