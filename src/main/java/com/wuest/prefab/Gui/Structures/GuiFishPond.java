package com.wuest.prefab.Gui.Structures;

import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.Structures.FishPondConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Render.StructureRenderHandler;
import com.wuest.prefab.StructureGen.CustomStructures.StructureFishPond;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * 
 * @author WuestMan
 *
 */
public class GuiFishPond extends GuiStructure
{
	private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/fish_pond_top_down.png");
	protected FishPondConfiguration configuration;
	
	public GuiFishPond(int x, int y, int z)
	{
		super(x, y, z, true);
		this.structureConfiguration = EnumStructureConfiguration.FishPond;
	}
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f) 
	{
		int grayBoxX = this.getCenteredXAxis() - 188;
		int grayBoxY = this.getCenteredYAxis() - 83;
		
		this.drawDefaultBackground();
		
		// Draw the control background.
		// Create class to de-compress image from resource path.
		// This class should inherit from "SimpleTexture" and override it's loadTexture method
		// After the buffered image has been loaded, the GlStateManager.bindTexture class should be called.
		// Will probably want to keep the buffered image around in a class so the resources aren't constantly being de-compressed as this happens on every tick.
		//BufferedImage image = ZipUtil.decompressImageResource(structureTopDown.getResourcePath());
		this.mc.getTextureManager().bindTexture(structureTopDown);
		
		this.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 151, 149, 151, 149);
		
		this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

		this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_FACING), grayBoxX + 10, grayBoxY + 10, this.textColor);
		
		// Draw the text here.
		this.mc.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), grayBoxX + 147, grayBoxY + 10, 95, this.textColor);
		this.mc.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.FISH_POND_STRUCTURE_FACING), grayBoxX + 147, grayBoxY + 60, 95, this.textColor);
		
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
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);
		
		if (button == this.btnVisualize)
		{
			StructureFishPond structure = StructureFishPond.CreateInstance(StructureFishPond.ASSETLOCATION, StructureFishPond.class);
			StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
			this.mc.displayGuiScreen(null);
		}
	}
	
	@Override
	protected void Initialize() 
	{
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("Fish Pond", FishPondConfiguration.class);
		this.configuration.pos = this.pos;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 188;
		int grayBoxY = (this.height / 2) - 83;

		// Create the buttons.	
		this.btnVisualize = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));
		this.buttonList.add(this.btnVisualize);

		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
		this.buttonList.add(this.btnBuild);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
		this.buttonList.add(this.btnCancel);
	}
}
