package com.wuest.prefab.gui.controls;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.gui.GuiUtils;
import com.wuest.prefab.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;

import java.awt.*;

/**
 * @author WuestMan
 */
@SuppressWarnings("WeakerAccess")
public class GuiTab extends Widget {
	protected static final ResourceLocation TAB_TEXTURES = new ResourceLocation("prefab", "textures/gui/gui_tab.png");
	protected static final ResourceLocation TAB_TEXTURES_hovered = new ResourceLocation("prefab", "textures/gui/gui_tab_hovered.png");
	private GuiTabTray parentTray;
	private boolean selected;
	private String name;

	public GuiTab(GuiTabTray parent, String name, int x, int y) {
		super(x, y, 50, 20, Utils.createTextComponent(name));

		this.Initialize(parent, name);
	}

	protected void Initialize(GuiTabTray parent, String name) {
		this.parentTray = parent;
		this.selected = false;
		this.name = name;
		this.height = 20;
		this.width = 50;
		this.visible = true;
	}

	public GuiTabTray getParent() {
		return this.parentTray;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String value) throws Exception {
		if (this.parentTray.DoesTabNameExist(value)) {
			throw new Exception("A tab with the name of [" + value + "] already exists.");
		}

		this.name = value;
	}

	public boolean getIsSelected() {
		return this.selected;
	}

	public void setIsSelected(boolean value) {
		this.selected = value;

		this.parentTray.SetSelectedTab(this.selected ? this : null);
	}

	/**
	 * This is an internal method used to set the boolean property without triggering the parent tray method.
	 *
	 * @param value The new value for the selected property.
	 */
	protected void InternalSetSelected(boolean value) {
		this.selected = value;
	}

	/**
	 * Draws this button to the screen.
	 *
	 * @param mc     The minecraft object.
	 * @param mouseX The location of the mouse X-Axis.
	 * @param mouseY The location of the mouse Y-Axis.
	 */
	public void drawTab(Minecraft mc, MatrixStack matrixStack, int mouseX, int mouseY) {
		if (!this.visible) {
			return;
		}

		FontRenderer fontrenderer = mc.font;
		this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

		if (this.selected || this.isHovered) {
			GuiUtils.bindTexture(TAB_TEXTURES_hovered);
		} else {
			GuiUtils.bindTexture(TAB_TEXTURES);
		}

		GuiUtils.drawTexture(matrixStack, this.x, this.y, 0, this.width, this.height, this.width, this.height);
		int j = Color.LIGHT_GRAY.getRGB();

		String buttonText = this.name;
		int strWidth = fontrenderer.width(this.name);
		int ellipsisWidth = fontrenderer.width("...");

		if (strWidth > width - 6 && strWidth > ellipsisWidth) {
			buttonText = mc.font.substrByWidth(Utils.createTextComponent(buttonText), width - 6 - ellipsisWidth).getString() + "...";
		}

		int stringXPosition = ((this.x + this.width / 2) - (fontrenderer.width(buttonText)) / 2);

		fontrenderer.draw(matrixStack, buttonText, stringXPosition, this.y + (this.height - 8) / 2, j);
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
	 *
	 * @param mouseX        The x-position for the mouse.
	 * @param mouseY        The y-position for the mouse.
	 * @param buttonClicked The button which was clicked.
	 * @return True if it's over this control, otherwise false.
	 */
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int buttonClicked) {
		boolean value = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height && this.isValidClickButton(buttonClicked);

		if (value && !this.selected) {
			// Select this tab;
			this.setIsSelected(true);
		}

		return value;
	}

	@Override
	public void playDownSound(SoundHandler soundHandlerIn) {
		soundHandlerIn.play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
}
