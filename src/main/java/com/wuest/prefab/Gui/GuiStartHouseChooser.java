package com.wuest.prefab.Gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.ChickenCoopConfiguration;
import com.wuest.prefab.Gui.Controls.GuiTab;
import com.wuest.prefab.Gui.Controls.GuiTabTray;
import com.wuest.prefab.Proxy.Messages.ChickenCoopTagMessage;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiStartHouseChooser extends GuiTabScreen
{
	private static final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/defaultBackground.png");
	protected GuiButtonExt btnCancel;
	protected GuiButtonExt btnBuild;
	protected GuiTab test;
	protected GuiTab test2;
	
	public BlockPos pos;
	
	public GuiStartHouseChooser(int x, int y, int z)
	{
		super();
		this.pos = new BlockPos(x, y, z);
		this.Tabs.trayWidth = 256;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.Initialize();
	}
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f) 
	{
		int grayBoxX = (this.width / 2) - 188;
		int grayBoxY = (this.height / 2) - 83;
		this.Tabs.trayX = grayBoxX;
		this.Tabs.trayY = grayBoxY - 21;
		
		this.drawDefaultBackground();
		
		// Draw the control background.
		this.mc.getTextureManager().bindTexture(backgroundTextures);
		this.drawTexturedModalRect(grayBoxX, grayBoxY, 0, 0, 256, 256);

		// Draw the buttons, labels and tabs.
		super.drawScreen(x, y, f);

		// Draw the text here.
		int color = Color.DARK_GRAY.getRGB();
	}
	
	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button == this.btnCancel)
		{
			this.mc.displayGuiScreen(null);
		}
		else if (button == this.btnBuild)
		{
			this.mc.displayGuiScreen(null);
		}
	}
	
	/**
	 * Processes when this tab is clicked.
	 * @param tab The tab which was clicked.
	 */
	@Override
	protected void tabClicked(GuiTab tab)
	{
		
	}
	
	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	@Override
	public boolean doesGuiPauseGame()
	{
		return true;
	}
	
	private void Initialize() 
	{
		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 188;
		int grayBoxY = (this.height / 2) - 83;

		// Create the buttons.
		
		this.test = new GuiTab(this.Tabs, "Test", grayBoxX + 3, grayBoxY-20);
		this.Tabs.AddTab(this.test);
		
		this.test2 = new GuiTab(this.Tabs, "Test 2", grayBoxX + 54, grayBoxY-20);
		this.Tabs.AddTab(this.test2);
		
		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, "Choose!");
		this.buttonList.add(this.btnBuild);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, "Cancel");
		this.buttonList.add(this.btnCancel);
	}
}
