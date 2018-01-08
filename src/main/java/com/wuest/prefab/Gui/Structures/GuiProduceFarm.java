package com.wuest.prefab.Gui.Structures;

import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.Structures.NetherGateConfiguration;
import com.wuest.prefab.Config.Structures.ProduceFarmConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Render.StructureRenderHandler;
import com.wuest.prefab.StructureGen.CustomStructures.StructureProduceFarm;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * 
 * @author WuestMan
 *
 */
public class GuiProduceFarm extends GuiStructure
{
	private static final ResourceLocation houseTopDown = new ResourceLocation("prefab", "textures/gui/produce_farm_top_down.png");
	protected GuiButtonExt btnGlassColor;
	protected ProduceFarmConfiguration configuration;
	
	public GuiProduceFarm(int x, int y, int z)
	{
		super(x, y, z, true);
		this.structureConfiguration = EnumStructureConfiguration.ProduceFarm;
	}
	
	@Override
	public void Initialize()
	{
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("Produce Farm", ProduceFarmConfiguration.class);
		this.configuration.pos = this.pos;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - 210;
		int grayBoxY = this.getCenteredYAxis() - 83;

		// Create the buttons.
		this.btnGlassColor = new GuiButtonExt(10, grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateDye(this.configuration.dyeColor));
		this.buttonList.add(this.btnGlassColor);

		this.btnVisualize = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));
		this.buttonList.add(this.btnVisualize);
		
		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
		this.buttonList.add(this.btnBuild);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
		this.buttonList.add(this.btnCancel);
	}
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f) 
	{
		int grayBoxX = this.getCenteredXAxis() - 210;
		int grayBoxY = this.getCenteredYAxis() - 83;
		
		this.drawDefaultBackground();
		
		// Draw the control background.
		this.mc.getTextureManager().bindTexture(houseTopDown);
		this.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 170, 171, 170, 171);
		
		this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

		// Draw the text here.
		this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), grayBoxX + 10, grayBoxY + 10, this.textColor);
		
		// Draw the text here.
		this.mc.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), grayBoxX + 147, grayBoxY + 10, 100, this.textColor);
		this.mc.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.PRODUCE_FARM_SIZE), grayBoxX + 147, grayBoxY + 50, 100, this.textColor);
		
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
		
		if (button == this.btnGlassColor)
		{
			this.configuration.dyeColor = EnumDyeColor.byMetadata(this.configuration.dyeColor.getMetadata() + 1);
			this.btnGlassColor.displayString = GuiLangKeys.translateDye(this.configuration.dyeColor);
		}
		else if (button == this.btnVisualize)
		{
			StructureProduceFarm structure = StructureProduceFarm.CreateInstance(StructureProduceFarm.ASSETLOCATION, StructureProduceFarm.class);
			StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
			this.mc.displayGuiScreen(null);
		}
	}
}