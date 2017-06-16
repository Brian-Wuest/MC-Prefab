package com.wuest.prefab.Gui.Structures;

import java.awt.Color;
import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.Structures.VillagerHouseConfiguration;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiTabScreen;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage;
import com.wuest.prefab.Proxy.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Render.StructureRenderHandler;
import com.wuest.prefab.StructureGen.CustomStructures.StructureVillagerHouses;
import com.wuest.prefab.StructureGen.CustomStructures.StructureWarehouse;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * 
 * @author WuestMan
 *
 */
public class GuiVillaerHouses extends GuiStructure
{
	private static final ResourceLocation backgroundTextures = new ResourceLocation("prefab", "textures/gui/default_background.png");
	protected GuiButtonExt btnHouseStyle;
	protected VillagerHouseConfiguration configuration;
	protected VillagerHouseConfiguration.HouseStyle houseStyle;
	
	public GuiVillaerHouses(int x, int y, int z)
	{
		super(x, y, z, true);
		this.structureConfiguration = EnumStructureConfiguration.VillagerHouses;
	}
	
	@Override
	public void Initialize()
	{
		this.configuration = new VillagerHouseConfiguration();
		this.configuration.pos = this.pos;
		this.configuration.houseFacing = EnumFacing.NORTH;
		this.houseStyle = this.configuration.houseStyle;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - 205;
		int grayBoxY = this.getCenteredYAxis() - 83;

		this.btnHouseStyle = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 20, 90, 20, this.houseStyle.getDisplayName());
		this.buttonList.add(this.btnHouseStyle);
		
		// Create the buttons.
		this.btnHouseFacing = new GuiButtonExt(3, grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.translateFacing(this.configuration.houseFacing));
		this.buttonList.add(this.btnHouseFacing);
		
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
		int grayBoxX = this.getCenteredXAxis() - 205;
		int grayBoxY = this.getCenteredYAxis() - 83;
		
		this.drawDefaultBackground();
		
		// Draw the control background.
		this.mc.getTextureManager().bindTexture(this.houseStyle.getHousePicture());
		GuiTabScreen.drawModalRectWithCustomSizedTexture(grayBoxX + 250, grayBoxY, 1, 
				this.houseStyle.getImageWidth(), this.houseStyle.getImageHeight(), 
				this.houseStyle.getImageWidth(), this.houseStyle.getImageHeight());
		
		this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

		// Draw the text here.
		this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), grayBoxX + 10, grayBoxY + 10, this.textColor);
		this.mc.fontRendererObj.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_FACING), grayBoxX + 10, grayBoxY + 50, this.textColor);

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
		this.configuration.houseStyle = this.houseStyle;
		
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);
		
		if (button == this.btnHouseStyle)
		{
			int id = this.houseStyle.getValue() + 1;
			this.houseStyle = VillagerHouseConfiguration.HouseStyle.ValueOf(id);
			
			this.btnHouseStyle.displayString = this.houseStyle.getDisplayName();
		}
		else if (button == this.btnVisualize)
		{
			StructureVillagerHouses structure = StructureVillagerHouses.CreateInstance(this.houseStyle.getStructureLocation(), StructureVillagerHouses.class);
			StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
			this.mc.displayGuiScreen(null);
		}
	}
}