package com.wuest.prefab.Gui;

import java.awt.Color;
import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.FishPondConfiguration;
import com.wuest.prefab.Proxy.Messages.FishPondTagMessage;

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

		this.mc.fontRendererObj.drawString("Fish Pond Facing", grayBoxX + 10, grayBoxY + 10, color);
		
		// Draw the text here.
		this.mc.fontRendererObj.drawSplitString("The red box in the image on the right shows the block you clicked on.", grayBoxX + 147, grayBoxY + 10, 95, color);
		this.mc.fontRendererObj.drawSplitString("Note: If you're facing north, choose south so the pier is going away from you. The white border is just to show the size.", grayBoxX + 147, grayBoxY + 60, 95, color);
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
			FishPondConfiguration houseConfiguration = new FishPondConfiguration();
			houseConfiguration.pos = this.pos;
			houseConfiguration.houseFacing = EnumFacing.byName(this.btnHouseFacing.displayString);
			
			Prefab.network.sendToServer(new FishPondTagMessage(houseConfiguration.WriteToNBTTagCompound()));
			
			this.mc.displayGuiScreen(null);
		}
		else if (button == this.btnHouseFacing)
		{
			EnumFacing currentFacing = EnumFacing.byName(this.btnHouseFacing.displayString).rotateY();
			this.btnHouseFacing.displayString = currentFacing.getName();
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
		this.btnHouseFacing = new GuiButtonExt(3, grayBoxX + 10, grayBoxY + 20, 100, 20, this.configuration.houseFacing.getName());
		this.buttonList.add(this.btnHouseFacing);

		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, "Build!");
		this.buttonList.add(this.btnBuild);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, "Cancel");
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
