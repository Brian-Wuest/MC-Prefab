package com.wuest.prefab.Structures.Gui;

import java.awt.Color;
import java.io.IOException;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiTabScreen;
import com.wuest.prefab.Gui.Controls.GuiCheckBox;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Structures.Config.ModerateHouseConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureModerateHouse;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * 
 * @author WuestMan
 *
 */
public class GuiModerateHouse extends GuiStructure
{
	protected ModerateHouseConfiguration configuration;
	protected GuiButtonExt btnHouseStyle;
	
	protected GuiCheckBox btnAddChest;
	protected GuiCheckBox btnAddChestContents;
	protected GuiCheckBox btnAddMineShaft;
	protected boolean allowItemsInChestAndFurnace = true;
	protected ModConfiguration serverConfiguration;
	
	public GuiModerateHouse()
	{
		super("Moderate House");
		
		this.structureConfiguration = EnumStructureConfiguration.ModerateHouse;
		
		if (!Minecraft.getMinecraft().player.capabilities.isCreativeMode)
		{
			this.allowItemsInChestAndFurnace = !ClientEventHandler.playerConfig.builtStarterHouse;
		}
	}
	
	@Override
	protected void Initialize() 
	{
		this.serverConfiguration = ((ClientProxy)Prefab.proxy).getServerConfiguration();
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("Moderate Houses", ModerateHouseConfiguration.class);
		this.configuration.pos = this.pos;
		int color = Color.DARK_GRAY.getRGB();

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - 212;
		int grayBoxY = this.getCenteredYAxis() - 83;

		this.btnHouseStyle = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 20, 90, 20, this.configuration.houseStyle.getDisplayName());
		this.buttonList.add(this.btnHouseStyle);
		
		// Create the buttons.
		this.btnVisualize = new GuiButtonExt(4, grayBoxX + 10, grayBoxY + 60, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));
		this.buttonList.add(this.btnVisualize);

		// Create the done and cancel buttons.
		this.btnBuild = new GuiButtonExt(1, grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));
		this.buttonList.add(this.btnBuild);

		this.btnCancel = new GuiButtonExt(2, grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
		this.buttonList.add(this.btnCancel);
		
		int x = grayBoxX + 130;
		int y = grayBoxY + 10;
		
		this.btnAddChest = new GuiCheckBox(6, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST), this.configuration.addChests);
		this.btnAddChest.setStringColor(color);
		this.btnAddChest.setWithShadow(false);
		this.buttonList.add(this.btnAddChest);
		y += 15;
		
		this.btnAddMineShaft = new GuiCheckBox(7, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_BUILD_MINESHAFT), this.configuration.addChestContents);
		this.btnAddMineShaft.setStringColor(color);
		this.btnAddMineShaft.setWithShadow(false);
		this.buttonList.add(this.btnAddMineShaft);
		y += 15;
		
		this.btnAddChestContents = new GuiCheckBox(8, x, y, GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_ADD_CHEST_CONTENTS), this.configuration.addMineshaft);
		this.btnAddChestContents.setStringColor(color);
		this.btnAddChestContents.setWithShadow(false);
		this.buttonList.add(this.btnAddChestContents);
	}
	
	/**
	 * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
	 */
	@Override
	public void drawScreen(int x, int y, float f) 
	{
		int grayBoxX = this.getCenteredXAxis() - 212;
		int grayBoxY = this.getCenteredYAxis() - 83;
		
		this.drawDefaultBackground();
		
		// Draw the control background.
		this.mc.getTextureManager().bindTexture(this.configuration.houseStyle.getHousePicture());
		GuiTabScreen.drawModalRectWithCustomSizedTexture(grayBoxX + 249, grayBoxY, 1, 
				this.configuration.houseStyle.getImageWidth(), this.configuration.houseStyle.getImageHeight(), 
				this.configuration.houseStyle.getImageWidth(), this.configuration.houseStyle.getImageHeight());
		
		this.drawControlBackgroundAndButtonsAndLabels(grayBoxX, grayBoxY, x, y);

		this.btnAddChest.visible = this.serverConfiguration.addChests;
		this.btnAddChestContents.visible = this.allowItemsInChestAndFurnace && this.serverConfiguration.addChestContents;
		this.btnAddMineShaft.visible = this.serverConfiguration.addMineshaft;
		
		// Draw the text here.
		this.mc.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.STARTER_HOUSE_STYLE), grayBoxX + 10, grayBoxY + 10, this.textColor);

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
		this.configuration.addChests = this.btnAddChest.visible && this.btnAddChest.isChecked();
		this.configuration.addChestContents = this.allowItemsInChestAndFurnace ? this.btnAddChestContents.visible && this.btnAddChestContents.isChecked() : false;
		this.configuration.addMineshaft = this.btnAddMineShaft.visible && this.btnAddMineShaft.isChecked();
		
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);
		
		if (button == this.btnHouseStyle)
		{
			int id = this.configuration.houseStyle.getValue() + 1;
			this.configuration.houseStyle = ModerateHouseConfiguration.HouseStyle.ValueOf(id);
			
			this.btnHouseStyle.displayString = this.configuration.houseStyle.getDisplayName();
		}
		else if (button == this.btnVisualize)
		{
			StructureModerateHouse structure = StructureModerateHouse.CreateInstance(this.configuration.houseStyle.getStructureLocation(), StructureModerateHouse.class);
			StructureRenderHandler.setStructure(structure, EnumFacing.NORTH, this.configuration);
			this.mc.displayGuiScreen(null);
		}
	}
}
