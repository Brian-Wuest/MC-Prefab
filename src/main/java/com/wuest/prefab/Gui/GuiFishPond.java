package com.wuest.prefab.Gui;

import java.awt.Color;
import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.FishPondConfiguration;
import com.wuest.prefab.Proxy.Messages.FishPondTagMessage;
import com.wuest.prefab.Render.StructureRenderHandler;
import com.wuest.prefab.StructureGen.CustomStructures.StructureFishPond;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiFishPond extends GuiScreen
{
	private static final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/defaultBackground.png");
	private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/fishPondTopDown.png");
	
	protected GuiButtonExt btnCancel;
	protected GuiButtonExt btnBuild;
	protected GuiButtonExt btnVisualize;
	
	public BlockPos pos;
	
	protected GuiButtonExt btnHouseFacing;
	protected FishPondConfiguration configuration;
	
	public GuiFishPond(int x, int y, int z)
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
		int grayBoxX = (this.width / 2) - 188;
		int grayBoxY = (this.height / 2) - 83;
		
		this.drawDefaultBackground();
		
		// Draw the control background.
		// Create class to de-compress image from resource path.
		// This class should inherit from "SimpleTexture" and override it's loadTexture method
		// After the buffered image has been loaded, the GlStateManager.bindTexture class should be called.
		// Will probably want to keep the buffered image around in a class so the resources aren't constantly being de-compressed as this happens on every tick.
		//BufferedImage image = ZipUtil.decompressImageResource(structureTopDown.getResourcePath());
		this.mc.getTextureManager().bindTexture(structureTopDown);
		
		this.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 0, 0, 151, 149, 151, 149);
		
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
		this.mc.fontRendererObj.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.FISH_POND_STRUCTURE_FACING), grayBoxX + 147, grayBoxY + 60, 95, color);
		
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
			Prefab.network.sendToServer(new FishPondTagMessage(this.configuration.WriteToNBTTagCompound()));
			
			this.mc.displayGuiScreen(null);
		}
		else if (button == this.btnHouseFacing)
		{
			this.configuration.houseFacing = this.configuration.houseFacing.rotateY();
			this.btnHouseFacing.displayString = GuiLangKeys.translateFacing(this.configuration.houseFacing);
		}
		else if (button == this.btnVisualize)
		{
			StructureFishPond structure = StructureFishPond.CreateInstance(StructureFishPond.ASSETLOCATION, StructureFishPond.class);
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
		this.configuration = new FishPondConfiguration();
		this.configuration.pos = this.pos;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 188;
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
	
    /**
     * Draws a textured rectangle Args: x, y, z, u, v, width, height, textureWidth, textureHeight
     */
    public static void drawModalRectWithCustomSizedTexture(int x, int y, int z, float u, float v, int width, int height, float textureWidth, float textureHeight)
    {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        
        vertexbuffer.pos((double)x, (double)(y + height), (double)z)
        	.tex((double)(u * f), (double)((v + (float)height) * f1)).endVertex();
        
        vertexbuffer.pos((double)(x + width), (double)(y + height), (double)z)
        	.tex((double)((u + (float)width) * f), (double)((v + (float)height) * f1)).endVertex();
        
        vertexbuffer.pos((double)(x + width), (double)y, (double)z)
        	.tex((double)((u + (float)width) * f), (double)(v * f1)).endVertex();
        
        vertexbuffer.pos((double)x, (double)y, (double)z)
        	.tex((double)(u * f), (double)(v * f1)).endVertex();
        
        tessellator.draw();
    }
}
