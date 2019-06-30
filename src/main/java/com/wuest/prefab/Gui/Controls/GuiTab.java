package com.wuest.prefab.Gui.Controls;

import java.awt.Color;

import com.wuest.prefab.Gui.GuiTabScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;

/**
 * 
 * @author WuestMan
 *
 */
public class GuiTab extends Widget
{
	private GuiTabTray parentTray;
	private boolean selected;
	private String name;
	protected static final ResourceLocation TAB_TEXTURES = new ResourceLocation("prefab", "textures/gui/gui_tab.png");
	protected static final ResourceLocation TAB_TEXTURES_hovered = new ResourceLocation("prefab", "textures/gui/gui_tab_hovered.png");

	public GuiTab(GuiTabTray parent, String name, int x, int y)
	{
		super(x, y, name);
		
		this.Initialize(parent, name);
	}

	protected void Initialize(GuiTabTray parent, String name)
	{
		this.parentTray = parent;
		this.selected = false;
		this.name = name;
		this.height = 20;
		this.width = 50;
		this.visible = true;
	}

	public GuiTabTray getParent()
	{
		return this.parentTray;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String value) throws Exception
	{
		if (this.parentTray.DoesTabNameExist(value))
		{
			Exception exception = new Exception("A tab with the name of [" + value + "] already exists.");
			throw exception;
		}

		this.name = value;
	}

	public boolean getIsSelected()
	{
		return this.selected;
	}

	public void setIsSelected(boolean value)
	{
		this.selected = value;

		this.parentTray.SetSelectedTab(this.selected ? this : null);
	}

	/**
	 * This is an internal method used to set the boolean property without triggering the parent tray method.
	 * 
	 * @param value The new value for the selected property.
	 */
	protected void InternalSetSelected(boolean value)
	{
		this.selected = value;
	}

	/**
	 * Draws this button to the screen.
	 * 
	 * @param mc The minecraft object.
	 * @param mouseX The location of the mouse X-Axis.
	 * @param mouseY The lcoation of the mouse Y-Axis.
	 */
	public void drawTab(Minecraft mc, int mouseX, int mouseY)
	{
		if (!this.visible)
		{
			return;
		}

		FontRenderer fontrenderer = mc.fontRenderer;
		this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

		if (this.selected || this.isHovered)
		{
			mc.getTextureManager().bindTexture(TAB_TEXTURES_hovered);
		}
		else
		{
			mc.getTextureManager().bindTexture(TAB_TEXTURES);
		}

		GuiTabScreen.drawModalRectWithCustomSizedTexture(this.x, this.y, 0, this.width, this.height, this.width, this.height);
		int j = Color.LIGHT_GRAY.getRGB();

		int stringXPosition = ((this.x + this.width / 2) - (fontrenderer.getStringWidth(this.name)) / 2);
		fontrenderer.drawString(this.name, stringXPosition, this.y + (this.height - 8) / 2, j);
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
	 * e).
	 * 
	 * @param mc The minecraft object.
	 * @param mouseX The location of the mouse X-Axis.
	 * @param mouseY The lcoation of the mouse Y-Axis.
	 * @return True if this tab was clicked, otherwise false.
	 */
	@Override
	public boolean clicked(double mouseX, double mouseY)
	{
		boolean value = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

		if (value && !this.selected)
		{
			// Select this tab;
			this.setIsSelected(true);
		}

		return value;
	}

	@Override
	public void playDownSound(SoundHandler soundHandlerIn)
	{
		soundHandlerIn.play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
}
