package com.wuest.prefab.Gui;

import java.awt.Color;
import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.ChickenCoopConfiguration;
import com.wuest.prefab.Proxy.Messages.ChickenCoopTagMessage;
import com.wuest.prefab.Render.StructureRenderHandler;
import com.wuest.prefab.StructureGen.CustomStructures.StructureChickenCoop;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiChickenCoop extends GuiScreen
{
	private static final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/defaultBackground.png");
	private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/chickenCoopTopDown.png");
	
	protected GuiButtonExt btnCancel;
	protected GuiButtonExt btnBuild;
	protected GuiButtonExt btnVisualize;
	
	public BlockPos pos;
	
	protected GuiButtonExt btnHouseFacing;
	protected ChickenCoopConfiguration configuration;
	
	public GuiChickenCoop(int x, int y, int z)
	{
		this.pos = new BlockPos(x, y, z);
	}
	
	@Override
	public void initGui()
	{
		this.Initialize();
	}
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f) 
	{
		int grayBoxX = (this.width / 2) - 213;
		int grayBoxY = (this.height / 2) - 83;
		
		this.drawDefaultBackground();
		
		// Draw the control background.
		this.mc.getTextureManager().bindTexture(structureTopDown);
		GuiTabScreen.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 171, 87, 171, 87);
		
		this.mc.getTextureManager().bindTexture(backgroundTextures);
		this.drawTexturedModalRect(grayBoxX, grayBoxY, 0, 0, 256, 256);

		for (int i = 0; i < this.buttonList.size(); ++i)
		{
			((GuiButton)this.buttonList.get(i)).drawButton(this.mc, x, y);
		}

		for (int j = 0; j < this.labelList.size(); ++j)
		{
			((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, x, y);
		}

		// Draw the text here.
		int color = Color.DARK_GRAY.getRGB();

		this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_FACING), grayBoxX + 10, grayBoxY + 10, color);
		
		// Draw the text here.
		this.mc.fontRendererObj.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), grayBoxX + 147, grayBoxY + 10, 95, color);
		this.mc.fontRendererObj.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_DOOR_FACING), grayBoxX + 147, grayBoxY + 60, 95, color);
		
		if (!Prefab.proxy.proxyConfiguration.enableStructurePreview)
		{
			this.btnVisualize.enabled = false;
		}
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
			Prefab.network.sendToServer(new ChickenCoopTagMessage(this.configuration.WriteToNBTTagCompound()));
			
			this.mc.displayGuiScreen(null);
		}
		else if (button == this.btnHouseFacing)
		{
			this.configuration.houseFacing = this.configuration.houseFacing.rotateY();
			this.btnHouseFacing.displayString = GuiLangKeys.translateFacing(this.configuration.houseFacing);
		}
		else if (button == this.btnVisualize)
		{
			StructureChickenCoop structure = StructureChickenCoop.CreateInstance(StructureChickenCoop.ASSETLOCATION, StructureChickenCoop.class);
			StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
			this.mc.displayGuiScreen(null);
		}
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
		this.configuration = new ChickenCoopConfiguration();
		this.configuration.pos = this.pos;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 213;
		int grayBoxY = (this.height / 2) - 83;

		// Create the buttons.
		this.btnHouseFacing = new GuiButtonExt(3, grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateFacing(this.configuration.houseFacing));
		this.buttonList.add(this.btnHouseFacing);

		this.btnVisualize = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 50, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));
		this.buttonList.add(this.btnVisualize);
		
		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
		this.buttonList.add(this.btnBuild);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
		this.buttonList.add(this.btnCancel);
	}

}
